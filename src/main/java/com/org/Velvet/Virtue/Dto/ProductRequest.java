package com.org.Velvet.Virtue.Dto;

import com.org.Velvet.Virtue.Dto.ProductsDto.CategoryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

	private String name;
	private String description;
	private Integer quantity;
	private boolean active;
	private double price;
	private int discount;
	private CategoryDto category;
	private ProductTypeDto productType;

	@Getter
	@Setter
	public static class CategoryDto {
		private String name;
	}
	@Getter
	@Setter
	public static class ProductTypeDto {
		private String type;
	}
}
