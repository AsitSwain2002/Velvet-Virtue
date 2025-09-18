package com.org.Velvet.Virtue.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public static ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
