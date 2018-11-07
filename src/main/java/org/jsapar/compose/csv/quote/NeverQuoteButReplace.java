package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 * Will never quote the cell but instead replaces cellSeparator or lineSeparator within a cell with supplied replace string.
 */
public class NeverQuoteButReplace implements Quoter {
    private NeverQuote neverQuote;
    private String cellSeparator;
    private String lineSeparator;
    private String replaceString;


    public NeverQuoteButReplace(int maxLength, String cellSeparator, String lineSeparator, String replaceString) {
        this.neverQuote = new NeverQuote(maxLength);
        this.cellSeparator = cellSeparator;
        this.lineSeparator = lineSeparator;
        this.replaceString = replaceString;
    }


    @Override
    public void writeValue(Writer writer, String value) throws IOException {
        value = value.replace(cellSeparator, replaceString).replace(lineSeparator, replaceString);
        neverQuote.writeValue(writer, value);
    }
}
