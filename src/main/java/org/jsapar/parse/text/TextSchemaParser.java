package org.jsapar.parse.text;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.parse.LineEventListener;

import java.io.IOException;

/**
 * Internal interface for text parser that can parse text based on a schema.
 */
public interface TextSchemaParser {

    /**
     * This method should only be called by a TextParseTask class. Don't use this
     * directly in your code. Use a TextParseTask instead.
     * <p>
     * Sends line parse events to the supplied lineEventListener while parsing.
     *
     * @param listener      The {@link LineEventListener} which will receive events for each parsed line.
     * @param errorListener The {@link ErrorEventListener} that will receive events for each error.
     * @return Number of lines parsed
     * @throws IOException If there is an error reading from the input reader.
     */
    long parse(LineEventListener listener, ErrorEventListener errorListener) throws IOException;

}
