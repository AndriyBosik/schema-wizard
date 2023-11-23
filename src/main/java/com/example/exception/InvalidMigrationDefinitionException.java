package com.example.exception;

public class InvalidMigrationDefinitionException extends RuntimeException {
    public InvalidMigrationDefinitionException(String message) {
        super(message);
    }
}
