package org.jsapar.compose.string;

/**
 * Interface for receiving event call-backs while composing.
 *
 */
public interface StringComposedEventListener {

    /**
     * Called when a line of string has been composed.
     * @param event The event containing the composed strings.
     */
    void stringComposedEvent(StringComposedEvent event) ;

}
