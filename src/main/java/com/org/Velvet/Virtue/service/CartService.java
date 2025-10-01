package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.CartDto;
import com.org.Velvet.Virtue.Model.Cart;

public interface CartService {

	boolean addToCart(CartDto cartDto);

	boolean incrementProduct(int cartId);

	boolean decrementProduct(int cartId);

	List<CartDto> findAllCart(int userId);

	void removeCart(int cartId);

	void removeAllCartItem(int userId);
}
