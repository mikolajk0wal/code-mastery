package com.macias34.codemastery.exception;


import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractApiException {

    public ResourceNotFoundException() {
        super("Resource not found", null, HttpStatus.NOT_FOUND, null);
    }

    public ResourceNotFoundException(String description) {
        super("Resource not found", description, HttpStatus.NOT_FOUND, null);
    }
}