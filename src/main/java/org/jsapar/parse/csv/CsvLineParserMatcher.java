package org.jsapar.parse.csv;

import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.CsvSchemaCell;
import org.jsapar.schema.CsvSchemaLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if line matches the current criteria defined within this line schema.
 */
class CsvLineParserMatcher {
    private final CsvSchemaLine schemaLine;
    private List<CsvControlCell> controlCells = new ArrayList<>();
    private CsvLineParser lineParser;
    private int occursLeft;
    private int maxControlPos;

    /**
     * Creates a line parser matcher
     * @param schemaLine The line schema to use for this matcher.
     * @param config Behavior.
     */
    CsvLineParserMatcher(CsvSchemaLine schemaLine, TextParseConfig config) {
        this.schemaLine = schemaLine;
        occursLeft = schemaLine.getOccurs();
        lineParser = new CsvLineParser(schemaLine, config);
        int pos = 0;
        for (CsvSchemaCell schemaCell : schemaLine.getSchemaCells()) {
            if (schemaCell.hasLineCondition()) {
                controlCells.add(new CsvControlCell(pos, schemaCell));
            }
            maxControlPos = pos;
            pos++;
        }
    }

    /**
     * Creates a line parser object if next line to be parsed matches the criteria of this line chema.
     * @param lineReader A line reader to read the line from.
     * @return A {@link CsvLineParser} ready to parse the line or null if next line cannot be parsed by using this schema.
     * @throws IOException If there is an io error.
     */
    CsvLineParser makeLineParserIfMatching(CsvLineReader lineReader) throws IOException{
        if (occursLeft <= 0)
            return null;

        if (!controlCells.isEmpty()) {
            String[] cells = lineReader.readLine(schemaLine.getCellSeparator(), schemaLine.getQuoteChar());
            if(null == cells || cells.length == 0)
                return null; // Empty line
            // We only peek into the line to follow.
            try {
                if (cells.length <= maxControlPos)
                    return null;

                for (CsvControlCell controlCell : controlCells) {

                    String value = cells[controlCell.pos];
                    if (value == null)
                        return null;
                    if (!controlCell.schemaCell.getLineCondition().satisfies(value))
                        return null;
                }
            }
            finally {
                lineReader.reset();
            }
        }
        if (!schemaLine.isOccursInfinitely())
            occursLeft--;
        return lineParser;
    }

    /**
     * Private internal class used to point to a control cell within a schema line.
     */
    private static class CsvControlCell {
        final int           pos;
        final CsvSchemaCell schemaCell;

        CsvControlCell(int pos, CsvSchemaCell schemaCell) {
            this.pos = pos;
            this.schemaCell = schemaCell;
        }
    }

    /**
     * @return True if this line schema can be used regarding number of occurrences. False if number of occurrences are
     * exceeded.
     */
    boolean isOccursLeft() {
        return schemaLine.isOccursInfinitely() || occursLeft > 0;
    }
}
