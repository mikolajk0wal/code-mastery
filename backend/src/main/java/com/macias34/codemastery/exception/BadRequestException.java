package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractApiException {
    public BadRequestException(String description){
        super("Bad request", description, HttpStatus.BAD_REQUEST);
    }
    public BadRequestException(){
        super("Bad request", null, HttpStatus.BAD_REQUEST);
    }
}