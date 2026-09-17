package com.macias34.codemastery.exception;

import org.springframework.http.HttpStatus;

public class NoPermissionException  extends AbstractApiException{
    public NoPermissionException() {
        super("You have not permission to this action", null, HttpStatus.UNAUTHORIZED, null);
    }

    public NoPermissionException(String description) {
        super("You have not permission to this action", description, HttpStatus.UNAUTHORIZED, null);
    }
}
