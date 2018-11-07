package org.jsapar;

import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.convert.Text2TextConverterMain;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Converts one text input to another text output. For instance converting from CSV to fixed with format.
 * See {@link AbstractConverter} for details about error handling and manipulating data.
 * @see org.jsapar.concurrent.ConcurrentText2TextConverter
 */
public class Text2TextConverter extends AbstractConverter {
    private final Schema parseSchema;
    private final Schema composeSchema;
    private TextParseConfig parseConfig;

    public Text2TextConverter(Schema parseSchema, Schema composeSchema) {
        this.parseSchema = parseSchema;
        this.composeSchema = composeSchema;
        parseConfig = new TextParseConfig();
    }

    public Text2TextConverter(Schema parseSchema, Schema composeSchema, TextParseConfig parseConfig) {
        this.parseSchema = parseSchema;
        this.composeSchema = composeSchema;
        this.parseConfig = parseConfig;
    }

    /**
     * Converts text read from the reader according to the parse schema and writes the output to the writer according
     * to the compose schema.
     * @param reader The reader to read input from
     * @param writer The writer to write converted result to.
     * @return Number of converted lines.
     * @throws IOException In case of IO error
     */
    public long convert(Reader reader, Writer writer) throws IOException {
        ConvertTask convertTask = new ConvertTask(makeParseTask(reader), makeComposer(writer));
        return execute(convertTask);
    }

    protected TextComposer makeComposer(Writer writer) {
        return new TextComposer(composeSchema, writer);
    }

    protected TextParseTask makeParseTask(Reader reader) {
        return new TextParseTask(parseSchema, reader, parseConfig);
    }

    public TextParseConfig getParseConfig() {
        return parseConfig;
    }

    public void setParseConfig(TextParseConfig parseConfig) {
        this.parseConfig = parseConfig;
    }

    public Schema getParseSchema() {
        return parseSchema;
    }

    public Schema getComposeSchema() {
        return composeSchema;
    }

    /**
     * Starts stand alone text to text converter.
     * @param args The main program arguments
     */
    public static void main(String[] args) {
        Text2TextConverterMain main = new Text2TextConverterMain();
        main.run(args);
    }

}
