package com.org.Velvet.Virtue.Dto;

import java.time.LocalDate;
import java.util.List;

import com.org.Velvet.Virtue.Dto.ProductsDto.CategoryDto;
import com.org.Velvet.Virtue.Dto.ProductsDto.FileDetailsDto;
import com.org.Velvet.Virtue.Dto.UsersDto.RolesDto;
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
		private String name;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UsersDto {
		private String firstName;
		private String lastName;
		private String mobile;
		private String email;
		private String password;
		private String age;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProductsDto {
		private Integer id;
		private String name;
		private double price;
		private CategoryDto category;
		private List<FileDetailsDto> fileDetails;
		private ProductTypeDto productType;

		@Getter
		@Setter
		public static class CategoryDto {
			private int id;
			private String name;
		}

		@Getter
		@Setter
		public static class FileDetailsDto {
			private String displayFileName;
		}
	}
}
