package com.example.exception;

public class ConflictDependencyException extends RuntimeException {
    public ConflictDependencyException(String message) {
        super(message);
    }
}
