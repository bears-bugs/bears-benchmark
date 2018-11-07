package org.jsapar.parse.csv;

import org.jsapar.error.JSaParException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Responsible for splitting quoted cells into a raw array of Strings.
 *
 */
class QuotedCellSplitter implements CellSplitter {
    private static final String[] EMPTY_LINE = new String[0];

    private final BufferedLineReader lineReader;
    private final String       cellSeparator;
    private final char         quoteChar;
    private final CellSplitter cellSplitter;
    private final int maxLinesWithinCell;
    private final String separatorAndQuote;
    private final String quoteAndSeparator;

    /**
     * @param cellSeparator
     *            The string that separates the cells on the line, usually only one character but can be a combination
     *            of many characters.
     * @param quoteChar
     *            The quote character to use for quoted cells.
     */
    QuotedCellSplitter(String cellSeparator, char quoteChar) {
        this(cellSeparator, quoteChar, null, 25);
    }

    /**
     * @param cellSeparator
     *            The string that separates the cells on the line, usually only one character but can be a combination
     *            of many characters.
     * @param quoteChar
     *            The quote character to use for quoted cells.
     * @param lineReader
     *            The line reader to read next line from in case multi-line cells are supported. Set to null if
     *            multi-line cells are not supported.
     * @param maxLinesWithinCell Maximum number of line breaks allowed within one cell.
     */
    QuotedCellSplitter(String cellSeparator, char quoteChar, BufferedLineReader lineReader, int maxLinesWithinCell) {
        this.cellSeparator = cellSeparator;
        this.quoteChar = quoteChar;
        this.lineReader = lineReader;
        this.cellSplitter = new SimpleCellSplitter(cellSeparator);
        this.maxLinesWithinCell = maxLinesWithinCell;
        this.separatorAndQuote = cellSeparator + quoteChar;
        this.quoteAndSeparator = quoteChar + cellSeparator;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.input.parse.CellSplitter#split(java.lang.String)
     */
    @Override
    public String[] split(String sLine) throws IOException, JSaParException {
        if(sLine.isEmpty()) {
            return EMPTY_LINE;
        }
        java.util.List<String> cells = new java.util.ArrayList<>(sLine.length() / 8);
        splitQuoted(cells, sLine);
        if(cells.size() == 1 && cells.get(0).trim().isEmpty())
            return EMPTY_LINE;

        return cells.toArray(new String[cells.size()]);
    }

    /**
     * Recursively find all quoted cells. A quoted cell is where the quote character is the first and last character in
     * the cell. Any other quote characters within the cells are ignored.
     * 
     * @param cells Resulting list of split cells built by this method.
     * @param sToSplit String to split.
     * @throws IOException In case there is an error reading the source.
     */
    private void splitQuoted(List<String> cells, String sToSplit) throws IOException {
        int nIndex = 0;
        if (sToSplit.length() <= 0)
            return;

        if (sToSplit.charAt(0) == quoteChar) {
            if(sToSplit.length()==1) {
                // Quote is the only character in the string.
                cells.add(sToSplit);
                return;
            }
            // Quote is the first character in the string.
        } else {
            // Search for quote character at first position after a cell separator. Otherwise ignore quotes.
            int nFoundQuoteIndex = sToSplit.indexOf(separatorAndQuote);

            if (nFoundQuoteIndex < 0) {
                cells.addAll(Arrays.asList(cellSplitter.split(sToSplit)));
                return;
            } else if (nFoundQuoteIndex > 0) {
                String sUnquoted = sToSplit.substring(0, nFoundQuoteIndex);
                String[] asCells = cellSplitter.split(sUnquoted);
                cells.addAll(Arrays.asList(asCells));
            } else {
                cells.add("");
            }
            nIndex = nFoundQuoteIndex + cellSeparator.length();  // Behind cell separator
        }

        int lineCounter = 1;
        // We do this in a do-while loop instead of recursive call since int will exhaust stack in case line separator
        // is not correctly specified.
        do {
            nIndex++; // Behind first quote
            int nFoundEnd = sToSplit.indexOf(quoteAndSeparator, nIndex);
            if (nFoundEnd < 0) {
                // Last character is quote
                if (nIndex < sToSplit.length() && sToSplit.length() > 1
                        && sToSplit.charAt(sToSplit.length() - 1) == quoteChar) {
                    final String sFound = sToSplit.substring(nIndex, sToSplit.length() - 1);
                    cells.add(sFound);
                    return;
                }
                if(lineCounter <= 1) {
                    int nextQuoteIndex = sToSplit.indexOf(quoteChar, nIndex);
                    if (nextQuoteIndex >= 0) {
                        // Find next cell separator after the end quote character
                        int endOfCellIndex = sToSplit.indexOf(cellSeparator, nextQuoteIndex + 1);
                        endOfCellIndex = endOfCellIndex >= 0 ? endOfCellIndex : sToSplit.length();
                        if (nextQuoteIndex < endOfCellIndex) {
                            // The end quote is within the same cell but not last character, consider quote to be part of string.
                            cells.add(sToSplit.substring(nIndex - 1, endOfCellIndex));
                            nIndex = endOfCellIndex + cellSeparator.length();
                            continue;
                        }
                    }
                }
                if(lineReader == null || maxLinesWithinCell <= 1){
                    int endOfCellIndex = sToSplit.indexOf(cellSeparator, nIndex);
                    cells.add(sToSplit.substring(nIndex - 1, endOfCellIndex));
                    nIndex = endOfCellIndex + cellSeparator.length();
                    continue;

                }
                else if (lineCounter > maxLinesWithinCell) {
                    throw new JSaParException(
                            "Searched "+lineCounter+" lines without finding an end of quoted cell. End quote is probably missing.");
                }
                String nextLine = lineReader.peekLine();
                if (nextLine == null) {
                    throw new JSaParException("End quote is missing for quoted cell. Reached end of file.");
                }
                lineCounter++;
                // Add next line and try again to find end quote
                sToSplit = sToSplit.substring(nIndex - 1) + lineReader.getLineSeparator() + nextLine;
                nIndex = 0;
                continue;
            }
            final String sFound = sToSplit.substring(nIndex, nFoundEnd);
            nIndex = nFoundEnd + 1; // Behind quote
            cells.add(sFound);

            // Continue to pick quoted cells.
            nIndex += cellSeparator.length();
        } while (nIndex < sToSplit.length() && sToSplit.charAt(nIndex) == quoteChar);

        // Reached end of line
        if (nIndex >= sToSplit.length()) {
            return;
        }

        // Next cell is not quoted
        // Now handle the rest of the string with a recursive call.
        splitQuoted(cells, sToSplit.substring(nIndex));
    }

}
