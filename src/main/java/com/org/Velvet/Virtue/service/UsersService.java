
package com.org.Velvet.Virtue.service;

import java.util.List;

import com.org.Velvet.Virtue.Dto.DelhiveryResponse;
import com.org.Velvet.Virtue.Dto.UsersDto;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface UsersService {
	boolean saveUser(UsersDto usersDto, String url) throws MessagingException;

	UsersDto findById(Integer id);

	List<UsersDto> findAll();

	void deleteUser(Integer id);

	DelhiveryResponse orders(int pageNumber, int pageSize);

	void forgetPassword(String userName, HttpServletRequest req) throws MessagingException;

	boolean passwordReset(int userId, String vCode);

	boolean resetPassword(String password);
}
