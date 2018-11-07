package org.jsapar.compose.string;

import org.jsapar.compose.cell.CellComposer;
import org.jsapar.compose.Composer;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.EmptyCell;
import org.jsapar.model.Line;
import org.jsapar.schema.Schema;
import org.jsapar.schema.SchemaLine;

import java.util.stream.Stream;

/**
 * Composer that creates {@link StringComposedEvent} for each line that is composed.
 * <p>
 * This implementation will return null for all cells of type EmptyCell from the parsed input and
 * where there is no default value.
 * <p>
 * The {@link StringComposedEvent} provides a
 * {@link java.util.stream.Stream} of {@link String} for the current {@link Line} where each
 * string is matches the cell in a schema. Each cell is formatted according to provided
 * {@link Schema}.
 */
public class StringComposerNullOnEmptyCell implements Composer {

    private final Schema             schema;
    private final static CellComposer cellComposer = new CellComposer();
    private final StringComposedEventListener stringComposedEventListener;

    public StringComposerNullOnEmptyCell(Schema schema, StringComposedEventListener composedEventListener) {
        this.schema = schema;
        this.stringComposedEventListener = composedEventListener;
    }

    @Override
    public boolean composeLine(Line line) {
        return schema.getSchemaLine(line.getLineType())
                .filter(schemaLine -> !schemaLine.isIgnoreWrite())
                .map(schemaLine -> stringComposedEvent(new StringComposedEvent(
                        line.getLineType(),
                        line.getLineNumber(),
                        composeStringLine(schemaLine, line))))
                .orElse(false);
    }

    private Stream<String> composeStringLine(SchemaLine schemaLine, Line line) {
        return schemaLine.stream().map(schemaCell -> {
            if (schemaCell.getDefaultValue() != null)
                return cellComposer.format(line.getCell(schemaCell.getName()).orElse(null), schemaCell);
            else
                return line.getCell(schemaCell.getName()).filter(cell -> !(cell instanceof EmptyCell))
                        .map(cell -> cellComposer.format(cell, schemaCell)).orElse(null);
        });
    }

    @Override
    public void setErrorEventListener(ErrorEventListener errorListener) {
//        this.errorEventListener = errorListener; // Not used
    }


    private boolean stringComposedEvent(StringComposedEvent event) {
        if (this.stringComposedEventListener != null) {
            stringComposedEventListener.stringComposedEvent(event);
            return true;
        }
        return false;
    }

}
