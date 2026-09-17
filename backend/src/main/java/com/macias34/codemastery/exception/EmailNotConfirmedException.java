package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class EmailNotConfirmedException extends AbstractApiException {
	public EmailNotConfirmedException() {
		super("Email not confirmed", null, HttpStatus.UNAUTHORIZED, null);
	}

	public EmailNotConfirmedException(String description) {
		super("Email not confirmed", description, HttpStatus.UNAUTHORIZED, null);
	}
}
