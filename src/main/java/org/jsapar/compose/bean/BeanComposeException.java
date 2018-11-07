package org.jsapar.compose.bean;

public class BeanComposeException extends Exception {

    public BeanComposeException(String message) {
        super(message);
    }

    @SuppressWarnings("WeakerAccess")
    public BeanComposeException(String message, Throwable cause) {
        super(message, cause);
    }

}
