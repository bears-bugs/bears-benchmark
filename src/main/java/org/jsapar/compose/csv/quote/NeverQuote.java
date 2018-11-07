package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 * Never quotes and never alters the value except for limiting number of characters written.
 */
public class NeverQuote implements Quoter {
    private ValueComposer valueComposer;

    public NeverQuote(int maxLength) {
        valueComposer = maxLength >=0 ? new MaxLengthComposer(maxLength) : new AtomicValueComposer();
    }

    @Override
    public void writeValue(Writer writer, String value) throws IOException {
        valueComposer.writeValue(writer, value);
    }

}
