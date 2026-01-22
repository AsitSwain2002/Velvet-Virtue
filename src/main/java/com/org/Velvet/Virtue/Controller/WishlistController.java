package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.ProductsDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/wishlist")
@Tag(name = "Wishlist", description = "All wishlist service")
public class WishlistController {
	@Autowired
	private WishlistService wishlistService;

	@Operation(summary = "add product to wishlist - Access by user", tags = { "Wishlist" })
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/add/{productId}")
	public ResponseEntity<?> addToWatchList(@PathVariable int productId) {
		boolean toWishList = wishlistService.addToWishList(productId);
		if (toWishList) {
			return ResponseBuilder.withOutData("Added to WishList", HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "all wishlist product - Access by user", tags = { "Wishlist" })
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/allWishlistProduct")
	public ResponseEntity<?> allWishListProduct() {
		List<ProductsDto> allWishlistproduct = wishlistService.allWishlistproduct();
		if (!CollectionUtils.isEmpty(allWishlistproduct)) {
			return ResponseBuilder.withData("Fetched Successfully", allWishlistproduct, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("No wishlist product found", HttpStatus.OK);
		}
	}

	@Operation(summary = "remove from wishlist - Access by user", tags = { "Wishlist" })
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/removeWishList/{productId}")
	public ResponseEntity<?> removeWishlist(@PathVariable int productId) {
		wishlistService.removeWishList(productId);

		return ResponseBuilder.withOutData("Remove from wishlist", HttpStatus.NO_CONTENT);
	}
}
