package org.jsapar.schema;

import org.jsapar.model.Document;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextSchemaParser;

import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Abstract base class for all type of jsapar schemata. A schema describes how the buffer should be
 * parsed or how the lines of a {@link Document} should be written. Usually the parse and output methods are
 * called from one of the in, out or io classes.
 * 
 * @see Xml2SchemaBuilder
 * @see Schema2XmlExtractor
 * 
 */
public abstract class Schema implements Cloneable{


    private Locale locale = SchemaCellFormat.defaultLocale;
    private String lineSeparator = System.getProperty("line.separator");

    /**
     * @return True if this schema does not contain any lines. False otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * Line separator string. Default value is the system default (Retrieved by
     * {@code System.getProperty("line.separator")}).
     * @return the lineSeparator
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * Sets the line separator string. Default value is the system default (Retrieved by
     * {@code System.getProperty("line.separator")}).
     * 
     * @param lineSeparator
     *            the lineSeparator to set.
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * The locale of the schema is used to determine the formatting of for example numbers, decimal separators etc. It
     * does not affect the error messages. Default is en_US.
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * The locale of the schema is used to determine the formatting of for example numbers, decimal separators etc. It
     * does not affect the error messages. Default is en_US.
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public Schema clone(){
        try {
            return (Schema) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @return list of all schema lines.
     */
    public abstract Collection<? extends SchemaLine> getSchemaLines();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return " lineSeparator=" +
                this.lineSeparator +
                " locale=" +
                this.locale;
    }


    /**
     * @param lineType The line type to get {@link SchemaLine} for.
     * @return The {@link SchemaLine} with the supplied line type name. Optional.empty if no such schema line was
     *         found.
     */
    public abstract Optional<? extends SchemaLine> getSchemaLine(String lineType);


    /**
     * @return Number of schema lines of this schema.
     */
    public abstract int size();


    /**
     * Loads a schema instance from an xml that is read from the supplied reader. The xml needs to comply to the
     * JSaParSchema.xsd otherwise a {@link SchemaException} is thrown.
     *
     * @param reader
     *            The reader to read the xml from. Caller is responsible for closing the reader.
     * @return A new schema instance created based on the xml.
     * @throws SchemaException
     *             if the supplied xml does not comply to the JSaParSchema.xsd or if there is any other error while
     *             loading a schema.
     * @throws UncheckedIOException In case there was an error reading from the input.
     */
    public static Schema ofXml(Reader reader)
            throws SchemaException, UncheckedIOException {
        Xml2SchemaBuilder schemaBuilder = new Xml2SchemaBuilder();
        return schemaBuilder.build(reader);
    }

    /**
     * Exports this schema instance as xml that complies to the JSaParSchema.xsd.
     *
     * @param writer
     *            The writer where the xml is written. Caller is responsible for closing the writer.
     * @throws SchemaException
     *             if there is an error while writing the xml.
     */
    public void toXml(Writer writer) throws SchemaException {
        Schema2XmlExtractor extractor = new Schema2XmlExtractor();
        extractor.extractXml(writer, this);
    }

    /**
     * @return A stream of all schema lines of this schema.
     */
    public abstract Stream<? extends SchemaLine> stream();

    /**
     * @return An iterator of all schema lines of this schema.
     */
    public abstract Iterator<? extends SchemaLine> iterator();
    /**
     * Internal method to create a schema parser using this schema.
     * @param reader The reader to use for the parser.
     * @param parseConfig Current parse configuration.
     * @return Create a schema based text parser.
     */
    public abstract TextSchemaParser makeSchemaParser(Reader reader, TextParseConfig parseConfig);
}
