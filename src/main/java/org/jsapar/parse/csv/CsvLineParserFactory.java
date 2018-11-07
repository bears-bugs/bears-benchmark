package org.jsapar.parse.csv;

import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.CsvSchemaLine;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates csv line parsers based on schema.
 */
class CsvLineParserFactory {

    private List<CsvLineParserMatcher> lineParserMatchers;

    CsvLineParserFactory(CsvSchema schema, TextParseConfig config) {
        lineParserMatchers = new LinkedList<>();
        for (CsvSchemaLine schemaLine : schema.getSchemaLines()) {
            lineParserMatchers.add(new CsvLineParserMatcher(schemaLine, config));
        }
    }

    /**
     * @param lineReader A {@link CsvLineReader} that can read csv lines.
     * @return A line parser that can be used to parse the next line.
     * @throws IOException If there is an io error.
     */
    CsvLineParser makeLineParser(CsvLineReader lineReader) throws IOException {
        if (lineParserMatchers.isEmpty())
            return null;
        Iterator<CsvLineParserMatcher> iter = lineParserMatchers.iterator();
        boolean first = true;
        while (iter.hasNext()) {
            CsvLineParserMatcher currentMatcher = iter.next();
            CsvLineParser lineParser = currentMatcher.makeLineParserIfMatching(lineReader);
            if (lineParser == null) {
                if (lineReader.eofReached())
                    return null;
            }
            else {
                if (!currentMatcher.isOccursLeft())
                    // No longer needed
                    iter.remove();
                else if (!first) {
                    // Move current matching line first in list so that it is tested first next time.
                    iter.remove();
                    lineParserMatchers.add(0, currentMatcher);
                }
                return lineParser;
            }
            first = false;
        }
        return null;
    }

    boolean isEmpty() {
        return lineParserMatchers.isEmpty();
    }
}
