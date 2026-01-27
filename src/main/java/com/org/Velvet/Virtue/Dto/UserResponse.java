package com.org.Velvet.Virtue.Dto;

import java.util.List;

import com.org.Velvet.Virtue.Dto.UsersDto.RolesDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class UserResponse {
	private Integer id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
	private String age;
	private AddressDto address;
	private List<RolesDto> roles;

	@Getter
	@Setter
	public static class RolesDto {

		private int id;
	}
}
