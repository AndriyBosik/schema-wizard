package com.example.exception;

import com.example.metadata.ErrorMessage;

public class DependencyRegisteredException extends RuntimeException {
    public DependencyRegisteredException(String dependencyName) {
        super(String.format(ErrorMessage.DEPENDENCY_ALREADY_REGISTERED_FORMAT, dependencyName));
    }
}
