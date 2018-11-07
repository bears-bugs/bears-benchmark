package org.jsapar.parse.line;

import org.jsapar.error.ErrorEvent;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.ValidationAction;
import org.jsapar.parse.LineParseException;

/**
 * Internal utility class for handling validation error.
 */
public class ValidationHandler {

    public ValidationHandler() {
    }

    /**
     * Handle line validation.
     *
     * @param source        The class where the error
     * @param lineNumber    The line number if present, 0 otherwise.
     * @param message       A message to provide as error message in case it is considered an error.
     * @param action        How to tread this validation.
     *                      ERROR will generate an error event but return true.
     *                      EXCEPTION will throw a LineParseException immediately.
     *                      NONE will simply ignore the validation and continue without action.
     *                      OMIT_LINE will omit the current line from the parsing context.
     * @param eventListener Destination error event listener in case error event is the action taken by the method.
     * @return True if the line should be included in the current context. False if the line should be ignored.
     */
    public boolean lineValidation(Object source,
                                  long lineNumber,
                                  String message,
                                  ValidationAction action,
                                  ErrorEventListener eventListener) {
        switch (action) {
        case ERROR: {
            LineParseException error = new LineParseException(lineNumber, message);
            ErrorEvent event = new ErrorEvent(source, error);
            eventListener.errorEvent(event);
            return true;
        }
        case EXCEPTION: {
            throw new LineParseException(lineNumber, message);
        }
        case NONE:
            return true;
        case OMIT_LINE:
            return false;
        }
        return false;
    }
}
