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

import com.org.Velvet.Virtue.Dto.AddressDto;
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

import jakarta.persistence.criteria.Order;

@Service
public class ProductDeliveryServImpl implements ProductDeliveryService {

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
				cartService.removeAllCartItem(userId);
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

	@Override
	public boolean updateStatus(int deliveryId, int statusId) {
		ProductDelivery order = deliveryRepo.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Not Found"));
		OrderStatus status = statusRepo.findById(statusId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Status is Invalid"));
		order.setOrderStatus(status);
		deliveryRepo.save(order);
		return true;
	}

	@Override
	public void cancelOrder(int deliveryId) {
		ProductDelivery order = deliveryRepo.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Not Found"));
		OrderStatus status = statusRepo.findById(9)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Status is Invalid"));
		order.setOrderStatus(status);
		deliveryRepo.save(order);
	}

	@Override
	public ProductDeliveryDto trackDelivery(int deliveryId) {
		ProductDelivery order = deliveryRepo.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Not Found"));
		return mapper.map(order, ProductDeliveryDto.class);
	}

	@Override
	public List<ProductDeliveryDto> getDeliveryHistory(int userId) {
		UsersDto findById = userService.findById(userId);
		OrderStatus staus = statusRepo.findById(5).orElseThrow(() -> new ResourceNotFoundException(" Order Not Found"));
		List<ProductDelivery> allOrder = deliveryRepo.findAllByUsersAndOrderStatus(mapper.map(findById, Users.class),
				staus);
		return allOrder.stream().map(e -> mapper.map(e, ProductDeliveryDto.class)).collect(Collectors.toList());
	}

	@Override
	public boolean updateDeliveryAddress(int deliveryId, AddressDto newAddress) {
		// convert address dto to address
		Address reAddress = mapper.map(newAddress, Address.class);

		ProductDelivery order = deliveryRepo.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException(" Order Not Found"));
		Address dbAddress = order.getAddress();

		// update existing data on address table
		updateAddress(dbAddress, reAddress);

		order.setAddress(dbAddress);
		ProductDelivery save = deliveryRepo.save(order);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	private void updateAddress(Address address, Address newAddress) {
		if (newAddress.getHouseNo() != null) {
			address.setHouseNo(newAddress.getHouseNo());
		}
		if (newAddress.getStreetName() != null) {
			address.setStreetName(newAddress.getStreetName());
		}
		if (newAddress.getArea() != null) {
			address.setArea(newAddress.getArea());
		}
		if (newAddress.getLandmark() != null) {
			address.setLandmark(newAddress.getLandmark());
		}
		if (newAddress.getCity() != null) {
			address.setCity(newAddress.getCity());
		}
		if (newAddress.getDistrict() != null) {
			address.setDistrict(newAddress.getDistrict());
		}
		if (newAddress.getState() != null) {
			address.setState(newAddress.getState());
		}
		if (newAddress.getPincode() != null) {
			address.setPincode(newAddress.getPincode());
		}
		if (newAddress.getCountry() != null) {
			address.setCountry(newAddress.getCountry());
		}
		if (newAddress.getType() != null) {
			address.setType(newAddress.getType());
		}
	}

}
