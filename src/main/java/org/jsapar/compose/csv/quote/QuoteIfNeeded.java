package org.jsapar.compose.csv.quote;

import java.io.IOException;
import java.io.Writer;

/**
 * Quotes cell only if needed because it contains either cellSeparator or lineSeparator. Also, if first character
 * is the quote character, the cell is quoted because it will otherwise trigger quoted parsing while parsing.
 */
public class QuoteIfNeeded implements Quoter {
    private char quoteChar;
    private AlwaysQuote alwaysQuote;
    private NeverQuote neverQuote;
    private String cellSeparator;
    private String lineSeparator;

    public QuoteIfNeeded(char quoteChar, int maxLength, String cellSeparator, String lineSeparator) {
        this.quoteChar = quoteChar;
        this.alwaysQuote = new AlwaysQuote(quoteChar, maxLength);
        this.neverQuote = new NeverQuote(maxLength);
        this.cellSeparator = cellSeparator;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void writeValue(Writer writer, String value) throws IOException {
        if(value.isEmpty())
            neverQuote.writeValue(writer, value);
        else if (value.contains(cellSeparator)
                || value.charAt(0) ==quoteChar && (value.length()==1 || value.charAt(value.length()-1) != quoteChar)
                || value.contains(lineSeparator)){
            alwaysQuote.writeValue(writer, value);
        }
        else
            neverQuote.writeValue(writer, value);
    }
}
