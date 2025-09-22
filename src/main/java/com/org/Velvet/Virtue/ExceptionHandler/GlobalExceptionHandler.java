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
}
