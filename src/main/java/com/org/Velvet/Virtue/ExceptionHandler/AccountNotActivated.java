package com.org.Velvet.Virtue.ExceptionHandler;

public class AccountNotActivated extends RuntimeException {

	public AccountNotActivated(String msg) {
		super(msg);
	}
}
