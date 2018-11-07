package org.jsapar.compose.line;

import org.jsapar.model.Line;

import java.io.IOException;

/**
 * Composes output of a line
 */
public interface LineComposer {

    /**
     * Composes an output from a line. Each cell is identified from the schema by the name of the cell.
     *
     * If the schema-cell has a name the cell with the same name is used. If no such cell is found
     * the implementation of this interface can decide how to treat the cell.
     *
     * @param line    The line to compose output of.
     *
     */
    void compose(Line line);


    /**
     * @return True if lines of this type should be ignored when composing.
     */
    boolean ignoreWrite();

}
