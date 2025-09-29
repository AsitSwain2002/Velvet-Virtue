package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.CartDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.CartService;

@RestController
@RequestMapping("api/v1/cart/")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("save-cart")
	public ResponseEntity<?> savCart(@RequestBody CartDto cartDto) {
		boolean addToCart = cartService.addToCart(cartDto);
		if (addToCart) {
			return ResponseBuilder.withOutData("Increment Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("increment/{cartId}")
	public ResponseEntity<?> increment(@PathVariable int cartId) {
		boolean addToCart = cartService.incrementProduct(cartId);
		if (addToCart) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Out Of Stock", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("decrement/{cartId}")
	public ResponseEntity<?> decrement(@PathVariable int cartId) {
		boolean addToCart = cartService.decrementProduct(cartId);
		if (addToCart) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("Bad Request", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("all-cart-item")
	public ResponseEntity<?> allCartItem() {
		int userId = 1;
		List<CartDto> allCart = cartService.findAllCart(userId);
		if (!CollectionUtils.isEmpty(allCart)) {
			return ResponseBuilder.withData("Fetched Successfully", allCart, HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withOutData("No Data Found", HttpStatus.NO_CONTENT);
		}
	}

	@DeleteMapping("delete-cart/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable int id) {
		cartService.removeCart(id);
		return ResponseBuilder.withOutData("Deleted", HttpStatus.NO_CONTENT);
	}
}
