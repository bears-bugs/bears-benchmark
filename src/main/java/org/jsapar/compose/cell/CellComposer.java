package org.jsapar.compose.cell;

import org.jsapar.model.Cell;
import org.jsapar.schema.SchemaCell;

import java.text.Format;

/**
 * Internal utility class that helps composing a cell.
 */
public class CellComposer {

    private static final String         EMPTY_STRING          = "";

    /**
     * Formats a cell to a string according to the rules of this schema.
     *
     * @param cell
     *            The cell to format. If this parameter is null or an empty string, the default
     *            value will be returned or if there is no default value, an empty string will be
     *            returned.
     * @param schemaCell The cell schema to use for this cell
     * @return The formatted value for this cell.
     */
    public String format(Cell cell, SchemaCell schemaCell) {
        if (schemaCell.isIgnoreWrite())
            return EMPTY_STRING;

        if (cell == null || cell.isEmpty()) {
            return defaultValueOrEmpty(schemaCell);
        }
        Format format = schemaCell.getCellFormat().getFormat();
        return format != null ? format.format(cell.getValue()) : cell.getStringValue();
    }

    /**
     * @return The default value if it is not null or empty string otherwise.
     * @param schemaCell The cell schema to use
     */
    private String defaultValueOrEmpty(SchemaCell schemaCell) {
        return schemaCell.isDefaultValue() ? schemaCell.getDefaultValue() : EMPTY_STRING;
    }

}
