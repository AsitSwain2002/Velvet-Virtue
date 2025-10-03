package com.org.Velvet.Virtue.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;

public interface ProductService {

	boolean saveProduct(String productDto, List<MultipartFile> file) throws IOException;

	void deleteProduct(int id);

	List<ProductsDto> allProduct();

	List<ProductsDto> searchProduct(String name);

	boolean likeProduct(int id);

	boolean dislikeProduct(int id);

	List<ProductsDto> allLikedProduct(int userId);

	boolean addReview(ReviewDto reviewDto);

	void deleteReview(int reviewId);

	List<ReviewDto> allReviewByUser(int userId);

	List<ReviewDto> allReviews();

}
