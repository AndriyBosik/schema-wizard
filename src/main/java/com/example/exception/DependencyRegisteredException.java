package com.example.exception;

public class DependencyRegisteredException extends RuntimeException {
    public DependencyRegisteredException(String dependencyName) {
        super(String.format("Dependency with name %s has been already registered", dependencyName));
    }
}
