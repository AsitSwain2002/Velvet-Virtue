package com.org.Velvet.Virtue.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.UsersDto;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

	@GetMapping("/save-user")
	public ResponseEntity<?> saveUser(@RequestBody UsersDto usersDto) {
		return null;
	}
}
