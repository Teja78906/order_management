package com.example.order_management_system.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor that accepts both a message and a cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

