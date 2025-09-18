package com.org.Velvet.Virtue.Dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UsersDto {
	private Integer id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
	private String age;
	private List<AddressDto> address;
	private List<RolesDto> roles;

	@Getter
	@Setter
	public static class RolesDto {

		private int id;
	}
}
