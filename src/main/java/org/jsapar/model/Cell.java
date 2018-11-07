package org.jsapar.model;

import java.io.Serializable;
import java.text.Format;
import java.util.Objects;

/**
 * Base class which represents a parsable item on a line in the original document. A cell has a
 * name and a value. A cell can be only exist as one of the sub-classes of this class. The type of
 * the value denotes which sub-class to use.
 * 
 */
public abstract class Cell<T> implements Serializable {

    private final T value;

    private static final long serialVersionUID = -3609313087173019222L;

    /**
     * The name of the cell. Can be null if there is no name.
     */
    private final String name;

    /**
     * Denotes the type of the cell.
     */
    private final CellType cellType;

    /**
     * Since all members are final, we can cache the hash code when needed.
     */
    private volatile int hashCode = 0;

    /**
     * Creates a cell with a name.
     * 
     * @param name        The name of the cell
     * @param value       The value to set for this cell.
     * @param cellType    The type of the cell.
     */
    public Cell(String name, T value, CellType cellType) {
        assert name != null : "Cell name cannot be null.";
        assert cellType != null : "Cell type cannot be null.";
        assert value != null : "Cell value cannot be null, use EmptyCell for empty values.";
        this.name = name;
        this.cellType = cellType;
        this.value = value;
    }

    /**
     * Gets the name of the cell.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets a string representation of the value formatted as value.toString()
     * 
     * @return The value.
     */
    public String getStringValue() {
        return getValue().toString();
    }


    /**
     * @return The value of the cell.
     */
    public T getValue() {
        return value;
    }

    /**
     * @return A string representation of the cell, including the name of the cell, suitable for
     * debugging. Use the method {@link #getValue()} to get the real value of the cell or use {@link #getStringValue()}
     * to get a string representation of the real value.
     */
    @Override
    public String toString() {
        return this.name + "=" + getStringValue();
    }

    /**
     * @return the cellType
     */
    public CellType getCellType() {
        return cellType;
    }


    /**
     * Compares value of this cell with the value of the supplied cell. 
     * 
     * @param right The cell to compare to.
     * @return a negative integer, zero, or a positive integer as this cell's value is less than, equal to, or greater than the specified cell's value. 
     * @throws IllegalArgumentException if the value of provided cell cannot be compared to the value of this cell.
     */
    public abstract int compareValueTo(Cell<T> right);
    
    /**
     * @return true if the cell is not set to any value, false otherwise.
     */
    public boolean isEmpty(){
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell<?> cell = (Cell<?>) o;
        return Objects.equals(value, cell.value) &&
                Objects.equals(name, cell.name) &&
                cellType == cell.cellType;
    }

    @Override
    public int hashCode() {
        // Since all members are final, we can cache the hash code.
        if(this.hashCode == 0){
            this.hashCode = Objects.hash(value, name, cellType);
        }
        return this.hashCode;
    }

}
