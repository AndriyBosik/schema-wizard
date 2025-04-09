package io.github.andriybosik.schemawizard.core.exception;

public class MigrationApplicationException extends RuntimeException {
    public MigrationApplicationException(String message) {
        super(message);
    }

    public MigrationApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
