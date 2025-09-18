package com.org.Velvet.Virtue.ExceptionHandler;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}
