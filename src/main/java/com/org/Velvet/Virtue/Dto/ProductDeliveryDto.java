package com.org.Velvet.Virtue.Dto;

import java.time.LocalDate;

import com.org.Velvet.Virtue.Model.Address;
import com.org.Velvet.Virtue.Model.OrderStatus;
import com.org.Velvet.Virtue.Model.Products;
import com.org.Velvet.Virtue.Model.Users;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class ProductDeliveryDto {
	private int id;
	private String orderId;
	private boolean deleted;
	private double price;
	private int quantity;
	private LocalDate orderDate;
	private ProductsDto products;
	private OrderStatusDto orderStatus;
	private UsersDto users;
	private AddressDto address;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderStatusDto {
		private int id;
	}
}
