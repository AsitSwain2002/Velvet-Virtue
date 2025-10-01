package com.org.Velvet.Virtue.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.CartDto;
import com.org.Velvet.Virtue.Dto.ProductDeliveryDto;
import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Address;
import com.org.Velvet.Virtue.Model.Cart;
import com.org.Velvet.Virtue.Model.OrderStatus;
import com.org.Velvet.Virtue.Model.ProductDelivery;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.ProductDeliveryRepo;
import com.org.Velvet.Virtue.Repo.ProductRepo;
import com.org.Velvet.Virtue.Repo.StatusRepo;
import com.org.Velvet.Virtue.service.CartService;
import com.org.Velvet.Virtue.service.ProductDeliveryService;
import com.org.Velvet.Virtue.service.UsersService;

@Service
public class ProdutDeliveryServImpl implements ProductDeliveryService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductDeliveryRepo deliveryRepo;
	@Autowired
	private UsersService userService;

	@Autowired
	private StatusRepo statusRepo;
	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CartService cartService;

	@Override
	public boolean saveDelivery(ProductDeliveryDto deliveryDto) {
		int userId = 1;
		boolean res = false;
		List<CartDto> collect = cartService.findAllCart(userId);

		for (CartDto c : collect) {

			ProductDelivery order = mapper.map(deliveryDto, ProductDelivery.class);
			setOrderId(order);
			setStatus(order);
			setAddress(c, order);
			setProduct(c, order);
			setUser(c, order);
			setPrice(order, c);
			order.setOrderDate(LocalDate.now());
			ProductDelivery save = deliveryRepo.save(order);
			if (!ObjectUtils.isEmpty(save)) {
				res = true; 
			}
		}

		return res;
	}

	private void setOrderId(ProductDelivery order) {
		String string = UUID.randomUUID().toString();
		order.setOrderId(string);

	}

	private void setPrice(ProductDelivery order, CartDto c) {
		Products product = productRepo.findById(c.getProductsId())
				.orElseThrow(() -> new ResourceNotFoundException(" Product Not Found"));
		Integer quantity = c.getQuantity();
		order.setPrice(product.getPriceAfterDiscount() * quantity);

	}

	private void setProduct(CartDto c, ProductDelivery order) {
		Products product = productRepo.findById(c.getProductsId())
				.orElseThrow(() -> new ResourceNotFoundException(" Product Not Found"));
		order.setQuantity(c.getQuantity());
		order.setProducts(product);

	}

	private void setUser(CartDto c, ProductDelivery order) {
		UsersDto findById = userService.findById(c.getUserId());
		order.setUsers(mapper.map(findById, Users.class));
	}

	private void setAddress(CartDto c, ProductDelivery order) {
		if (!ObjectUtils.isEmpty(order.getAddress())) {
			UsersDto findById = userService.findById(c.getUserId());
			Address address = order.getAddress();
			address.setUsers(mapper.map(findById, Users.class));
			order.setAddress(address);
		} else {
			throw new NullPointerException();
		}

	}

	private void setStatus(ProductDelivery order) {
		int ststusId = 1;
		OrderStatus orderStatus = statusRepo.findById(ststusId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Status is Invalid"));
		order.setOrderStatus(orderStatus);
	}

}
