package org.jsapar.model;

import java.time.LocalDateTime;

/**
 */
public class LocalDateTimeCell extends TemporalCell<LocalDateTime>{

    /**
     * Creates a cell with a name and value.
     *
     * @param name     The name of the cell
     * @param value    The value of the cell
     */
    public LocalDateTimeCell(String name, LocalDateTime value) {
        super(name, value, CellType.LOCAL_DATE_TIME);
    }

    @Override
    public int compareValueTo(Cell<LocalDateTime> right) {
        return this.getValue().compareTo(right.getValue());
    }

    public static Cell emptyOf(String name) {
        return new EmptyCell(name, CellType.LOCAL_DATE_TIME);
    }
}
