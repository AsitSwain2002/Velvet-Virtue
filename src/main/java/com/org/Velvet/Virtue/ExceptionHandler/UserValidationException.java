package com.org.Velvet.Virtue.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class UserValidationException extends RuntimeException {
	Map<String, String> error = new LinkedHashMap<String, String>();

	public UserValidationException(Map<String, String> error) {
		super("User Validation Failed");
		this.error = error;
	}
}