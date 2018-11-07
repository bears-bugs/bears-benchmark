package org.jsapar.model;

import java.util.Date;

/**
 * {@link Cell} implementation carrying a date value of a cell.
 * 
 */
public class DateCell extends ComparableCell<Date> {

    /**
     * 
     */
    private static final long serialVersionUID = -4950587241666521775L;

    public DateCell(String sName, Date value) {
        super(sName, value, CellType.DATE);
    }

    public static Cell emptyOf(String name) {
        return new EmptyCell(name, CellType.DATE);
    }
}
