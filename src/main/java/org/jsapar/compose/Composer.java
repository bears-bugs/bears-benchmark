package org.jsapar.compose;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.MulticastErrorEventListener;
import org.jsapar.model.Document;
import org.jsapar.model.Line;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Common interface for composer classes. A composer is able to take a {@link Document} or a sequence of {@link Line} and turn them into an output of
 * some kind. The output type depends on the implementation of this interface.
 *
 * @see org.jsapar.TextComposer
 * @see org.jsapar.compose.bean.BeanComposer
 */
public interface Composer extends AutoCloseable{

    /**
     * This method composes some output based on an entire {@link Document}.
     *
     * @param document The document to compose output from.
     * @throws java.io.UncheckedIOException When a low level IO error occurs.
     */
    default void compose(Document document){
        compose(document.iterator());
    }

    /**
     * Composes all lines returned by the iterator.
     *
     * @param lineIterator An iterator that iterates over a collection of lines. Can be used to build lines on-the-fly if you
     *                     don't want to store them all in memory.
     */
    default void compose(Iterator<Line> lineIterator) {
        while (lineIterator.hasNext())
            composeLine(lineIterator.next());
    }

    /**
     * Composes all lines returned by the supplied stream.
     * @param lineStream A stream of lines to compose
     */
    default void compose(Stream<Line> lineStream){
        lineStream.forEach(this::composeLine);
    }

    /**
     * Composes output based on supplied {@link Line}, including line separator if applicable.
     *
     * @param line The line to compose
     * @return True if the line was actually composed.
     * @throws java.io.UncheckedIOException When a low level IO error occurs.
     */
    boolean composeLine(Line line);

    /**
     * Sets an error event listener to this composer. If you want to add more than one error event listeners, use the {@link MulticastErrorEventListener}
     *
     * @param errorListener The error event listener to add.
     */
    void setErrorEventListener(ErrorEventListener errorListener);

    @Override
    default void close() throws IOException{
        // do nothing
    }
}
