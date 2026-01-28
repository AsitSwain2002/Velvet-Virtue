package com.org.Velvet.Virtue.Dto;

import lombok.Data;

@Data
public class ReviewDto {

	private int id;
	private double rating;
	private String comment;
	private int user_id;
	private int product_id;
}
 