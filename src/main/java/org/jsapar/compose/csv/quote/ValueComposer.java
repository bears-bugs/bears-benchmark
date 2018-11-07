package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 */
public interface ValueComposer {

    /**
     * Writes the value to the writer and applies any transforming strategy of the strategy implementation.
     * @param writer  The writer to write the quoted value to.
     * @param value The value to write
     * @throws IOException In case there was an error writing to output.
     */
    void writeValue(Writer writer, String value) throws IOException;

}
