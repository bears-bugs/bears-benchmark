package org.jsapar.compose.fixed;

import org.jsapar.compose.line.LineComposer;
import org.jsapar.model.Cell;
import org.jsapar.model.Line;
import org.jsapar.schema.FixedWidthSchemaCell;
import org.jsapar.schema.FixedWidthSchemaLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Optional;

/**
 * Composes line to a fixed width format based on line schema.
 * Created by stejon0 on 2016-01-31.
 */
class FixedWidthLineComposer implements LineComposer {

    private final Writer writer;
    private final FixedWidthSchemaLine lineSchema;
    private final FixedWidthCellComposer cellComposer;

    FixedWidthLineComposer(Writer writer, FixedWidthSchemaLine lineSchema) {
        this.writer = writer;
        this.lineSchema = lineSchema;
        this.cellComposer = new FixedWidthCellComposer(writer);
    }

    /**
     * Composes an output from a line. Each cell is identified from the schema by the name of the cell.
     *
     * If the schema-cell has a name the cell with the same name is used. If no such cell is found the positions are
     * filled with fill character defined by the schema.
     *
     * @param line
     *            The line to write to the writer
     * @throws UncheckedIOException If an IO error occurs.
     *
     */
    @Override
    public void compose(Line line)  {
        try {
            if (lineSchema.isIgnoreWrite())
                return;
            Iterator<FixedWidthSchemaCell> iter = lineSchema.getSchemaCells().iterator();

            // Iterate all schema cells.
            int totalLength = 0;
            while (iter.hasNext()) {
                FixedWidthSchemaCell schemaCell = iter.next();
                totalLength += schemaCell.getLength();
                Optional<Cell> oCell = line.getCell(schemaCell.getName());
                cellComposer.compose(oCell.orElse(schemaCell.makeEmptyCell()), schemaCell);
            }
            if (lineSchema.getMinLength() > totalLength) {
                FixedWidthCellComposer.fill(writer, lineSchema.getPadCharacter(), lineSchema.getMinLength() - totalLength);
            }
        }
        catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean ignoreWrite() {
        return lineSchema.isIgnoreWrite();
    }
}
