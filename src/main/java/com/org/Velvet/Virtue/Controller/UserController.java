package com.org.Velvet.Virtue.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.service.UsersService;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
	@Autowired
	private UsersService userService;

	@PostMapping("/save-user")
	public ResponseEntity<?> saveUser(@RequestBody UsersDto usersDto) {
		boolean saveUser = userService.saveUser(usersDto);
		if (saveUser) {
			return new ResponseEntity("Saved Successfully", HttpStatus.OK);
		}
		return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
