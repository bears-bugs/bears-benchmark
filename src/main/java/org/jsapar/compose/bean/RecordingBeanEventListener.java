package org.jsapar.compose.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Saves all beans that was composed into a list that can be retrieved by calling {@link #getBeans()} when done
 * composing. The Generic type T should be set to a common base class of all the expected beans. Use {@link Object} as
 * base class if there is no common base class for all beans. A {@link ClassCastException} will be thrown if the bean
 * created could not be converted into the class defined by T.
 * Created by stejon0 on 2016-10-15.
 */
public class RecordingBeanEventListener<T> implements BeanEventListener<T>, Iterable<T> {
    private List<T> beans = new ArrayList<>();

    /**
     * @return All composed beans.
     */
    public List<T> getBeans() {
        return beans;
    }

    /**
     * Called every time that a bean, on root level, is successfully composed. Child beans do not generate events.
     * This implementation saves the composed bean into an internal list to be retrieved later by calling
     * {@link #getBeans()}
     * @param event The event that contains the composed bean.
     */
    @Override
    public void beanComposedEvent(BeanEvent<T> event) {
        beans.add(event.getBean());
    }

    /**
     * Clears all recorded beans.
     */
    public void clear(){
        beans.clear();
    }

    /**
     * @return Number of recorded beans.
     */
    public int size(){
        return beans.size();
    }

    @Override
    public Iterator<T> iterator() {
        return beans.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        beans.forEach(consumer);
    }

    public Stream<T> stream(){
        return beans.stream();
    }
}
