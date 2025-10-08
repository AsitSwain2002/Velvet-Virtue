package com.org.Velvet.Virtue.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.RequestDto;
import com.org.Velvet.Virtue.Dto.ResponseDto;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.AuthService;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestDto requestDto) {
		ResponseDto login = authService.login(requestDto);

		if (login != null) {
			return ResponseBuilder.withData("Login Successful", login, HttpStatus.OK);
		} else {
			return ResponseBuilder.withOutData("Invalid Credintial", HttpStatus.NOT_FOUND);
		}

	}
}
