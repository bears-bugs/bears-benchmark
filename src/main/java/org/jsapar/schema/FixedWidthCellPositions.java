package org.jsapar.schema;

/**
 * Contains positions within a line for a fixed with cell. The "first" position of the first cell is
 * 1. The "last" position of the first cell is the same as the length of the cell.
 * 
 *
 */
public class FixedWidthCellPositions {
    private int first=0;
    private int last=0;

    FixedWidthCellPositions() {
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first
     *            the first to set
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * @return the last
     */
    public int getLast() {
        return last;
    }

    /**
     * @param last
     *            the last to set
     */
    public void setLast(int last) {
        this.last = last;
    }
    
    /**
     * Increments the positions with one cell.
     * @param cell The cell to increment with
     */
    void increment(FixedWidthSchemaCell cell){
        first = last+1;
        last += cell.getLength();
    }

}
