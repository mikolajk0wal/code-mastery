package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends AbstractApiException {

    public StorageException() {
        super("Error with file", null, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public StorageException(String description) {
        super("Error with file", description, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}
