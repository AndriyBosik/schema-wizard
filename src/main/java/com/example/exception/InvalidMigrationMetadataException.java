package com.example.exception;

public class InvalidMigrationMetadataException extends RuntimeException {
    public InvalidMigrationMetadataException(String message) {
        super(message);
    }
}
