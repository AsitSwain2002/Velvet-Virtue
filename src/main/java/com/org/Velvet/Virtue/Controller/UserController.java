package com.org.Velvet.Virtue.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.Velvet.Virtue.Dto.DelhiveryResponse;
import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.ResponseBuilder;
import com.org.Velvet.Virtue.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
@Tag(name = "User", description = "All user operation")
public class UserController {
	@Autowired
	private UsersService userService;

	@Operation(summary = "Register User/Admin/Seller", tags = { "User" })
	@PostMapping("/save-user")
	public ResponseEntity<?> saveUser(@RequestBody UsersDto usersDto, HttpServletRequest req)
			throws MessagingException {
		String url = CommonUtil.getUrl(req);
		boolean saveUser = userService.saveUser(usersDto, url);
		if (saveUser) {
			return ResponseBuilder.withOutData("Saved Successfully", HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "see all order - Access by user", tags = { "User" })
	@GetMapping("/orders")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> orders(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "8") int pageSize) {

		DelhiveryResponse orders = userService.orders(pageNumber, pageSize);
		if (!CollectionUtils.isEmpty(orders.getProductDelhiveryDto())) {
			return ResponseBuilder.withData("Fetched Successfully", orders, HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("No Order Found", HttpStatus.OK);
	}

	@Operation(summary = "find user by id - Access by User and Admin", tags = { "User" })
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> findById(@PathVariable int id) {

		UsersDto user = userService.findById(id);
		if (!ObjectUtils.isEmpty(user)) {
			return ResponseBuilder.withData("Fetched Successfully", user, HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("No User Found", HttpStatus.OK);
	}

	@Operation(summary = "See all user - Acess by Admin", tags = { "User" })
	@GetMapping("/allUser")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> alluser() {

		List<UsersDto> allUser = userService.findAll();
		if (!CollectionUtils.isEmpty(allUser)) {
			return ResponseBuilder.withData("Fetched Successfully", allUser, HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("No User Found", HttpStatus.OK);
	}

	@Operation(summary = "soft delete user - Access by Admin", tags = { "User" })
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	@PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
	@PostMapping("/verify-password-link/{userId}")
	public ResponseEntity<?> verifyPassword(@PathVariable int userId,@RequestParam String vCode){
		boolean passwordReset = userService.passwordReset(userId, vCode);
		if(passwordReset) {
			return ResponseBuilder.withOutData("Verification Sucessfull", HttpStatus.OK);
		}
		return ResponseBuilder.withOutData("Invalid Verification Link", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
