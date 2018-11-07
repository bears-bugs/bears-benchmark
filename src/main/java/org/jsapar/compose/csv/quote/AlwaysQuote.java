package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 * Always quotes supplied value and limits length to supplied max length.
 */
public class AlwaysQuote implements Quoter {
    private ValueComposer valueComposer;
    private char quoteChar;

    public AlwaysQuote(char quoteChar, int maxLength) {
        valueComposer = maxLength >=0 ? new MaxLengthComposer(maxLength-2) : new AtomicValueComposer();
        this.quoteChar = (quoteChar == 0 ? '"' : quoteChar);
    }

    @Override
    public void writeValue(Writer writer, String value) throws IOException {
        writer.write(quoteChar);
        valueComposer.writeValue(writer, value);
        writer.write(quoteChar);
    }
}
