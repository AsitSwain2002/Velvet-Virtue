package com.org.Velvet.Virtue.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.org.Velvet.Virtue.Dto.ProductTypeDto;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;
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

	@PostMapping("/save-product-type")
	public ResponseEntity<?> saveProductType(@RequestBody ProductTypeDto dto) {
		boolean saveType = productTypeService.saveType(dto);
		if (saveType) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/search-product/{name}")
	public ResponseEntity<?> searchProduct(@PathVariable String name) {
		List<ProductsDto> allProduct = productService.searchProduct(name);
		if (!ObjectUtils.isEmpty(allProduct)) {
			return ResponseBuilder.withData("fetched", allProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Product Found", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all-product")
	public ResponseEntity<?> allProduct() {
		List<ProductsDto> allProduct = productService.allProduct();
		if (!ObjectUtils.isEmpty(allProduct)) {
			return ResponseBuilder.withData("Fetched Successfully", allProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("like-product/{productId}")
	public ResponseEntity<?> likeProduct(@PathVariable int productId) {
		boolean like = productService.likeProduct(productId);
		if (like) {
			return ResponseBuilder.withOutData("Liked Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("dislike-product/{productId}")
	public ResponseEntity<?> dislikeProduct(@PathVariable int productId) {
		boolean dislike = productService.dislikeProduct(productId);
		if (dislike) {
			return ResponseBuilder.withOutData("DisLiked Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("all-likedProducts")
	public ResponseEntity<?> allLikedProduct() {
		int UserId = 1;
		List<ProductsDto> allLikedProduct = productService.allLikedProduct(UserId);
		if (!CollectionUtils.isEmpty(allLikedProduct)) {
			return ResponseBuilder.withData("Fetched Successfully", allLikedProduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Liked Product Found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("add-review")
	public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto) {
		boolean review = productService.addReview(reviewDto);
		if (review) {
			return ResponseBuilder.withOutData("Review Added  Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable int productId) {
		productService.deleteProduct(productId);
		return ResponseBuilder.withOutData("Product Deleted", HttpStatus.NO_CONTENT);
	}

	@GetMapping("all-user-review")
	public ResponseEntity<?> allUserReview() {
		int userId = 1;
		List<ReviewDto> allReviewByUser = productService.allReviewByUser(userId);
		if (!CollectionUtils.isEmpty(allReviewByUser)) {
			return ResponseBuilder.withData("Fetched Successfully", allReviewByUser, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Liked Product Found", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("all-review")
	public ResponseEntity<?> allReview() {

		List<ReviewDto> allReview = productService.allReviews();
		if (!CollectionUtils.isEmpty(allReview)) {
			return ResponseBuilder.withData("Fetched Successfully", allReview, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No Liked Product Found", HttpStatus.NOT_FOUND);
		}
	}

}
