package com.example.focuslog_api.exception;

/** Thrown when a request clashes with existing data, e.g. duplicate email (HTTP 409). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
