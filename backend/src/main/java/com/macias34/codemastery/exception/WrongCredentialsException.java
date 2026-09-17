package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class WrongCredentialsException extends AbstractApiException {
	public WrongCredentialsException(String description) {
		super("Wrong credentials", description, HttpStatus.BAD_REQUEST);
	}

	public WrongCredentialsException() {
		super("Wrong credentials", null, HttpStatus.BAD_REQUEST);
	}
}
