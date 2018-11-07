package org.jsapar.parse;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.MulticastErrorEventListener;
import org.jsapar.parse.bean.BeanParseTask;
import org.jsapar.parse.text.TextParseTask;

import java.io.IOException;

/**
 * Common interface for all parse jobs. The interface does not state anything about the origin of the parsed items.
 *
 * An instance of a parser is only useful once. You create an instance, initializes it
 * with the event listeners needed, then call {@link #execute()}.
 *
 * @see TextParseTask
 * @see BeanParseTask
 */
public interface ParseTask extends AutoCloseable{

    /**
     * Sets a line event listener to this parser. If you want more than one line event listener registered, use a {@link MulticastLineEventListener}.
     * @param eventListener The line event listener.
     */
    void setLineEventListener(LineEventListener eventListener);

    /**
     * Sets an error event listener to this parser. Default behavior otherwise is to throw an exception upon the first
     * error. If you want more than one listener to get each error event, use a {@link MulticastErrorEventListener}.
     * @param errorEventListener The error event listener.
     */
    void setErrorEventListener(ErrorEventListener errorEventListener);

    /**
     * Start the parsing. Should only be called once for each {@link ParseTask}. Consecutive calls may have unexpected behavior.
     * @throws IOException In case there is an error reading the input.
     * @return Number of line events that were generated, i.e. the number of lines parsed.
     */
    long execute() throws IOException;

    /**
     * Closes attached resources.
     * @throws IOException In case of error closing io resources.
     */
    @Override
    default void close() throws IOException{
        // Do nothing
    }
}
