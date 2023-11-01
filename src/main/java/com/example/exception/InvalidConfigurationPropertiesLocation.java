package com.example.exception;

public class InvalidConfigurationPropertiesLocation extends RuntimeException {
    public InvalidConfigurationPropertiesLocation(String message) {
        super(message);
    }
}
