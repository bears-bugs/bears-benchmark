package org.jsapar.parse.fixed;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.Line;
import org.jsapar.parse.LineEventListener;
import org.jsapar.parse.LineParsedEvent;
import org.jsapar.parse.text.LineReader;
import org.jsapar.parse.text.TextLineReader;
import org.jsapar.parse.text.TextLineReaderAnyCRLF;
import org.jsapar.parse.text.TextParseConfig;
import org.jsapar.schema.FixedWidthSchema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Parses fixed with text where lines are separated by a line separator character sequence.
 */
public class FixedWidthParserLinesSeparated extends FixedWidthParser {

    private LineReader lineReader;

    /**
     * Creates a parser for fixed with cells where lines are separated.
     * @param reader The reader to read from.
     * @param schema The schema to use.
     * @param config The parse configuration to use.
     */
    public FixedWidthParserLinesSeparated(Reader reader, FixedWidthSchema schema, TextParseConfig config) {
        super(schema, config);
        lineReader = makeLineReader(reader, schema.getLineSeparator());
    }

    private LineReader makeLineReader(Reader reader, String lineSeparator) {
        return TextLineReaderAnyCRLF.isLineSeparatorSupported(lineSeparator) ?
                new TextLineReaderAnyCRLF(lineSeparator, reader) :
                new TextLineReader(lineSeparator, reader);
    }

    @Override
    public long parse(LineEventListener listener, ErrorEventListener errorListener) throws IOException {

        FWLineParserFactory lineParserFactory = new FWLineParserFactory(getSchema(), getConfig());
        long lineNumber = 0;
        while(true){
            String sLine = lineReader.readLine();
            if(sLine == null)
                return lineNumber;
            if(sLine.trim().isEmpty()) // Just ignore empty lines
                continue;
            lineNumber++;
            try(BufferedReader r = new BufferedReader(new StringReader(sLine))) {
                if(lineParserFactory.isEmpty())
                    return lineNumber-1;
                FixedWidthLineParser lineParser = lineParserFactory.makeLineParser(r);
                if (lineParser == null) {
                    handleNoParser(lineNumber, lineParserFactory.getLastResult(), errorListener);
                    if(lineParserFactory.getLastResult() == LineParserMatcherResult.NOT_MATCHING)
                        continue;
                    else
                        return lineNumber-1;
                }
                Line line = lineParser.parse(r, lineNumber, errorListener );
                if (line == null) // Should never occur.
                    throw new AssertionError("Unexpected error while parsing line number " + lineNumber);
                String remaining = r.readLine();
                if(remaining != null && ! remaining.isEmpty()) {
                    if(!getValidationHandler().lineValidation(this, lineNumber, "Trailing characters found on line",
                            getConfig().getOnLineOverflow(), errorListener))
                        continue; // Ignore the line.
                }
                if(lineParser.isIgnoreRead())
                    continue;
                listener.lineParsedEvent(new LineParsedEvent(this, line));
            }
        }
    }

}
