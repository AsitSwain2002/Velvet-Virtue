package com.org.Velvet.Virtue.ExceptionHandler;

public class InvalidJwtToken extends RuntimeException {

	public InvalidJwtToken(String msg) {
		super(msg);
	}
}
