/**
 * 
 */
package org.jsapar.error;

import java.util.EventListener;

/**
 * Interface for receiving event call-backs while parsing or composing.
 * 
 */
public interface ErrorEventListener extends EventListener {

    /**
     * <p>Called when there is an error while parsing input or composing output. If an implementation of this method throws
     * an unchecked exception, the parsing/composing will be aborted and the exception will be passed through to the
     * original calling method.
     * </p>
     * <p>May throw unchecked exceptions depending on implementation.</p>
     * 
     * @param event The event that contains the error information.
     */
    void errorEvent(ErrorEvent event);

}
