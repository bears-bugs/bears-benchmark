package org.jsapar.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributes all error events to multiple {@link ErrorEventListener} instances. All registered listeners
 * are called one by one from the same thread.
 * If no {@link ErrorEventListener} is registered, this implementation will throw an exception upon the first error.
 */
public class MulticastErrorEventListener implements ErrorEventListener{

    private List<ErrorEventListener> eventListeners = new ArrayList<>();
    private ErrorEventListener defaultEventListener = new ExceptionErrorEventListener();

    /**
     * Adds an {@link ErrorEventListener}
     * @param errorEventListener The {@link ErrorEventListener} to add.
     */
    public void addEventListener(ErrorEventListener errorEventListener){
        if (errorEventListener == null)
            return;
        eventListeners.add(errorEventListener);
    }

    /**
     * Removes an {@link ErrorEventListener}
     * @param errorEventListener The {@link ErrorEventListener} to remove
     */
    public void removeEventListener(ErrorEventListener errorEventListener){
        eventListeners.remove(errorEventListener);
    }

    /**
     * Called when an error occurs. Will forward the event to all registered event listeners.
     * If no {@link ErrorEventListener} is registered, this implementation will throw an exception upon the first error.
     * @param event The event that contains the error information.
     */
    @Override
    public void errorEvent(ErrorEvent event) {
        if(eventListeners.isEmpty())
            defaultEventListener.errorEvent(event);
        else
            eventListeners.forEach(l->l.errorEvent(event));
    }

    /**
     * @return Number of event listeners that has been registered
     */
    public int size(){
        return eventListeners.size();
    }
}
