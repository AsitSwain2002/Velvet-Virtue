
package com.org.Velvet.Virtue.ExceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.org.Velvet.Virtue.Util.ResponseBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public static ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public static ResponseEntity<?> resourceNotFound(IllegalArgumentException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public static ResponseEntity<?> badCredentialsException(BadCredentialsException e) {
		return ResponseBuilder.withOutData(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullPointerException.class)
	public static ResponseEntity<?> nullPointerException(NullPointerException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ReviewNotAllowedException.class)
	public static ResponseEntity<?> reviewNotAllowedException(ReviewNotAllowedException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(CategoryValidationException.class)
	public static ResponseEntity<?> categoryValidationException(CategoryValidationException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProductValidationException.class)
	public static ResponseEntity<?> categoryValidationException(ProductValidationException ex) {
		return ResponseBuilder.exceptionDetails(ex.getMessage(), ex.getErrors(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProductTypeValidationException.class)
	public static ResponseEntity<?> productTypeValidationException(ProductTypeValidationException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserValidationException.class)
	public static ResponseEntity<?> userValidationException(UserValidationException ex) {
		return ResponseBuilder.exceptionDetails(ex.getMessage(), ex.getError(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AlreadyVerifiedException.class)
	public static ResponseEntity<?> alreadyVerified(AlreadyVerifiedException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccountNotActivated.class)
	public static ResponseEntity<?> accountNotActivated(AccountNotActivated ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ClassCastException.class)
	public static ResponseEntity<?> classCatException(ClassCastException ex) {
		return ResponseBuilder.withOutData(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
