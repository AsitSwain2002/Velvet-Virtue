package com.org.Velvet.Virtue.validation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.Dto.UsersDto.RolesDto;
import com.org.Velvet.Virtue.ExceptionHandler.UserValidationException;
import com.org.Velvet.Virtue.Repo.RolesRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Util.Constant;

import ch.qos.logback.core.util.StringUtil;

@Component
public class UserValidation {
	@Autowired
	private UsersRepo usersRepo;
	@Autowired
	private RolesRepo rolesRepo;

	public void validateUser(UsersDto usersDto) {

		Map<String, String> error = new LinkedHashMap<String, String>();

		String firstName = usersDto.getFirstName();
		String lastName = usersDto.getLastName();
		String mobile = usersDto.getMobile();
		String password = usersDto.getPassword();
		String email = usersDto.getEmail();
		List<RolesDto> roles = usersDto.getRoles();

		if (ObjectUtils.isEmpty(usersDto)) {
			error.put("Json", "Can't send null value");
		} else {
			// ------- firstName validation ------
			if (firstName == null) {
				error.put("firstname", "firstname can't be null");
			} else if (firstName.length() < 3) {
				error.put("firstname", "firstname length is too short (Minimum 4 character required)");
			} else if (firstName.length() >= 20) {
				error.put("firstname", "firstname length is too big (maximum 20 character allowed)");
			}

			// ------- LastName validation ------
			if (lastName == null) {
				error.put("lastName", "lastName can't be null");
			} else if (lastName.length() < 3) {
				error.put("lastName", "lastName length is too short (Minimum 4 character required)");
			} else if (firstName.length() >= 20) {
				error.put("lastName", "lastName length is too big (maximum 20 character allowed)");
			}

			// ------- Mobile validation ------
			if (mobile == null) {
				error.put("mobile", "mobile number can't be null");
			} else if (mobile.length() != 10) {
				error.put("mobile", "mobile number is invalid");
			}
			if (mobile == null) {
				error.put("mobile", "mobile number can't be null");
			} else if (mobile.length() != 10) {
				error.put("mobile", "mobile number is invalid");
			}

			// ------- Password validation ------
			if (password == null) {
				error.put("password", "password can't be null");
			} else if (password.length() < 6) {
				error.put("password", "Minimum 6 character required");
			} else if (password.length() > 8) {
				error.put("password", "Maximum 8 character allowed");
			}

			// ------- Email validation ------
			if (usersRepo.existsByEmail(email)) {
				error.put("Email", "Email already exists");
			}
			if (email == null) {
				error.put("Email", "Email can't be null");
			} else if (!email.matches(Constant.EMAIL_REGX)) {
				error.put("Email", "Email is invalid");
			}

			// ------- Role validation ------
			if (CollectionUtils.isEmpty(roles)) {
				error.put("Role", "Role can't be null");
			} else {
				List<Integer> roleId = rolesRepo.findAll().stream().map(e -> e.getId()).collect(Collectors.toList());
				List<Integer> list = roles.stream().map(e -> e.getId()).filter(r -> !roleId.contains(r)).toList();
				if (!CollectionUtils.isEmpty(list)) {
					error.put("Role", "Invalid Role");
				}

			}
		}

		if (!ObjectUtils.isEmpty(error)) {
			throw new UserValidationException(error);
		}
	}
}
