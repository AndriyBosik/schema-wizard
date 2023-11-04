package com.example.dao.exception;

public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
