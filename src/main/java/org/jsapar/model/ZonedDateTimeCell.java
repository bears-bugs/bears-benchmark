package org.jsapar.model;

import java.time.ZonedDateTime;

/**
 */
public class ZonedDateTimeCell extends Cell<ZonedDateTime>{

    /**
     * Creates a cell with a name and value.
     *
     * @param name     The name of the cell
     * @param value    The value of the cell
     */
    public ZonedDateTimeCell(String name, ZonedDateTime value) {
        super(name, value, CellType.ZONED_DATE_TIME);
    }

    @Override
    public int compareValueTo(Cell<ZonedDateTime> right) {
        return this.getValue().compareTo(right.getValue());
    }

    public static Cell emptyOf(String name) {
        return new EmptyCell(name, CellType.ZONED_DATE_TIME);
    }
}
