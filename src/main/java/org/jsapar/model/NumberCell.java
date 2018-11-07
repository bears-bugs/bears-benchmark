package org.jsapar.model;

/**
 * Abstract base class for all type of cells that can be represented as a {@link Number}.
 * 
 */
public abstract class NumberCell extends Cell<Number> {

    /**
     * 
     */
    private static final long serialVersionUID = -2103478512589522630L;


    /**
     * @param name The name of the cell
     * @param value The value
     * @param cellType The type of the cell, from the sub-class.
     */
    NumberCell(String name, Number value, CellType cellType) {
        super(name, value, cellType);
    }


    /* (non-Javadoc)
     * @see org.jsapar.model.Cell#compareValueTo(org.jsapar.model.Cell)
     */
    @Override
    public int compareValueTo(Cell<Number> right)  {
        if(right instanceof BigDecimalCell){
            return -right.compareValueTo(this);
        }
        if(right instanceof NumberCell){
            Number nRight = right.getValue();
            double dLeft = getValue().doubleValue();
            double dRight = nRight.doubleValue();
            if(dLeft < dRight)
                return -1;
            else if(dLeft > dRight)
                return 1;
            else
                return 0;
        }
        else{
            throw new IllegalArgumentException("Value of cell of type " + getCellType() + " can not be compared to value of cell of type " + right.getCellType());
        }
    }

}
