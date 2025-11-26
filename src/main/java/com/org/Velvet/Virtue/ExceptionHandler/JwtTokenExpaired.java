package com.org.Velvet.Virtue.ExceptionHandler;

public class JwtTokenExpaired extends RuntimeException {

	public JwtTokenExpaired(String msg) {
		super(msg);
	}
}
