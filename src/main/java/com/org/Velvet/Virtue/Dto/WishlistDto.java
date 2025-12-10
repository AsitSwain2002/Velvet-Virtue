package com.org.Velvet.Virtue.Dto;

import lombok.Data;

@Data
public class WishlistDto {

	private int id;
	private UsersDto usersDto;
	private ProductsDto productsDto;
	private boolean isDeleted;
}
