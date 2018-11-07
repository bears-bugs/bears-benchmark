package org.jsapar;

import org.jsapar.convert.LineManipulator;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.error.ExceptionErrorEventListener;
import org.jsapar.parse.bean.BeanMap;
import org.jsapar.parse.bean.BeanMarshaller;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Converts from beans to text output. This implementation accepts beans pushed one by one to be converted. See
 * {@link BeanCollection2TextConverter} for an implementation where you provide a stream or a collection of beans from which
 * beans are pulled. This means that this class acts more like a Composer compared to other Converter in the sense that you need to provide the writer
 * in the constructor and each call to {@link #convert(Object)} method is done without supplying any writer.
 * <p>
 * The Generic type T should be set to a common base class of all the expected beans. Use Object as
 * base class if there is no common base class for all beans.
 * <p>
 * An instance of this class can only be used once for one writer, then it needs to be disposed. Instances of {@link BeanCollection2TextConverter} on
 * the other hand can be used multiple times for multiple writers.
 * <p>
 * ExampleUsage:
 * <pre>{@code
 * Bean2TextConverter<TstPerson> converter = new Bean2TextConverter<>(schema, writer);
 * converter.convert(person1);
 * converter.convert(person2);
 * }</pre>
 * <p>
 * The default error behavior is to throw an exception upon the first error that occurs. You can however change that
 * behavior by adding an {@link org.jsapar.error.ErrorEventListener}. There are several implementations to choose from such as
 * {@link org.jsapar.error.RecordingErrorEventListener} or
 * {@link org.jsapar.error.ThresholdRecordingErrorEventListener}, or you may implement your own.
 *
 * @see BeanCollection2TextConverter
 */
public class Bean2TextConverter<T> implements AutoCloseable{

    private final BeanMarshaller<T>     beanMarshaller;
    private final TextComposer          textComposer;
    private       long                  lineNumber         = 1;
    private       List<LineManipulator> manipulators       = new java.util.LinkedList<>();
    private       ErrorEventListener    errorEventListener = new ExceptionErrorEventListener();

    /**
     * Creates a converter with supplied composer schema.
     *
     * @param composerSchema The schema to use while composing text output.
     * @param writer         The writer to write text output to. Caller is responsible for either closing the writer or call the close method of the created instance.
     */
    public Bean2TextConverter(Schema composerSchema, Writer writer){
        this(composerSchema, BeanMap.ofSchema(composerSchema), writer);
    }

    /**
     * Creates a converter with supplied composer schema.
     *
     * @param composerSchema The schema to use while composing text output.
     * @param beanMap        The bean map to use to map schema names to bean properties. This {@link BeanMap} instance will be used as is so it needs to contain
     *                       mapping for all values that should be converted to text. If you want to use a {@link BeanMap} that is created
     *                       from a combination of the schema and an additional override {@link BeanMap} you can use the method {@link BeanMap#ofSchema(Schema, BeanMap)} to create such combined instance.
     * @param writer         The writer to write text output to. Caller is responsible for either closing the writer or call the close method of the created instance.
     */
    @SuppressWarnings("WeakerAccess")
    public Bean2TextConverter(Schema composerSchema, BeanMap beanMap, Writer writer) {
        assert composerSchema != null;
        beanMarshaller = new BeanMarshaller<>(beanMap);
        textComposer = new TextComposer(composerSchema, writer);
    }

    /**
     * Converts supplied bean into a text output. To be called once for each bean that should be written to the output.
     *
     * @param bean The bean to convert
     */
    public void convert(T bean) {
        beanMarshaller.marshal(bean, errorEventListener, lineNumber++).ifPresent(line -> {
            for (LineManipulator manipulator : manipulators) {
                if (!manipulator.manipulate(line))
                    return;
            }
            textComposer.composeLine(line);
        });
    }

    public void setErrorEventListener(ErrorEventListener errorEventListener) {
        this.errorEventListener = errorEventListener;
    }

    /**
     * Adds LineManipulator to this converter. All present line manipulators are executed for each
     * line in the same order that they were added.
     *
     * @param manipulator The line manipulator to add.
     */
    public void addLineManipulator(LineManipulator manipulator) {
        manipulators.add(manipulator);
    }

    /**
     * Closes the attached writer.
     * @throws IOException In case of failing to close
     */
    @Override
    public void close() throws IOException {
        this.textComposer.close();
    }
}
