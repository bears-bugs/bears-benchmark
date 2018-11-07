package org.jsapar.parse;

import org.jsapar.error.ErrorEvent;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.ExceptionErrorEventListener;

import java.io.IOException;

/**
 * Abstract implementation of {@link ParseTask} interface. Provides possibility to have line event listeners and
 * error event listeners. Override this class to implement a specific parser.
 */
public abstract class AbstractParseTask implements ParseTask, LineEventListener, ErrorEventListener {
    private LineEventListener  lineEventListener  = null;
    private ErrorEventListener errorEventListener = new ExceptionErrorEventListener();


    @Override
    public void setLineEventListener(LineEventListener eventListener) {
        this.lineEventListener = eventListener;
    }

    @Override
    public void setErrorEventListener(ErrorEventListener errorEventListener) {
        this.errorEventListener = errorEventListener;
    }

    @Override
    public void lineParsedEvent(LineParsedEvent event)  {
        if(lineEventListener != null)
            lineEventListener.lineParsedEvent(event);
    }

    @Override
    public void errorEvent(ErrorEvent event) {
        errorEventListener.errorEvent(event);
    }

}
