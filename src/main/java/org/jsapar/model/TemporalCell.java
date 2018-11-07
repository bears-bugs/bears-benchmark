package org.jsapar.model;

import java.time.temporal.Temporal;

/**
 */
public abstract class TemporalCell<T extends Temporal> extends Cell<T>{

    /**
     * Creates a cell with a name.
     *
     * @param name     The name of the cell
     * @param value    The value of the cell.
     * @param cellType The type of the cell.
     */
    public TemporalCell(String name, T value, CellType cellType) {
        super(name, value, cellType);
    }
}
