package org.jsapar.parse;

import org.jsapar.error.JSaParException;
import org.jsapar.parse.text.LineReader;

import java.io.IOException;

public class SingleLineReader implements LineReader {
    private String line;

    public SingleLineReader(String line) {
        super();
        this.line = line;
    }

    @Override
    public String readLine() throws IOException, JSaParException {
        try {
            return line;
        } finally {
            line = null;
        }
    }

    @Override
    public String getLineSeparator() {
        return "|";
    }

    @Override
    public boolean eofReached() {
        return line == null;
    }

}
