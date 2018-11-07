package org.jsapar.parse;

import java.util.LinkedList;
import java.util.List;

/**
 * Distributes all line events to multiple {@link LineEventListener} instances. All registered listeners
 * are called one by one from the same thread.
 */
public class MulticastLineEventListener implements LineEventListener{

    private List<LineEventListener> lineEventListeners = new LinkedList<>();


    public void addLineEventListener(LineEventListener eventListener) {
        if (eventListener == null)
            return;
        this.lineEventListeners.add(eventListener);
    }

    public void removeLineEventListener(LineEventListener lineEventListener){
        this.lineEventListeners.remove(lineEventListener);
    }

    /**
     * Will call each registered line event listener one by one in order of registration.
     * @param event The event to distribute.
     */
    @Override
    public void lineParsedEvent(LineParsedEvent event)  {
        this.lineEventListeners.forEach(l->l.lineParsedEvent(event));
    }

    /**
     * @return Number of registered listeners
     */
    public int size(){
        return lineEventListeners.size();
    }

    /**
     * @return true if no event listeners are registered, false if there is at least one listener registered.
     */
    public boolean isEmpty(){
        return lineEventListeners.isEmpty();
    }

}
