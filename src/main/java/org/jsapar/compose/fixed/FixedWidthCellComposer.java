package org.jsapar.compose.fixed;

import org.jsapar.compose.cell.CellComposer;
import org.jsapar.model.Cell;
import org.jsapar.schema.FixedWidthSchemaCell;

import java.io.IOException;
import java.io.Writer;

/**
 * Composes fixed width output on cell level.
 */
class FixedWidthCellComposer {

    CellComposer cellComposer = new CellComposer();
    private Writer writer;

    FixedWidthCellComposer(Writer writer) {
        this.writer = writer;
    }

    /**
     * Writes a cell to the supplied writer using supplied fill character.
     *
     * @param cell
     *            The cell to write
     * @param schemaCell The schema of the cell
     * @throws IOException
     */
    void compose(Cell cell, FixedWidthSchemaCell schemaCell) throws IOException {
        String sValue = cellComposer.format(cell, schemaCell);
        compose(sValue, schemaCell.getPadCharacter(), schemaCell.getLength(), schemaCell.getAlignment());
    }

    /**
     * Writes a cell to the supplied writer using supplied fill character.
     *
     * @param sValue
     *            The value to write
     * @param fillCharacter
     *            The fill character to fill empty spaces.
     * @param length
     *            The number of characters to write.
     * @param alignment
     *            The alignment of the cell content if the content is smaller than the cell length.
     * @throws IOException
     */
    private void compose(String sValue, char fillCharacter, int length, FixedWidthSchemaCell.Alignment alignment)
            throws IOException{
        if (sValue.length() == length) {
            writer.write(sValue);
        } else if (sValue.length() > length) {
            // If the cell value is larger than the cell length, we have to cut the value.
            alignment.fit(writer, length, sValue);
        } else {
            // Otherwise use the alignment of the schema.
            int nToFill = length - sValue.length();
            pad(alignment, writer, nToFill, sValue, fillCharacter);
        }
    }

    /**
     * Padds supplied value in the correct end with the supplied number of characters
     *
     * @param alignment How to allign the value of the cell.
     * @param writer The writer to write to
     * @param nToFill Number of characters to fill
     * @param sValue The value to write
     * @param fillCharacter The fill character to use.
     * @throws IOException
     */
    private void pad(FixedWidthSchemaCell.Alignment alignment,
                     Writer writer,
                     int nToFill,
                     String sValue,
                     char fillCharacter) throws IOException {
        switch (alignment) {

        case LEFT:
            writer.write(sValue);
            fill(writer, fillCharacter, nToFill);
            break;
        case CENTER:
            int nLeft = nToFill / 2;
            fill(writer, fillCharacter, nLeft);
            writer.write(sValue);
            fill(writer, fillCharacter, nToFill - nLeft);
            break;
        case RIGHT:
            fill(writer, fillCharacter, nToFill);
            writer.write(sValue);
            break;
        }
    }

    /**
     * Writes specified fill character specified number of times.
     * @param writer The writer to write to
     * @param ch The character to write
     * @param nSize Number of times to write the character
     * @throws IOException
     */
    static void fill(Writer writer, char ch, int nSize) throws IOException {
        for (int i = 0; i < nSize; i++) {
            writer.write(ch);
        }
    }

}
