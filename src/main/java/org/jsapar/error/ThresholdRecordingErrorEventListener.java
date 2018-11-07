package org.jsapar.error;

import java.util.List;

/**
 * This error event listener records errors until it reaches a maximum number. Any errors that occurs after maximum
 * number has been reached will cause a {@link MaxErrorsExceededException} instead.
 * Created by stejon0 on 2016-10-15.
 */
public class ThresholdRecordingErrorEventListener extends RecordingErrorEventListener {
    private int maxNumberOfErrors;

    /**
     * Creates an error event listener where the error list needs to be fetched with
     * {@link RecordingErrorEventListener#getErrors()} afterwards.
     *
     * @param maxNumberOfErrors The maximum number of errors allowed to be recorded
     */
    public ThresholdRecordingErrorEventListener(int maxNumberOfErrors) {
        this.maxNumberOfErrors = maxNumberOfErrors;
    }

    /**
     * Creates an error event listener that adds errors to the supplied list
     *
     * @param maxNumberOfErrors The maximum number of errors allowed to be recorded
     * @param errors            The list that errors will be added to.
     */
    public ThresholdRecordingErrorEventListener(int maxNumberOfErrors, List<JSaParException> errors) {
        super(errors);
        this.maxNumberOfErrors = maxNumberOfErrors;
    }

    /**
     * Called when there is an error while parsing input or composing output. This implementation saves the errors in
     * a member list until maximum number has been reached. The list is protected by a semaphore/synchronized block in
     * case more than one thread is writing
     * to the same list. The list itself is used as the semaphore.
     * Any errors that occurs after maximum
     * number has been reached will cause a {@link MaxErrorsExceededException} to be thrown.
     *
     * @param event The event that contains the error information.
     */
    @Override
    public void errorEvent(ErrorEvent event) {
        synchronized(this) {
            super.errorEvent(event);
            if (getErrors().size() > maxNumberOfErrors)
                throw new MaxErrorsExceededException(event.getError(), getErrors());
        }
    }
}
