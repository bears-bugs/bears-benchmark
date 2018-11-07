package org.jsapar.parse.text;

import org.jsapar.parse.AbstractParseTask;
import org.jsapar.parse.LineEventListener;
import org.jsapar.parse.ParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Reader;

/**
 * This class is used for a one-off parsing of a text source. You create an instance of this class, calls execute, then dispose it.. <br>
 * The instance of this class will produce events for each line that has been successfully parsed.
 * <p>
 * If you want to get the result back as a complete Document object, you should use the {@link org.jsapar.parse.DocumentBuilderLineEventListener}.
 * <ol>
 * <li>First, create an instance of TextParseTask.</li>
 * <li>Set event listeners for parse events and error events</li>
 * <li>Call the {@link #execute()} method. You will receive a callback event for each line that is parsed.</li>
 * </ol>
 *
 * @see org.jsapar.TextParser
 * @see ParseTask
 */
public class TextParseTask extends AbstractParseTask implements ParseTask, AutoCloseable {

    private final Schema          schema;
    private final Reader          reader;
    private final TextParseConfig parseConfig;

    public TextParseTask(Schema schema, Reader reader) {
        this(schema, reader, new TextParseConfig());
    }

    public TextParseTask(Schema schema, Reader reader, TextParseConfig parseConfig) {
        this.schema = schema;
        this.reader = reader;
        this.parseConfig = parseConfig;
    }

    /**
     * Reads characters from the input and parses them into a Line. Once a Line is completed, a
     * LineParsedEvent is generated to all registered event listeners. If there is an error while
     * parsing a line, a CellErrorEvent or ErrorEvent is generated to all registered error event listeners <br>
     * Before calling this method you have to call {@link #setLineEventListener(LineEventListener)} to be able to handle the
     * result
     *
     * @throws IOException If there is an error reading the input
     */
    @Override
    public long execute() throws IOException {
        return schema.makeSchemaParser(reader, parseConfig).parse(this, this);
    }

    /**
     * Closes the attached reader
     * @throws IOException In case of error while closing.
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
