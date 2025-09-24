package com.org.Velvet.Virtue.Dto;

import java.util.List;

import com.org.Velvet.Virtue.Model.Category;
import lombok.Data;

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
	private Category category;
	private List<FileDetailsDto> fileDetails;
}
