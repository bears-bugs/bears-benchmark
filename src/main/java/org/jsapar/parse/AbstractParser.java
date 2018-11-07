package org.jsapar.parse;

import org.jsapar.error.ErrorEventListener;

import java.io.IOException;

/**
 * Abstract base class for all parsers.
 *
 * The default error handling is to throw an exception upon the first error that occurs. You can however change that
 * behavior by adding an {@link org.jsapar.error.ErrorEventListener}. There are several implementations to choose from such as
 * {@link org.jsapar.error.RecordingErrorEventListener} or
 * {@link org.jsapar.error.ThresholdRecordingErrorEventListener}, or you may implement your own..
 */
public class AbstractParser {

    ErrorEventListener errorEventListener;

    public void setErrorEventListener(ErrorEventListener errorEventListener) {
        this.errorEventListener = errorEventListener;
    }

    protected long execute(ParseTask parseTask, LineEventListener lineEventListener) throws IOException {
        parseTask.setLineEventListener(lineEventListener);
        if(errorEventListener != null)
            parseTask.setErrorEventListener(errorEventListener);
        return parseTask.execute();
    }
}
