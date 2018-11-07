package org.jsapar.parse.text;

import java.io.IOException;
import java.io.Reader;

/**
 * Internal class for reading lines from a text.
 * {@link LineReader} implementation that reads lines from a text {@link Reader} object. This implementation handles
 * any combination of CR+LF or LF as line separator.
 * <p>
 * {@link Reader} object should be closed by caller. Once End of File has been reached, the instance will no longer be useful.
 * <p>
 * Calling {@link #getLineSeparator()} will return the last line separator that was detected.
 */
public class TextLineReaderAnyCRLF extends TextLineReader {

    private final static String[] lineSeparators=new String[]{"\n", "\r\n"};
    private int lineSeparatorIndex=-1;

    private Reader  reader;

    /**
     * Creates a lineReader instance reading from a reader.
     *
     * @param lineSeparator  The line separator returned as default before any line is detected.
     * @param reader
     *            The reader to read from.
     */
    public TextLineReaderAnyCRLF(String lineSeparator, Reader reader) {
        super(lineSeparator, reader);
        for(int i=0; i<2 ; i++)
            if(lineSeparator.equals(lineSeparators[i]))
                lineSeparatorIndex = i;
        if(lineSeparatorIndex < 0)
            throw new IllegalArgumentException("Only line separator CR+LF or LF are allowed.");
        this.reader = reader;
    }

    /**
     * @param lineSeparator The line separator to test.
     * @return True if supplied line separator is one of either CR+LF or LF.
     */
    public static boolean isLineSeparatorSupported(String lineSeparator){
        for(int i=0; i<2 ; i++)
            if(lineSeparator.equals(lineSeparators[i]))
                return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.input.parse.LineReader#readLine()
     */
    @Override
    public String readLine() throws IOException {
        if (eofReached())
            return null;
        StringBuilder lineBuilder = new StringBuilder(INITIAL_LINE_CAPACITY);
        boolean crFound = false;
        char[] buffer = new char[1]; // Re-using same buffer.
        while (true) {
            int nRead = reader.read(buffer, 0, 1);
            if (nRead < 1) {
                setEofReached(true);
                return lineBuilder.toString();
            }
            char chRead = buffer[0];
            if (chRead == '\n') {
                lineSeparatorIndex = crFound ? 1 : 0;
                break; // End of line found.
            }
            else if(chRead == '\r'){
                crFound = true;
            }
            // It was not a complete line separator.
            else if (crFound) {
                // Move pending characters to lineBuilder.
                lineBuilder.append('\r');
                lineBuilder.append(chRead);
                crFound = false;
            } else
                lineBuilder.append(chRead);
            if (lineBuilder.length() > MAX_LINE_LENGTH)
                throw new IOException(
                        "Line size exceeds "+MAX_LINE_LENGTH+" characters. Probably wrong line-separator for the line type within the schema.");
        }
        return lineBuilder.toString();
    }


    /**
     * @return This implementation returns the last detected line separator. Could be any of LF or CR+LF.
     */
    @Override
    public String getLineSeparator() {
        return lineSeparators[lineSeparatorIndex];
    }


    /**
     * @return The internal reader that is used to read lines.
     */
    public Reader getReader() {
        return reader;
    }


}
