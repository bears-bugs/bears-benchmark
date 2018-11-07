package org.jsapar.parse.bean;

import org.jsapar.BeanCollection2TextConverter;
import org.jsapar.model.Cell;
import org.jsapar.model.CellType;
import org.jsapar.model.Line;
import org.jsapar.parse.AbstractParseTask;
import org.jsapar.parse.LineParsedEvent;
import org.jsapar.parse.ParseTask;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Uses a collection of java bean objects to build {@link LineParsedEvent}. The {@link Line#lineType} of each line will be
 * the name of the class denoted by {@link Class#getName()}. Each bean property that have a getter method will result in
 * a cell with the bean property name The {@link Cell#name} of each cell will be the name of the bean property, e.g. if
 * the bean has a method declared as {@code public int getNumber()}, it will result in a cell with the name "number" of
 * type {@link CellType}.INTEGER.
 * <p>
 * If you use these rules you can write a {@link org.jsapar.schema.Schema} that converts a bean to a different type of output.
 *
 * @see BeanCollection2TextConverter
 * @see org.jsapar.Bean2TextConverter
 */
public class BeanParseTask<T> extends AbstractParseTask implements ParseTask {

    private final BeanMarshaller<T>   beanMarshaller;
    private       Stream<? extends T> stream;

    public BeanParseTask(Stream<? extends T> stream, BeanMap beanMap) {
        this.stream = stream;
        this.beanMarshaller = new BeanMarshaller<>(beanMap);
    }

    public BeanParseTask(Iterator<? extends T> iterator, BeanMap beanMap) {
        this.stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
        this.beanMarshaller = new BeanMarshaller<>(beanMap);
    }


    /**
     * Starts parsing of an iterated series of beans. The result will be line parsed events where each
     * line hav
     */
    @Override
    public long execute() {
        AtomicLong count = new AtomicLong(1);
        stream.forEach(bean ->
                beanMarshaller.marshal(bean, this, count.incrementAndGet()).ifPresent(line ->
                        lineParsedEvent(new LineParsedEvent(
                                this,
                                line))));
        return count.get();
    }

 }
