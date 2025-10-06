package com.org.Velvet.Virtue.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class ProductValidationException extends RuntimeException {

	Map<String, String> errors = new LinkedHashMap<String, String>();

	public ProductValidationException(Map<String, String> error) {
		super("Product Validation Failed");
		this.errors = error;
	}
}
