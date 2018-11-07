package org.jsapar.parse.text;

import java.io.IOException;

/**
 * Reads line separated input text line by line.
 */
public interface LineReader {

    /**
     * Parses one line from the input and returns the result without line separator characters.
     * 
     * @return The next available line from the input without trailing line separator or null if end of input buffer was
     *         reached.
     * @throws IOException In case there was an error reading from input.
     */
    String readLine() throws IOException;


    /**
     * @return The line separator.
     */
    String getLineSeparator();

    /**
     * @return True if this reader has reached end of input stream. False otherwise.
     */
    boolean eofReached();
}
