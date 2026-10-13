package com.example.focuslog_api.exception;

/** Thrown when login fails. Deliberately vague about which part was wrong (HTTP 401). */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
