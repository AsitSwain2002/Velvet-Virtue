package com.org.Velvet.Virtue.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.org.Velvet.Virtue.Dto.ProductResponse;
import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Dto.ReviewDto;
import com.org.Velvet.Virtue.Dto.ReviewResponse;

public interface ProductService {

	boolean saveProduct(String productDto, List<MultipartFile> file) throws IOException;

	void deleteProduct(int id);

	ProductResponse allProduct(int pageNum, int pagSize);

	List<ProductsDto> searchProduct(String name);

	boolean likeProduct(int id);

	boolean dislikeProduct(int id);

	ProductResponse allLikedProduct(int userId, int pageNum, int pagSize);

	boolean addReview(ReviewDto reviewDto);

	void deleteReview(int reviewId);

	ReviewResponse allReviewByUser(int userId, int pageNum, int pagSize);

	ReviewResponse allReviews(int pageNum, int pagSize);

	ProductsDto findByProductId(int priductId);

}
