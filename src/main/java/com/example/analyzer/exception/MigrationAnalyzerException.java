package com.example.analyzer.exception;

public class MigrationAnalyzerException extends RuntimeException {
    public MigrationAnalyzerException(String message) {
        super(message);
    }

    public MigrationAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }
}
