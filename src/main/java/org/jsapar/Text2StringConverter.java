package org.jsapar;

import org.jsapar.compose.Composer;
import org.jsapar.compose.string.StringComposedEvent;
import org.jsapar.compose.string.StringComposedEventListener;
import org.jsapar.compose.string.StringComposer;
import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.parse.text.TextParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.io.Reader;

/**
 * Converts a text input to  {@link StringComposedEvent} for each line that is parsed.
 * <p>
 * The {@link StringComposedEvent} provides a
 * {@link java.util.stream.Stream} of {@link java.lang.String} for the current {@link org.jsapar.model.Line} where each
 * string is matches the cell in a schema. Each cell is formatted according to provided
 * {@link org.jsapar.schema.Schema}.
 * <p>
 * The schema can be of either CSV or FixedWith, the only thing that is significant is the order of the cells and the
 * cell formatting.
 * <p>
 * See {@link AbstractConverter} for details about error handling and manipulating data.
 */
public class Text2StringConverter extends AbstractConverter {
    private final Schema parseSchema;
    private final Schema composeSchema;
    private TextParseConfig parseConfig = new TextParseConfig();

    public Text2StringConverter(Schema parseSchema, Schema composeSchema) {
        this.parseSchema = parseSchema;
        this.composeSchema = composeSchema;
    }

    public Text2StringConverter(Schema parseSchema, Schema composeSchema, TextParseConfig parseConfig) {
        this.parseSchema = parseSchema;
        this.composeSchema = composeSchema;
        this.parseConfig = parseConfig;
    }

    /**
     * @param reader                The reader to read input from
     * @param composedEventListener The string composed event listener that get notification of each line.
     * @return Number of converted lines.
     * @throws IOException In case of IO error
     */
    public long convert(Reader reader, StringComposedEventListener composedEventListener) throws IOException {
        TextParseTask parseTask = new TextParseTask(this.parseSchema, reader, parseConfig);
        ConvertTask convertTask = new ConvertTask(parseTask, makeComposer(composeSchema, composedEventListener));
        return execute(convertTask);
    }

    /**
     * Creates the composer. Makes it possible to override with custom made implementation of {@link StringComposer}
     * @param schema                The output schema to use while composing.
     * @param composedEventListener The string composed event listener that get notification of each line.
     * @return The composer to use in this converter
     */
    @SuppressWarnings("WeakerAccess")
    protected Composer makeComposer(Schema schema, StringComposedEventListener composedEventListener) {
        return new StringComposer(schema, composedEventListener);
    }

    public TextParseConfig getParseConfig() {
        return parseConfig;
    }

    public void setParseConfig(TextParseConfig parseConfig) {
        this.parseConfig = parseConfig;
    }
}
