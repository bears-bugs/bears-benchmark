package com.github.example.exception;


public class CouldNotAcquireLockException extends RuntimeException {
    private static final long serialVersionUID = -6274696342788771752L;

    public CouldNotAcquireLockException(String message) {
        super(message);
    }

    public CouldNotAcquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
