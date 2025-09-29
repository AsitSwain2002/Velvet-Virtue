package com.org.Velvet.Virtue.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.CartDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Cart;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.CartRepo;
import com.org.Velvet.Virtue.Repo.ProductRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private UsersRepo userRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public boolean addToCart(CartDto cartDto) {
		Cart cart = mapper.map(cartDto, Cart.class);
		if (cartDto.getId() != 0) {
			updateCart(cart, cartDto);
		} else {
			// change latter use product service find by id
			Products product = productRepo.findById(cartDto.getProductsId())
					.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
			Users user = userRepo.findById(cartDto.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("User Not found"));
			cart.setQuantity(cartDto.getQuantity());
		}
		Cart save = cartRepo.save(cart);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	private void updateCart(Cart cart, CartDto cartDto) {
		cart.setQuantity(cartDto.getQuantity());
	}

	@Override
	public boolean incrementProduct(int cartId) {
		Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
		if (cart.getProducts().getQuantity() > cart.getQuantity()) {
			cart.setQuantity(cart.getQuantity() + 1);
			cartRepo.save(cart);
			return true;
		}
		return false;
	}

	@Override
	public boolean decrementProduct(int cartId) {
		Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
		if (cart.getQuantity() != 1) {
			cart.setQuantity(cart.getQuantity() - 1);
			cartRepo.save(cart);
			return true;
		}
		return false;
	}

	@Override
	public List<CartDto> findAllCart(int userId) {
		Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
		List<Cart> findAllByUser = cartRepo.findAllByUser(user);
		return findAllByUser.stream().map(e -> mapper.map(e, CartDto.class)).collect(Collectors.toList());
	}

	@Override
	public void removeCart(int cartId) {
		Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
		cartRepo.delete(cart);
	}

}
