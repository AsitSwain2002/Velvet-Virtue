package com.org.Velvet.Virtue.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
