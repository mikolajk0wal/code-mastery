package com.macias34.codemastery.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ApiErrorResponse> handleApiException(AbstractApiException e){
        return ResponseEntity.status(e.getCode()).body(new ApiErrorResponse(
                e.getMessage(),
                e.getDescription(),
                e.getCode()
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    BadRequestException handleConstraintViolationException(ConstraintViolationException e) {
        String message = "not valid due to validation error: " + e.getMessage();
        return new BadRequestException(message);
    }
}