package com.org.Velvet.Virtue.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
@Tag(name = "Home", description = "All Home Operation")
public class HomeController {

	@Autowired
	private UsersService usersService;

	@Operation(summary = "Forget Password - Access by Admin,User,Seller", tags = { "Home" })
	@PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
	@GetMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(@RequestParam String email, HttpServletRequest req)
			throws MessagingException {
		usersService.forgetPassword(email, req);
		return ResponseBuilder.withOutData("Email Sent Successfully", HttpStatus.OK);
	}

	@Operation(summary = "Reset Password - Access by Admin,User,Seller", tags = { "Home" })
	@PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestParam String password, @RequestParam String confirmPassword)
			throws Exception {
		boolean resetPassword = usersService.resetPassword(password, confirmPassword);
		if (resetPassword) {
			return ResponseBuilder.withOutData("Password Reset Successfully", HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
