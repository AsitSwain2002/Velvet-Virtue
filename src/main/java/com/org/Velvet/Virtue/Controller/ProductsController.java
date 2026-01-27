package com.org.Velvet.Virtue.Controller;

import java.io.IOException;
import java.util.List;

import org.modelmapper.internal.bytebuddy.implementation.Implementation;
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

import com.org.Velvet.Virtue.Dto.ProductRequest;
import com.org.Velvet.Virtue.Dto.ProductResponse;
import com.org.Velvet.Virtue.Dto.ProductTypeDto;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;
import com.org.Velvet.Virtue.Dto.ReviewResponse;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.ProductService;
import com.org.Velvet.Virtue.service.ProductTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "All Product Service written here")
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
	@Operation(summary = "add the product - Seller can access", tags = { "Product" })
	@PreAuthorize("hasRole('SELLER')")
	@PostMapping(value = "/save-product", consumes = { "multipart/form-data" })
	public ResponseEntity<?> saveProduct(
			@RequestParam @Parameter(description = "Json String Products", required = true, content = @Content(schema = @Schema(implementation = ProductRequest.class))) String productsDto,
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
	@Operation(summary = "Add Product type - Access by Admin", tags = { "Product" })
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
	@Operation(summary = "Search Product - Access By User,Admin,Seller", tags = { "Product" })
	@PreAuthorize("hasAnyRole('SELLER','ADMIN','USER')")
	@GetMapping("/search-product")
	public ResponseEntity<?> searchProduct(@RequestParam String name,
			@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @RequestParam String sortBy,
			@RequestParam String sortType) {

		ProductResponse searchProduct = productService.searchProduct(name, pageNumber, pageSize, sortBy, sortType);

		if (!ObjectUtils.isEmpty(searchProduct)) {
			return ResponseBuilder.withData("Fetched", searchProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Product Found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get all products with pagination ----------------------------------
	 * Accessible by USER, SELLER, ADMIN
	 */
	@Operation(summary = "all product - Access by User,Admin,Seller", tags = { "Product" })
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
	@Operation(summary = "like the product - Acces by User", tags = { "Product" })
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
	@Operation(summary = "dislike the product - Access by User", tags = { "Product" })
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
	@Operation(summary = "all liked product - Access by User", tags = { "Product" })
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
	@Operation(summary = "add review - Access by User", tags = { "Product" })
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
	@Operation(summary = "delete product - Access by Seller and Admin", tags = { "Product" })
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
	@Operation(summary = "see all user review - Acess by User and Admin", tags = { "Product" })
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
	@Operation(summary = "See all review - Access by Admin and user", tags = { "Product" })
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
	@Operation(summary = "find user by id - Accesss by User , Admin , Seller", tags = { "Product" })
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