package org.jsapar.model;

/**
 */
public class ComparableCell<T extends Comparable<T>> extends Cell<T> implements Comparable<ComparableCell<T> > {

    /**
     * Creates a cell with a name and value.
     *
     * @param name     The name of the cell
     * @param value    The value of the cell
     * @param cellType The type of the cell.
     */
    public ComparableCell(String name, T value, CellType cellType) {
        super(name, value, cellType);
    }

    @Override
    public int compareValueTo(Cell<T> right) {
        return this.getValue().compareTo(right.getValue());
    }

    @Override
    public int compareTo(ComparableCell<T> right) {
        return this.getValue().compareTo(right.getValue());
    }

}
