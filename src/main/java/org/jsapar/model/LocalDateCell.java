package org.jsapar.model;

import java.time.LocalDate;

/**
 */
public class LocalDateCell extends TemporalCell<LocalDate>{

    /**
     * Creates a cell with a name and value.
     *
     * @param name     The name of the cell
     * @param value    The value of the cell
     */
    public LocalDateCell(String name, LocalDate value) {
        super(name, value, CellType.LOCAL_DATE);
    }

    @Override
    public int compareValueTo(Cell<LocalDate> right) {
        return this.getValue().compareTo(right.getValue());
    }

    public static Cell emptyOf(String name) {
        return new EmptyCell(name, CellType.LOCAL_DATE);
    }
}
