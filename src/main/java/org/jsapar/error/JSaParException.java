package org.jsapar.error;

/**
 * Base class for all types of errors that can be added to an {@link ErrorEvent}
 */
public class JSaParException extends RuntimeException{

    /**
     * Creates a JSaParException
     * @param message A description of the error that occurred.
     * @param cause A nesting exception that caused the error.
     */
    public JSaParException(String message, Throwable cause) {
        super(makeSuperMessage(message, cause), cause);
    }

    /**
     * Creates a JSaParException
     * @param cause A nesting exception that caused the error.
     */
    public JSaParException(Throwable cause) {
        super(makeSuperMessage(null, cause), cause);
    }

    private static String makeSuperMessage(String message, Throwable cause) {
        if(message == null){
            if(cause == null)
                return null;
            return cause.getMessage();
        }
        if(cause==null || cause.getMessage()==null || message.endsWith(cause.getMessage()))
            return message;
        return message + " - " + cause.getMessage();
    }

    /**
     * Creates a JSaParException
     * @param message A description of the error that occurred.
     */
    public JSaParException(String message) {
        super(message);
    }

}
