package org.jsapar.parse.csv;

import org.jsapar.error.JSaParException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Simple cell splitter that can be used when we don't have to consider quotes.
 */
class SimpleCellSplitter implements CellSplitter {

    private static final String[] EMPTY_LINE = new String[0];
    private final String splitPattern;

    SimpleCellSplitter(String cellSeparator) {
        this.splitPattern = Pattern.quote(cellSeparator);
    }

    @Override
    public String[] split(String sLine) throws IOException, JSaParException {
        if(sLine.isEmpty())
            return EMPTY_LINE;
        String[] aLine = sLine.split(splitPattern, -1);
        if(aLine.length == 1 && aLine[0].trim().isEmpty())
            return EMPTY_LINE;
        return aLine;
    }

}
