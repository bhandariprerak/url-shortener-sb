package com.url.shortener.exceptions;

// Custom exception for when a user is not found in the database
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}