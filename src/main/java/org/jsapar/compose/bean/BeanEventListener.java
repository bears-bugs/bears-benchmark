package org.jsapar.compose.bean;

import java.util.EventListener;

/**
 * Interface for receiving event call-backs while composing.
 * @param <T> common base class of all the expected beans. Use Object as base class if there is no common base class for all beans.
 * @author stejon0
 * 
 */
public interface BeanEventListener<T> extends EventListener {

    /**
     * Called every time that a bean, on root level, is successfully composed. Child beans do not generate events.
     * 
     * @param event
     *            The event that contains the composed bean.
     *
     */
    void beanComposedEvent(BeanEvent<T> event) ;


}
