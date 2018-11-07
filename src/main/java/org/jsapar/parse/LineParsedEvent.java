/**
 *
 */
package org.jsapar.parse;

import org.jsapar.model.Line;

import java.util.EventObject;

/**
 * Event object that is sent as event to all {@link LineEventListener} instances of a parser for each line while parsing.
 */
public final class LineParsedEvent extends EventObject {

    /**
     *
     */
    private static final long serialVersionUID = 9009392654758990080L;
    private final Line line;

    /**
     * @param source The sending class.
     * @param line   The line that was parsed.
     */
    public LineParsedEvent(Object source, Line line) {
        super(source);
        this.line = line;
    }

    /**
     * @return the line
     */
    public Line getLine() {
        return line;
    }

    /**
     * @return The line number of the line in this event.
     */
    public long getLineNumber() {
        return line.getLineNumber();
    }
}
