
package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.UsersDto;

public interface UsersService {
	boolean saveUser(UsersDto usersDto);

	UsersDto findById(Integer id);

	List<UsersDto> findAll();

	void deleteUser(Integer id);
}
