package org.jsapar.compose;

import org.jsapar.error.JSaParException;
import org.jsapar.model.Line;

/**
 * Error that can happen when composing.
 */
public class ComposeException extends JSaParException {

    /**
     * A clone of the line that caused the error.
     */
    private final Line line;

    /**
     * Creates a new ComposeException
     * @param message Error message.
     * @param line The line where the error occured.
     * @param exception An exception that was caught and is hereby forwarded as en error instead.
     */
    public ComposeException(String message, Line line, Throwable exception) {
        super(message, exception);
        this.line = line.clone();
    }

    /**
     * Creates a new ComposeException
     * @param message Error message.
     * @param line The line where the error occured.
     */
    public ComposeException(String message, Line line) {
        super(message);
        this.line = line.clone();
    }

    /**
     * Creates a new ComposeException
     * @param message Error message.
     * @param exception An exception that was caught and is hereby forwarded as en error instead.
     */
    public ComposeException(String message, Throwable exception) {
        super(message, exception);
        this.line = null;
    }

    /**
     * Creates a new ComposeException
     * @param errorDescription Error message.
     */
    public ComposeException(String errorDescription) {
        super(errorDescription);
        this.line = null;
    }

    /**
     * @return A simple message describing the error and it's location.
     */
    @Override
    public String getMessage() {
        if(line != null) {
            return super.getMessage() + " at line " +
                    this.line;
        }
        else
            return super.getMessage();
    }
    /**
     * @return The line where the error occurred or null if no line reference could be given.
     */
    public Line getLine() {
        return line;
    }
}
