package com.org.Velvet.Virtue.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.Dto.ProductResponse;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;
import com.org.Velvet.Virtue.Dto.ReviewResponse;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.ExceptionHandler.ReviewNotAllowedException;
import com.org.Velvet.Virtue.Model.Category;
import com.org.Velvet.Virtue.Model.FileDetails;
import com.org.Velvet.Virtue.Model.LikedProduct;
import com.org.Velvet.Virtue.Model.ProductDelivery;
import com.org.Velvet.Virtue.Model.ProductType;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Review;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.FileRepo;
import com.org.Velvet.Virtue.Repo.LikedProductRepo;
import com.org.Velvet.Virtue.Repo.ProductRepo;
import com.org.Velvet.Virtue.Repo.ProductTypeRepo;
import com.org.Velvet.Virtue.Repo.ReviewRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.service.CategoryService;
import com.org.Velvet.Virtue.service.ProductService;
import com.org.Velvet.Virtue.validation.ProductValidation;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private LikedProductRepo likedProductRepo;

	@Autowired
	private ProductTypeRepo productTypeRepo;

	@Autowired
	private UsersRepo usersRepo;

	@Autowired
	private ReviewRepo reviewRepo;

	@Value("${file.upload.path}")
	private String folderName;
	@Autowired
	private ProductValidation productValidation;

	@Override
	public boolean saveProduct(String reqProd, List<MultipartFile> file) throws IOException {

		ObjectMapper ob = new ObjectMapper();
		ProductsDto productDto = ob.readValue(reqProd, ProductsDto.class);
		// validation code
		productValidation.validate(productDto);
		int userId = CommonUtil.getLoggedUser().getId();
		Products products = mapper.map(productDto, Products.class);

		// update product
		if (products.getId() != null) {
			return updateProducts(products);
		} else {
			// get the category
			CategoryDto category = categoryService.findById(productDto.getCategory().getId());
			products.setCategory(mapper.map(category, Category.class));

			// set discount here
			setDiscount(products);

			// set Type
			setType(products);

			// set file
			List<FileDetails> saveFile = saveFile(products, file);
			if (!CollectionUtils.isEmpty(saveFile)) {
				products.setFileDetails(saveFile);
			} else {
				products.setFileDetails(null);
			}

			// set created by
			products.setCreatedBy(userId);
			products.setCreatedOn(new Date());
			products.setActive(true);
			Products save = productRepo.save(products);
			if (!ObjectUtils.isEmpty(save)) {
				return true;
			}
			return false;
		}

	}

	private void setType(Products products) {
		ProductType orElseThrow = productTypeRepo.findById(products.getProductType().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Product Type Not Found"));
		products.setProductType(orElseThrow);
	}

	private List<FileDetails> saveFile(Products products, List<MultipartFile> files) throws IOException {
		List<FileDetails> savedFiles = new ArrayList();

		if (!ObjectUtils.isEmpty(files)) {
			for (MultipartFile file : files) {
				String originalFilename = file.getOriginalFilename();
				if (originalFilename == null)
					continue;

				// Get extension
				String extension = FilenameUtils.getExtension(originalFilename);

				// Create folder if not exist
				File newFile = new File(folderName);
				if (!newFile.exists()) {
					newFile.mkdirs();
				}

				// Generate random file name
				String random = UUID.randomUUID().toString();
				String uploadFileName = random + "." + extension;

				// Store file path
				String storePath = folderName + uploadFileName;

				// Copy file
				Files.copy(file.getInputStream(), Paths.get(storePath));

				// Save FileDetails in DB
				FileDetails fileDetails = new FileDetails();
				fileDetails.setOriginalFileName(originalFilename);
				fileDetails.setUploadFileName(uploadFileName);
				fileDetails.setDisplayFileName(displayFileName(originalFilename)); // your custom method
				fileDetails.setPath(storePath);
				fileDetails.setSize(file.getSize());

				fileDetails.setProducts(products);

				savedFiles.add(fileDetails);
			}
		}
		return savedFiles;
	}

	private String displayFileName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);
		if (fileName.length() > 8) {
			return fileName.substring(0, 8) + "." + extension;
		}
		return fileName + "." + extension;
	}

	private boolean updateProducts(Products products) {
		int userid = CommonUtil.getLoggedUser().getId();

		Products dbProducts = productRepo.findById(products.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));

		// update audit fields
		dbProducts.setUpdateBy(userid);
		dbProducts.setUpdateOn(new Date());
		dbProducts.setActive(products.isActive());
		// update fields only if passed
		if (products.getPrice() != 0) {
			dbProducts.setPrice(products.getPrice());
		}
		if (products.getQuantity() != null) {
			dbProducts.setQuantity(products.getQuantity());
		}

		if (products.getDiscount() != 0) {
			dbProducts.setDiscount(products.getDiscount());
		}

		// don't touch category
		dbProducts.setCategory(dbProducts.getCategory());

		// ✅ recalc discount every time price or discount changes
		setDiscount(dbProducts);

		// save here
		Products save = productRepo.save(dbProducts);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	private void setDiscount(Products products) {
		int discount = products.getDiscount();
		double price = products.getPrice();
		double discountPrice = price * ((double) discount / 100);

		products.setPriceAfterDiscount(price - discountPrice);
	}

	@Override
	public void deleteProduct(int id) {

		Products dbProducts = productRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		dbProducts.setDeleted(true);
		dbProducts.setDeletedOn(LocalDateTime.now());
		productRepo.save(dbProducts);

	}

	@Override
	public ProductResponse allProduct(int pageNum, int pagSize) {
		Pageable page = PageRequest.of(pageNum, pagSize);
		Page<Products> products = productRepo.findAllByDeletedFalseAndActiveTrue(page);
		List<ProductsDto> productDto = products.stream().map(e -> mapper.map(e, ProductsDto.class))
				.collect(Collectors.toList());
		return ProductResponse.builder().products(productDto).totalPage(products.getTotalPages())
				.pageNumber(products.getNumber()).pagesize(products.getSize()).isLastPage(products.isLast())
				.isfirstPage(products.isFirst()).totalElement(products.getTotalElements()).build();
	}

	@Override
	public ProductResponse searchProduct(String name, int pageNumber, int pageSize, String sortBy, String sortType) {
		Sort sort = sortType.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable of = PageRequest.of(pageNumber, pageSize, sort);
		Page<Products> products = productRepo.findByNameContainingIgnoreCaseOrProductType_TypeContainingIgnoreCase(name,
				name, of);
		List<ProductsDto> list = products.stream().map(e -> mapper.map(e, ProductsDto.class)).toList();
		return ProductResponse.builder().products(list).isfirstPage(products.isFirst()).isLastPage(products.isLast())
				.pageNumber(products.getNumber()).pagesize(products.getSize()).totalElement(products.getTotalElements())
				.totalPage(products.getTotalPages()).build();
	}

	// liked product logic
	@Override
	public boolean likeProduct(int id) {
		int userId = CommonUtil.getLoggedUser().getId();
		Products dbProducts = productRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		Users users = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		// 3. Check if already liked (avoid duplicates)
		List<LikedProduct> existingLike = likedProductRepo.findAllByUsersAndProducts(users, dbProducts);

		if (!ObjectUtils.isEmpty(existingLike)) {
			return false;
		}
		// 4. Save new like
		LikedProduct newLike = LikedProduct.builder().users(users).products(dbProducts).build();

		LikedProduct save = likedProductRepo.save(newLike);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean dislikeProduct(int id) {
		int userId = CommonUtil.getLoggedUser().getId();
		Products dbProducts = productRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));

		Users users = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LikedProduct likedProduct = likedProductRepo.findByUsersAndProducts(users, dbProducts);
		if (ObjectUtils.isEmpty(likedProduct)) {
			return false;
		}

		likedProductRepo.delete(likedProduct);
		return true;
	}

	@Override
	public ProductResponse allLikedProduct(int userId, int pageNum, int pagSize) {
		Users users = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Pageable of = PageRequest.of(pageNum, pagSize);
		Page<LikedProduct> likedProducts = likedProductRepo.findByUsers(users, of);
		// find product from likedProduct
		List<Products> likedProduct = likedProducts.stream().map(e -> e.getProducts()).collect(Collectors.toList());
		List<ProductsDto> productsDto = likedProducts.stream().map(e -> mapper.map(e, ProductsDto.class))
				.collect(Collectors.toList());
		return ProductResponse.builder().products(productsDto).totalPage(likedProducts.getTotalPages())
				.pageNumber(likedProducts.getNumber()).pagesize(likedProducts.getSize())
				.isLastPage(likedProducts.isLast()).isfirstPage(likedProducts.isFirst())
				.totalElement(likedProducts.getTotalElements()).build();
	}

	// add review logic written heres
	@Override
	public boolean addReview(ReviewDto reviewDto) {
		Review review = mapper.map(reviewDto, Review.class);
		Users users = usersRepo.findById(review.getUser().getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// check the user is buy the product or not
		checkProductBuyOrNoByUser(users, review);

		review.setUser(users);
		Products product = productRepo.findById(review.getProducts().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		review.setProducts(product);
		Review save = reviewRepo.save(review);
		if (!ObjectUtils.isEmpty(save)) {
			setRatingInProduct(product);
			productRepo.save(product);
			return true;
		}
		return false;
	}

	// logic for check the user is buy the product and the product is delivered to
	// the user after that user can give review
	private void checkProductBuyOrNoByUser(Users users, Review review) {

		Integer productId = review.getProducts().getId();
		List<ProductDelivery> productDeliveries = users.getProductDeliveries();
		ProductDelivery productDelivery = productDeliveries.stream().filter(e -> e.getProducts().getId() == productId)
				.findFirst().get();
		if (ObjectUtils.isEmpty(productDelivery) || !"DELIVERED".equals(productDelivery.getOrderStatus().getName())) {
			throw new ReviewNotAllowedException(
					"Your product is not delivered yet or you have not purchased the product.");
		}
	}

	// set product rating here
	private void setRatingInProduct(Products product) {

		List<Review> reviews = product.getReviews();
		if (reviews.isEmpty()) {
			product.setRating(0);
			return;
		}
		double avg = reviews.stream().mapToDouble(Review::getRating) // take only rating values
				.average() // find average
				.orElse(0.0);
		product.setRating(avg);

		// set rating count here
		product.setRatingCount(product.getRatingCount() + 1);

	}

	@Override
	public void deleteReview(int reviewId) {
		Review review = reviewRepo.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));
		review.setDeleted(true);
		reviewRepo.save(review);
	}

	@Override
	public ReviewResponse allReviewByUser(int userId, int pageNum, int pagSize) {
		Users users = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Pageable of = PageRequest.of(pageNum, pagSize);
		Page<Review> reviews = reviewRepo.findAllByUserAndDeleted(users, false, of);
		List<ReviewDto> reviewsDto = reviews.stream().map(e -> mapper.map(e, ReviewDto.class))
				.collect(Collectors.toList());
		return ReviewResponse.builder().reviewDto(reviewsDto).totalPage(reviews.getTotalPages())
				.pageNumber(reviews.getNumber()).pagesize(reviews.getSize()).isLastPage(reviews.isLast())
				.isfirstPage(reviews.isFirst()).totalElement(reviews.getTotalElements()).build();
	}

	@Override
	public ReviewResponse allReviews(int pageNum, int pagSize) {
		Pageable of = PageRequest.of(pageNum, pagSize);
		Page<Review> reviews = reviewRepo.findAllByDeletedFalse(of);
		List<ReviewDto> reviewsDto = reviews.stream().map(e -> mapper.map(e, ReviewDto.class))
				.collect(Collectors.toList());
		return ReviewResponse.builder().reviewDto(reviewsDto).totalPage(reviews.getTotalPages())
				.pageNumber(reviews.getNumber()).pagesize(reviews.getSize()).isLastPage(reviews.isLast())
				.isfirstPage(reviews.isFirst()).totalElement(reviews.getTotalElements()).build();
	}

	@Override
	public ProductsDto findByProductId(int productId) {
		Products product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
		return mapper.map(product, ProductsDto.class);
	}

}
