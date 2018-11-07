/**
 * 
 */
package org.jsapar.parse;

import java.io.IOException;
import java.util.EventListener;

/**
 * Interface for receiving event call-backs while parsing.
 */
public interface LineEventListener extends EventListener {

    /**
     * Called every time that a complete line was found in the input.
     * 
     * @param event
     *            The event that contains the parsed line.
     */
    void lineParsedEvent(LineParsedEvent event) ;


}
