package com.org.Velvet.Virtue.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.org.Velvet.Virtue.Dto.ProductResponse;
import com.org.Velvet.Virtue.Dto.ProductTypeDto;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;
import com.org.Velvet.Virtue.Dto.ReviewResponse;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductService;
import com.org.Velvet.Virtue.service.ProductTypeService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductTypeService productTypeService;

	/**
	 * Save a new product ---------------------------------- Accessible only by
	 * SELLER Accepts product data as JSON string Accepts multiple images as
	 * multipart files
	 */
	@PreAuthorize("hasRole('SELLER')")
	@PostMapping(value = "/save-product", consumes = { "multipart/form-data" })
	public ResponseEntity<?> saveProduct(@RequestParam String productsDto,
			@RequestParam(required = false) List<MultipartFile> files) throws IOException {

		boolean product = productService.saveProduct(productsDto, files);

		if (product) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Save product type / category ---------------------------------- Accessible
	 * only by ADMIN
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/save-product-type")
	public ResponseEntity<?> saveProductType(@RequestBody ProductTypeDto dto) {

		boolean saveType = productTypeService.saveType(dto);

		if (saveType) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Search product by name ---------------------------------- Accessible by
	 * SELLER and ADMIN
	 */
	@PreAuthorize("hasAnyRole('SELLER','ADMIN')")
	@GetMapping("/search-product/{name}")
	public ResponseEntity<?> searchProduct(@PathVariable String name) {

		List<ProductsDto> allProduct = productService.searchProduct(name);

		if (!ObjectUtils.isEmpty(allProduct)) {
			return ResponseBuilder.withData("Fetched", allProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Product Found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get all products with pagination ----------------------------------
	 * Accessible by USER, SELLER, ADMIN
	 */
	@PreAuthorize("hasAnyRole('SELLER','ADMIN','USER')")
	@GetMapping("/all-product")
	public ResponseEntity<?> allProduct(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "16") int pageSize) {

		ProductResponse allProduct = productService.allProduct(pageNumber, pageSize);

		if (!ObjectUtils.isEmpty(allProduct)) {
			return ResponseBuilder.withData("Fetched Successfully", allProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Like a product ---------------------------------- Accessible only by USER
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping("like-product/{productId}")
	public ResponseEntity<?> likeProduct(@PathVariable int productId) {

		boolean like = productService.likeProduct(productId);

		if (like) {
			return ResponseBuilder.withOutData("Liked Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Dislike a product ---------------------------------- Accessible only by USER
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping("dislike-product/{productId}")
	public ResponseEntity<?> dislikeProduct(@PathVariable int productId) {

		boolean dislike = productService.dislikeProduct(productId);

		if (dislike) {
			return ResponseBuilder.withOutData("DisLiked Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get all liked products of logged-in user ----------------------------------
	 * Accessible only by USER
	 */
	@PreAuthorize("hasRole('USER')")
	@GetMapping("all-likedProducts")
	public ResponseEntity<?> allLikedProduct(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "16") int pageSize) {

		int userId = CommonUtil.getLoggedUser().getId();
		ProductResponse allLikedProduct = productService.allLikedProduct(userId, pageNumber, pageSize);

		if (!ObjectUtils.isEmpty(allLikedProduct)) {
			return ResponseBuilder.withData("Fetched Successfully", allLikedProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Liked Product Found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Add review to a product ---------------------------------- Accessible only by
	 * USER
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping("add-review")
	public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto) {

		boolean review = productService.addReview(reviewDto);

		if (review) {
			return ResponseBuilder.withOutData("Review Added Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete a product ---------------------------------- Accessible by ADMIN and
	 * SELLER
	 */
	@PreAuthorize("hasAnyRole('SELLER','ADMIN')")
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable int productId) {

		productService.deleteProduct(productId);
		return ResponseBuilder.withOutData("Product Deleted", HttpStatus.NO_CONTENT);
	}

	/**
	 * Get all reviews added by logged-in user ----------------------------------
	 * Accessible by USER and ADMIN
	 */
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("all-user-review")
	public ResponseEntity<?> allUserReview(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "16") int pageSize) {

		int userId = CommonUtil.getLoggedUser().getId();
		ReviewResponse allReviewByUser = productService.allReviewByUser(userId, pageNumber, pageSize);

		if (!ObjectUtils.isEmpty(allReviewByUser)) {
			return ResponseBuilder.withData("Fetched Successfully", allReviewByUser, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Review Found", HttpStatus.OK);
		}
	}

	/**
	 * Get all product reviews ---------------------------------- Accessible by USER
	 * and ADMIN
	 */
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("all-review")
	public ResponseEntity<?> allReview(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "16") int pageSize) {

		ReviewResponse allReviews = productService.allReviews(pageNumber, pageSize);

		if (!ObjectUtils.isEmpty(allReviews)) {
			return ResponseBuilder.withData("Fetched Successfully", allReviews, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Review Found", HttpStatus.OK);
		}
	}

	/**
	 * Get product details by product ID ----------------------------------
	 * Accessible by ADMIN, USER, SELLER
	 */
	@PreAuthorize("hasAnyRole('ADMIN','USER','SELLER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) {

		ProductsDto product = productService.findByProductId(id);

		if (!ObjectUtils.isEmpty(product)) {
			return ResponseBuilder.withData("Fetched Successfully", product, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Product Found", HttpStatus.OK);
		}
	}
}