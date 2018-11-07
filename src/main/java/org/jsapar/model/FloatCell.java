/** 
 * Copyright: Jonas Stenberg
 */
package org.jsapar.model;

/**
 * Float cell contains a double precision float number. Single precision float
 * values are converted into double precision values.
 * 
 */
public class FloatCell extends NumberCell implements Comparable<FloatCell> {

    private static final long serialVersionUID = 2102712515168714171L;

    /**
     * Creates a float number cell with supplied name. Converts the float value
     * into a double precision float value.
     * 
     * @param name The name of the cell
     * @param value The value
     */
    public FloatCell(String name, Float value) {
	super(name, value, CellType.FLOAT);
    }

    /**
     * Creates a float number cell with supplied name.
     * 
     * @param name The name of the cell
     * @param value The value
     */
    public FloatCell(String name, Double value) {
	super(name, value, CellType.FLOAT);
    }


    @Override
    public int compareTo(FloatCell right) {
	return Double.compare(this.getValue().doubleValue(), right
		.getValue().doubleValue());
    }
}
