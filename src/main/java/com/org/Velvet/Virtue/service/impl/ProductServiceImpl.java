package com.org.Velvet.Virtue.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Velvet.Virtue.Dto.CategoryDto;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Category;
import com.org.Velvet.Virtue.Model.FileDetails;
import com.org.Velvet.Virtue.Model.ProductType;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Repo.FileRepo;
import com.org.Velvet.Virtue.Repo.ProductRepo;
import com.org.Velvet.Virtue.Repo.ProductTypeRepo;
import com.org.Velvet.Virtue.service.CategoryService;
import com.org.Velvet.Virtue.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileRepo fileRepo;

	@Autowired
	private ProductTypeRepo productTypeRepo;

	@Value("${file.upload.path}")
	private String folderName;

	@Override
	public boolean saveProduct(String reqProd, List<MultipartFile> file) throws IOException {

		ObjectMapper ob = new ObjectMapper();
		ProductsDto productDto = ob.readValue(reqProd, ProductsDto.class);
		int userid = 1;
		Products products = mapper.map(productDto, Products.class);

		// update category
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
			products.setCreatedBy(userid);
			products.setCreatedOn(new Date());
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
		int userid = 1;

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

		if (products.getQuantity() != 0) {
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
		productRepo.save(dbProducts);

	}

	@Override
	public List<ProductsDto> allProduct() {
		List<Products> products = productRepo.findAllByDeletedFalse();
		return products.stream().map(e -> mapper.map(e, ProductsDto.class)).collect(Collectors.toList());
	}

}
