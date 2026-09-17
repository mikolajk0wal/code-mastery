package com.macias34.codemastery.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private Integer statusCode;
    private String message;
    private String description;

    ApiErrorResponse(String message, String description, HttpStatus statusCode) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.description = description;
        this.statusCode = statusCode.value();
    }
}
