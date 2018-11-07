package org.jsapar.concurrent;

import org.jsapar.BeanCollection2TextConverter;
import org.jsapar.convert.AbstractConverter;
import org.jsapar.parse.bean.BeanMap;
import org.jsapar.schema.Schema;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A multi threaded version of {@link BeanCollection2TextConverter} where the composer is started in a separate worker
 * thread.
 * See {@link AbstractConverter} for details about error handling and manipulating data.
 * <p>
 * As a rule of thumb while working with normal files on disc, don't use this concurrent version unless your input
 * normally exceeds at least 1MB of data, as the overhead of starting
 * a new thread and synchronizing threads are otherwise greater than the gain by the concurrency.
 * @param <T> The base class for the beans to convert.
 */
public class ConcurrentBeanCollection2TextConverter<T> extends BeanCollection2TextConverter<T> implements ConcurrentStartStop{
    private ConcurrentConvertTaskFactory convertTaskFactory = new ConcurrentConvertTaskFactory();

    public ConcurrentBeanCollection2TextConverter(Schema composerSchema) throws IntrospectionException, ClassNotFoundException {
        super(composerSchema);
    }

    public ConcurrentBeanCollection2TextConverter(Schema composerSchema, BeanMap beanMap) {
        super(composerSchema, beanMap);
    }

    @Override
    public void convert(Stream<? extends T> stream, Writer writer) throws IOException {
        execute(convertTaskFactory.makeConvertTask(makeParseTask(stream), makeComposer(writer)));
    }

    @Override
    public void convert(Iterator<? extends T> iterator, Writer writer) throws IOException {
        execute(convertTaskFactory.makeConvertTask(makeParseTask(iterator), makeComposer(writer)));
    }

    public void registerOnStart(Runnable onStart) {
        this.convertTaskFactory.registerOnStart(onStart);
    }

    public void registerOnStop(Runnable onStop) {
        this.convertTaskFactory.registerOnStop(onStop);
    }
}
