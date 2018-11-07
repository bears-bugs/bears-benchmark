package org.jsapar.compose;

import org.jsapar.model.Line;

/**
 * Internal common interface for all schema composers that uses a schema to compose text output.
 * You should normally not use this class directly in your code.
 * Use a {@link org.jsapar.TextComposer} instead.
 */
public interface SchemaComposer {

    /**
     * Composes line separator accoring to schema.
     */
    void composeLineSeparator();


    /**
     * Composes the supplied line but does not write any line separator.
     *
     * @param line The line to compose output from.
     * @return True if line was actually composed, false otherwise.
     */
    boolean composeLine(Line line) ;
}
