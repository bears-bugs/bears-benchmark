package org.jsapar.parse.csv;

import org.jsapar.parse.text.LineReader;
import org.jsapar.parse.text.TextLineReader;
import org.jsapar.parse.text.TextLineReaderAnyCRLF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * LineReader implementation that reads lines from a Reader object.
 * <p>
 * Reader object should be closed by caller. Once End of File has been reached, the instance will no longer be useful
 * (unless it is reset to a previous state by calling reset() ).
 * <p>
 * The class may read characters ahead from the provided reader before it is used in any result.
 *
 */
class BufferedLineReader implements LineReader {

    private static final int MAX_LINE_LENGTH = 10 * 1024;

    private long lineNumber = 0;
    private BufferedReader reader;
    private TextLineReader lineReaderImpl;

    /**
     * Creates a lineReader instance reading from a reader.
     *
     * @param lineSeparator The line separator character to use while parsing lines.
     * @param reader        The reader to read from.
     */
    BufferedLineReader(String lineSeparator, Reader reader) {
        super();
        this.reader = new BufferedReader(reader);
        this.lineReaderImpl = makeLineReaderImpl(this.reader, lineSeparator);
    }

    private TextLineReader makeLineReaderImpl(Reader reader, String lineSeparator) {
        return TextLineReaderAnyCRLF.isLineSeparatorSupported(lineSeparator) ?
                new TextLineReaderAnyCRLF(lineSeparator, reader) :
                new TextLineReader(lineSeparator, reader);
    }

    /**
     * Reads one line from the input and returns the result without line separator characters.
     * This implementation marks the internal input reader before reading any further in order to be able to reset the
     * action later.
     *
     * @return The line read from the internal input reader.
     * @throws IOException If there is an io error.
     *
     */
    @Override
    public String readLine() throws IOException {
        reader.mark(MAX_LINE_LENGTH);
        lineNumber++;
        return lineReaderImpl.readLine();
    }

    /**
     * Reads another line from the input reader and returns the result without line separator characters.
     * Does not create any mark in the input reader, thus the next call to reset() will not reset this action but
     * instead the previous call to readLine()
     *
     * @return The line read from the internal input reader.
     * @throws IOException If there is an io error.
     *
     */
    String peekLine() throws IOException {
        return lineReaderImpl.readLine();
    }

    /**
     * Resets state of reader to before last call to readLine
     *
     * @throws IOException If there is an io error.
     */
    void reset() throws IOException {
        reader.reset();
        lineNumber--;
        lineReaderImpl.resetEof();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.input.parse.LineReader#getLineSeparator()
     */
    @Override
    public String getLineSeparator() {
        return lineReaderImpl.getLineSeparator();
    }

    /**
     * @return The internal reader that is used to read lines.
     */
    Reader getReader() {
        return reader;
    }

    /**
     * @return 0 before any line has been read. The number of lines read by realLine() after that if not reset by call
     * to reset(). Calls to peekLine() are not counted.
     */
    long getLineNumber() {
        return lineNumber;
    }

    /**
     * @return True if End of input stream was reached, false otherwise.
     */
    @Override
    public boolean eofReached() {
        return lineReaderImpl.eofReached();
    }
}
