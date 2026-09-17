package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends AbstractApiException {

	public ResourceAlreadyExistsException() {
		super("Resource already exists", null, HttpStatus.CONFLICT, null);
	}

	public ResourceAlreadyExistsException(String description) {
		super("Resource already exists", description, HttpStatus.CONFLICT, null);
	}
}