package com.github.example.exception;


public class EntityAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 7265331099054689065L;

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
