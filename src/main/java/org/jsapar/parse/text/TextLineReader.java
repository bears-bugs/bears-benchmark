/**
 * 
 */
package org.jsapar.parse.text;

import java.io.IOException;
import java.io.Reader;

/**
 * Internal class for reading lines from a text.
 * {@link LineReader} implementation that reads lines from a text {@link Reader} object.
 * 
 * {@link Reader} object should be closed by caller. Once End of File has been reached, the instance will no longer be useful.
 * 
 */
public class TextLineReader implements LineReader {

    static final int     MAX_LINE_LENGTH       = 10 * 1024;
    static final int     INITIAL_LINE_CAPACITY = 128;
    private final String lineSeparator;

    private Reader  reader;

    private boolean eofReached = false;

    /**
     * Creates a lineReader instance reading from a reader.
     * 
     * @param lineSeparator
     *            The line separator character to use while parsing lines.
     * @param reader
     *            The reader to read from.
     */
    public TextLineReader(String lineSeparator, Reader reader) {
        super();
        this.lineSeparator = lineSeparator;
        this.reader = reader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.input.parse.LineReader#readLine()
     */
    @Override
    public String readLine() throws IOException {
        if (eofReached)
            return null;
        String lineSeparator = getLineSeparator();
        char chLineSeparatorNext = lineSeparator.charAt(0);
        StringBuilder lineBuilder = new StringBuilder(INITIAL_LINE_CAPACITY);
        StringBuilder pending = new StringBuilder(lineSeparator.length());
        char[] buffer = new char[1]; // Re-using same buffer.
        while (true) {
            int nRead = reader.read(buffer, 0, 1);
            if (nRead < 1) {
                eofReached = true;
                return lineBuilder.toString();
            }
            char chRead = buffer[0];
            if (chRead == chLineSeparatorNext) {
                pending.append(chRead);
                if (lineSeparator.length() > pending.length())
                    chLineSeparatorNext = lineSeparator.charAt(pending.length());
                else
                    break; // End of line found.
            }
            // It was not a complete line separator.
            else if (pending.length() > 0) {
                // Move pending characters to lineBuilder.
                lineBuilder.append(pending);
                pending.setLength(0);
                lineBuilder.append(chRead);
            } else
                lineBuilder.append(chRead);
            if (lineBuilder.length() > MAX_LINE_LENGTH)
                throw new IOException(
                        "Line size exceeds "+MAX_LINE_LENGTH+" characters. Probably wrong line-separator for the line type within the schema.");
        }
        return lineBuilder.toString();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.input.parse.LineReader#getLineSeparator()
     */
    @Override
    public String getLineSeparator() {
        return lineSeparator;
    }


    /**
     * @return The internal reader that is used to read lines.
     */
    public Reader getReader() {
        return reader;
    }

    @Override
    public boolean eofReached() {
        return eofReached;
    }

    public void resetEof() {
        this.eofReached = false;
    }

    void setEofReached(boolean eofReached){
        this.eofReached = eofReached;
    }
}
