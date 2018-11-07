package org.jsapar.compose.string;

import java.util.stream.Stream;

/**
 * Generated once for each line that is composed.
 */
public class StringComposedEvent {
    private final String lineType;
    private final Stream<String> line;
    private final long lineNumber;

    StringComposedEvent(String lineType, long lineNumber, Stream<String> line) {
        this.lineType = lineType;
        this.lineNumber = lineNumber;
        this.line = line;
    }

    /**
     * Can only be called once for each instance of this event.
     *
     * @return A stream of String values for all cells in this line.
     */
    public Stream<String> stream() {
        return line;
    }

    /**
     * @return The type of the line that generated this event.
     */
    public String getLineType() {
        return lineType;
    }

    /**
     * @return The line number within the parsed input of the line in this event.
     */
    public long getLineNumber() {
        return lineNumber;
    }
}
