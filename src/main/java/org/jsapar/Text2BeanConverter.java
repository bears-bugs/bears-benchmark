package org.jsapar;

import org.jsapar.compose.bean.*;
import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.error.BeanException;
import org.jsapar.parse.bean.BeanMap;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Reader;

/**
 * Converts text input to Java bean objects. You can choose to use the standard behavior or you may customize assigning
 * of bean properties. The default behavior is to use the schema names where the line type name needs to match the class
 * name of the class to create and the cell names needs to match bean property names. Any sub-objects needs to be of a
 * class that provides a default constructor. By supplying a {@link BeanMap} instance to the constructor you can map
 * different line and cell names to property names. {@link BeanMap} instance can be created form an xml input by using
 * the {@link BeanMap#ofXml(Reader)} method.
 *
 * See {@link AbstractConverter} for details about error handling and manipulating data.
 * @see AbstractConverter
 */
public class Text2BeanConverter<T> extends AbstractConverter {

    private final Schema         parseSchema;
    private BeanFactory<T> beanFactory;
    private BeanComposeConfig composeConfig = new BeanComposeConfig();
    private TextParseConfig   parseConfig   = new TextParseConfig();

    /**
     * Creates a converter with supplied composer schema.
     * @param parseSchema The schema to use while reading the text input.
     * @param beanMap     The bean map to use to map schema names to bean properties. By supplying a {@link BeanMap} instance to the constructor you can map
     * different line and cell names to property names. {@link BeanMap} instance can be created form an xml input by using
     * the {@link BeanMap#ofXml(Reader)} method. This {@link BeanMap} instance will be used as is so it needs to contain
     * mapping for all values that should be assigned to the bean instances. If you want to use a {@link BeanMap} that is created
     *                    from a combination of the schema and an additional override {@link BeanMap} you can use the method {@link BeanMap#ofSchema(Schema, BeanMap)} to create such combined instance.
     */
    public Text2BeanConverter(Schema parseSchema, BeanMap beanMap) {
        this.parseSchema = parseSchema;
        this.beanFactory = new BeanFactoryByMap<>(beanMap);
    }

    /**
     * The default behavior is to use the schema names where the line type name needs to match the class
     * name of the class to create and the cell names needs to match bean property names.
     * @param parseSchema The schema to use while reading the text input.
     * @throws BeanException In case of error when instantiating bean.
     */
    public Text2BeanConverter(Schema parseSchema) throws BeanException {
        this(parseSchema, BeanMap.ofSchema(parseSchema));
    }

    /**
     * Assigns a different bean factory. The bean factory is responsible for creating beans and assigning bean values. You may implement your own or use any of the existing:
     * <ul>
     *     <li>{@link BeanFactoryByMap} - Default for this class. Uses input schema with optional {@link BeanMap} to map properties.</li>
     *     <li>{@link BeanFactoryDefault} - Default if no schema is present. Uses cell names to guess the property names.</li>
     * </ul>
     * @param beanFactory The bean factory to use.
     */
    public void setBeanFactory(BeanFactory<T> beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Executes the actual convert. Will produce a callback to supplied eventListener for each bean that is parsed.
     * @param reader The reader to read the text from.
     * @param eventListener The callback interface which will receive a callback for each bean that is successfully parsed.
     * @return Number of converted beans.
     * @throws IOException In case of io error.
     */
    public long convert(Reader reader, BeanEventListener<T> eventListener) throws IOException {
        BeanComposer<T> composer = new BeanComposer<>(composeConfig, beanFactory);
        ConvertTask convertTask = new ConvertTask(new TextParseTask(this.parseSchema, reader, parseConfig), composer);
        composer.setComposedEventListener(eventListener);
        if (beanFactory != null)
            composer.setBeanFactory(beanFactory);
        return execute(convertTask);
    }

    public void setComposeConfig(BeanComposeConfig composeConfig) {
        this.composeConfig = composeConfig;
    }

    public BeanComposeConfig getComposeConfig() {
        return composeConfig;
    }

    public TextParseConfig getParseConfig() {
        return parseConfig;
    }

    public void setParseConfig(TextParseConfig parseConfig) {
        this.parseConfig = parseConfig;
    }
}
