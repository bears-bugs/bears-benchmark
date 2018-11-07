package org.jsapar;

import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.parse.bean.BeanMap;
import org.jsapar.parse.bean.BeanParseTask;
import org.jsapar.schema.Schema;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Converts from a collection or a stream of beans to text output. Pulls beans from the provided source and converts to
 * text. See {@link Bean2TextConverter} for an implementation where you can push bean instances one by one instead.
 * <p>
 * The Generic type T should be set to a common base class of all the expected beans. Use Object as
 * base class if there is no common base class for all beans.
 * <p>
 * The difference compared to {@link Bean2TextConverter} is that instances of this class can be used multiple times for
 * different writers by calling one of the convert methods where as {@link Bean2TextConverter} only can be used once and
 * the writer needs to be supplied in the constructor.
 * <p>
 * ExampleUsage:
 * <pre>{@code
 * BeanCollection2TextConverter<TstPerson> converter = new BeanCollection2TextConverter<>(schema);
 * converter.convert(people, writer);
 * }</pre>
 *
 * @see Bean2TextConverter
 */
public class BeanCollection2TextConverter<T> extends AbstractConverter {

    private final Schema  composerSchema;
    private       BeanMap beanMap;

    /**
     * Creates a converter with supplied composer schema.
     *
     * @param composerSchema The schema to use while composing text output.
     * @throws IntrospectionException If string names of properties could not be mapped to actual properties.
     * @throws ClassNotFoundException In case any of the classes described in the schema does not exist in the classpath.
     */
    public BeanCollection2TextConverter(Schema composerSchema) throws IntrospectionException, ClassNotFoundException {
        this(composerSchema, BeanMap.ofSchema(composerSchema));
    }

    /**
     * Creates a converter with supplied composer schema.
     *
     * @param composerSchema The schema to use while composing text output.
     * @param beanMap        The bean map to use to map schema names to bean properties.
     */
    public BeanCollection2TextConverter(Schema composerSchema, BeanMap beanMap) {
        assert composerSchema != null;
        this.composerSchema = composerSchema;
        this.beanMap = beanMap;
    }

    /**
     * Converts objects referenced by supplied stream into a text output written to supplied writer.
     *
     * @param stream The stream to get beans from.
     * @param writer The text writer to write text output to. Caller is responsible for closing the writer.
     * @throws IOException If there is an error writing text output.
     */
    public void convert(Stream<? extends T> stream, Writer writer) throws IOException {
        execute(new ConvertTask(makeParseTask(stream), makeComposer(writer)));
    }

    /**
     * Converts objects referenced by supplied iterator into a text output written to supplied writer.
     *
     * @param iterator The iterator to get beans from.
     * @param writer   The text writer to write text output to. Caller is responsible for closing the writer.
     * @throws IOException If there is an error writing text output.
     */
    public void convert(Iterator<? extends T> iterator, Writer writer) throws IOException {
        ConvertTask convertTask = new ConvertTask(makeParseTask(iterator), makeComposer(writer));
        execute(convertTask);
    }

    protected TextComposer makeComposer(Writer writer) {
        return new TextComposer(this.composerSchema, writer);
    }

    protected BeanParseTask<T> makeParseTask(Stream<? extends T> stream) {
        return new BeanParseTask<>(stream, beanMap);
    }

    protected BeanParseTask<T> makeParseTask(Iterator<? extends T> iterator) {
        return new BeanParseTask<>(iterator, beanMap);
    }

    /**
     * Converts objects of supplied collection into a text output written to supplied writer.
     *
     * @param collection The collection of beans to convert
     * @param writer     The text writer to write text output to.
     * @throws IOException If there is an error writing text output.
     */
    public void convert(Collection<? extends T> collection, Writer writer) throws IOException {
        convert(collection.stream(), writer);
    }

}
