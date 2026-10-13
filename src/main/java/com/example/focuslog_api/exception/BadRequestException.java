package com.example.focuslog_api.exception;

/** Thrown when a request is well-formed but breaks a business rule (HTTP 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
