package org.jsapar.error;

/**
 * Used when creating or assigning beans. Usually a result of an underlying cause.
 */
public class BeanException extends JSaParException {
    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanException(Throwable cause) {
        super(cause);
    }

    public BeanException(String message) {
        super(message);
    }
}
