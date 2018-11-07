package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 */
public class MaxLengthComposer implements ValueComposer {
    private int maxLength;

    public MaxLengthComposer(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
    }

    @Override
    public void writeValue(Writer writer, String value) throws IOException {
        if(value.length() > maxLength)
            writer.write(value,0, maxLength);
        else
            writer.write(value);
    }
}
