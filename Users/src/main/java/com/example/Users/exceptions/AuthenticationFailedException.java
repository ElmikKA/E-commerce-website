package com.example.Users.exceptions;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
