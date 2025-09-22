package com.org.Velvet.Virtue.Dto;

import lombok.Data;

@Data
public class CategoryDto {

	private int id;
	private String name;
	private boolean isActive;
	private boolean isDeleted;
}
