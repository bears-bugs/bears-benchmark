package org.jsapar.compose.csv;

import org.jsapar.compose.cell.CellComposer;
import org.jsapar.compose.csv.quote.Quoter;
import org.jsapar.model.Cell;
import org.jsapar.schema.CsvSchemaCell;

import java.io.IOException;
import java.io.Writer;

/**
 * Composes cell values based on the schema of the cell.
 */
class CsvCellComposer {
    private CsvSchemaCell schemaCell;
    private final static CellComposer cellComposer = new CellComposer();
    private Quoter quoter;

    CsvCellComposer(CsvSchemaCell schemaCell, Quoter quoter)
    {
        this.schemaCell = schemaCell;
        this.quoter = quoter;
    }

    /**
     * Writes the cell to the supplied writer, including quote character if necessary.
     * @param writer
     * @param cell The cell to compose output for.
     * @throws IOException
     */
    void compose(Writer writer, Cell cell) throws IOException {
        quoter.writeValue(writer, cellComposer.format(cell, schemaCell));
    }


}
