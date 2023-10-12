package com.example.exception;

public class InvalidEnvironmentVariable extends RuntimeException {
    public InvalidEnvironmentVariable(String message) {
        super(message);
    }
}
