package com.org.Velvet.Virtue.Dto;

import java.util.List;

import com.org.Velvet.Virtue.Model.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductsDto {
	private Integer id;
	private String name;
	private String description;
	private Integer quantity;
	private boolean active;
	private boolean deleted;
	private double price;
	private int discount;
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
