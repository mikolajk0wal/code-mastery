package com.macias34.codemastery.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractApiException extends RuntimeException {

    private final HttpStatus code;
    private final String description;
    private final String title;
    private final Throwable cause;

    public AbstractApiException(String title, String description, HttpStatus code) {
        super(title + (description != null ? (": " + description) : ""), null);
        this.code = code;
        this.title = title;
        this.description = description;
        this.cause = null;
    }

    public AbstractApiException(String title, String description, HttpStatus code, Throwable cause) {
        super(title + (description != null ? (": " + description) : ""), cause);
        this.code = code;
        this.title = title;
        this.description = description;
        this.cause = cause;
    }
}