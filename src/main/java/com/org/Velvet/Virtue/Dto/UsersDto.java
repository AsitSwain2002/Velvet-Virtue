package com.org.Velvet.Virtue.Dto;

import lombok.Data;

@Data
public class UsersDto {
	private int id;
	private String firstName;
	private String lastame;
	private String mobile;
	private String email;
	private String password;
	private String age;
	private AddressDto address;
}
