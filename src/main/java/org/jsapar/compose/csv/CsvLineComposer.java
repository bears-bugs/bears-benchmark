package org.jsapar.compose.csv;

import org.jsapar.compose.line.LineComposer;
import org.jsapar.compose.csv.quote.*;
import org.jsapar.model.Cell;
import org.jsapar.model.CellType;
import org.jsapar.model.Line;
import org.jsapar.model.StringCell;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;
import org.jsapar.schema.QuoteBehavior;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Composes csv line output based on schema and provided line.
 * Created by stejon0 on 2016-01-30.
 */
class CsvLineComposer implements LineComposer {

    private Writer        writer;
    private CsvSchemaLine schemaLine;
    private String lineSeparator;
    private Map<String, CsvCellComposer> cellComposers;
    private boolean firstRow=true;

    CsvLineComposer(Writer writer, CsvSchemaLine schemaLine, String lineSeparator) {
        this.writer = writer;
        this.schemaLine = schemaLine;
        this.lineSeparator = lineSeparator;
        cellComposers = makeCellComposers(schemaLine, lineSeparator);
    }

    private Map<String, CsvCellComposer> makeCellComposers(CsvSchemaLine schemaLine, String lineSeparator) {
        return schemaLine.getSchemaCells().stream()
                .collect(Collectors.toMap(CsvSchemaCell::getName, it ->makeCellComposer(schemaLine, it, lineSeparator)));
    }

    private CsvCellComposer makeCellComposer(CsvSchemaLine schemaLine, CsvSchemaCell schemaCell, String lineSeparator) {
        return new CsvCellComposer(schemaCell, makeQuoter(schemaLine, schemaCell, lineSeparator));
    }

    private Quoter makeQuoter(CsvSchemaLine schemaLine, CsvSchemaCell schemaCell, String lineSeparator) {
        char quoteChar = schemaLine.getQuoteChar();
        QuoteBehavior quoteBehavior = schemaCell.getQuoteBehavior();
        switch (quoteBehavior) {
            case AUTOMATIC:
                if (quoteChar != 0)
                    if (schemaCell.getCellFormat().getCellType().isAtomic())
                        return new NeverQuote(schemaCell.getMaxLength());
                    else
                        return new QuoteIfNeeded(quoteChar, schemaCell.getMaxLength(), schemaLine.getCellSeparator(), lineSeparator);
                else
                    return makeReplaceQuoter(schemaLine, schemaCell, lineSeparator);
            case NEVER:
                return new NeverQuote(schemaCell.getMaxLength());
            case REPLACE:
                return makeReplaceQuoter(schemaLine, schemaCell, lineSeparator);
            case ALWAYS:
                return new AlwaysQuote(quoteChar, schemaCell.getMaxLength());
            default:
                throw new IllegalStateException("Unsupported quote behavior: " + quoteBehavior);
        }
    }

    private Quoter makeReplaceQuoter(CsvSchemaLine schemaLine, CsvSchemaCell schemaCell, String lineSeparator) {
        if (schemaCell.getCellFormat().getCellType().isAtomic())
            return new NeverQuote(schemaCell.getMaxLength());
        else
            return new NeverQuoteButReplace(schemaCell.getMaxLength(), schemaLine.getCellSeparator(), lineSeparator, "\u00A0");
    }

    /**
     * This implementation composes a csv output based on the line schema and provided line.
     * @param line The line to compose output of.
     * @throws UncheckedIOException If there is an error writing line to writer.
     */
    @Override
    public void compose(Line line) {
        try {
            if (schemaLine.isIgnoreWrite())
                return;
            if (firstRow && schemaLine.isFirstLineAsSchema()) {
                composeHeaderLine();
                writer.write(lineSeparator);
            }
            firstRow = false;
            String sCellSeparator = schemaLine.getCellSeparator();

            Iterator<CsvSchemaCell> iter = schemaLine.iterator();
            while (iter.hasNext()) {
                CsvSchemaCell schemaCell = iter.next();
                Cell cell = line.getCell(schemaCell.getName()).orElse(schemaCell.makeEmptyCell());
                CsvCellComposer cellComposer = cellComposers.get(schemaCell.getName());
                cellComposer.compose(writer, cell);

                if (iter.hasNext())
                    writer.write(sCellSeparator);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean ignoreWrite() {
        return schemaLine.isIgnoreWrite();
    }

    /**
     * Writes header line if first line is schema.
     */
    private void composeHeaderLine() {
        CsvSchemaLine unformattedSchemaLine = schemaLine.clone();
        unformattedSchemaLine.setFirstLineAsSchema(false);
        for (CsvSchemaCell schemaCell : unformattedSchemaLine.getSchemaCells()) {
            schemaCell.setCellFormat(CellType.STRING);
        }
        CsvLineComposer headerLineComposer = new CsvLineComposer(writer, unformattedSchemaLine, lineSeparator);
        headerLineComposer.compose(this.buildHeaderLineFromSchema(unformattedSchemaLine));
    }

    /**
     * @return The header line
     *
     */
    private Line buildHeaderLineFromSchema(CsvSchemaLine headerSchemaLine)  {
        Line line = new Line(headerSchemaLine.getLineType());

        for (CsvSchemaCell schemaCell : headerSchemaLine.getSchemaCells()) {
            line.addCell(new StringCell(schemaCell.getName(), schemaCell.getName()));
        }

        return line;
    }

}
