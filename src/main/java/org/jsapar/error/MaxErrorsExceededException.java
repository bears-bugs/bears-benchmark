/**
 * 
 */
package org.jsapar.error;

import java.util.List;

/**
 * Used by {@link ThresholdRecordingErrorEventListener} and is thrown when the maximum number of errors has occured.
 * Please note that an instance of this class contains a list of all errors that has occurred until the threshold was
 * reached so if your memory is limited, don't use too large threshold value because the list can become too large.
 */
public class MaxErrorsExceededException extends JSaParException {

    /**
     * 
     */
    private static final long serialVersionUID = -8025034269584118995L;
    private final List<JSaParException> errors;

    /**
     * Creates an exception.
     * @param cause the error that caused the threshold to be exceeded
     * @param allErrors All errors that has occured before the limit was reached.
     */
    public MaxErrorsExceededException(JSaParException cause, List<JSaParException> allErrors) {
        super("Maximum number of errors exceeded.", cause);
        this.errors = allErrors;
    }


    /**
     * @return All the errors that has occurred until the threshold was reached.
     */
    public List<JSaParException> getErrors() {
        return errors;
    }
    
}
