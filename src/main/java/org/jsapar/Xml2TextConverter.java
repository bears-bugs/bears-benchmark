package org.jsapar;

import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.parse.xml.XmlParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Converts an xml input to a text output. For instance converting from xml to a CSV format.
 * See {@link AbstractConverter} for details about error handling and manipulating data.
 * @see org.jsapar.parse.xml.XmlParser
 */
public class Xml2TextConverter extends AbstractConverter {
    private final Schema composeSchema;

    public Xml2TextConverter(Schema composeSchema) {
        this.composeSchema = composeSchema;
    }

    /**
     * Starts the converting reading from supplied reader, writing to supplied writer.
     * @param reader The reader to read from.
     * @param writer The writer to write to
     * @throws IOException if there is an IO error.
     */
    public void convert(Reader reader, Writer writer) throws IOException {
        XmlParseTask parseTask = new XmlParseTask(reader);
        ConvertTask convertTask = new ConvertTask(parseTask, new TextComposer(composeSchema, writer));
        execute(convertTask);
    }

}
