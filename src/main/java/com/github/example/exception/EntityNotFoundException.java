package com.github.example.exception;


public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8686709079530195608L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
