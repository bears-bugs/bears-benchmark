package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for different quote strategies.
 */
public interface Quoter extends ValueComposer {

    /**
     * Writes the value to the writer and applies the quote behavior of the strategy implementation.
     * @param writer  The writer to write the quoted value to.
     * @param value The value to quote
     * @throws IOException In case there was an error writing to output.
     */
    void writeValue(Writer writer, String value) throws IOException;
}
