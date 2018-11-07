package org.jsapar.model;

/**
 * {@link Cell} implementation carrying a boolean value of a cell.
 * 
 */
public class BooleanCell extends ComparableCell<Boolean>  {

	/**
     * 
     */
	private static final long serialVersionUID = -6337207320287960296L;


	public BooleanCell(String sName, Boolean value) {
		super(sName, value, CellType.BOOLEAN);
	}

	/**
	 * @param name The name of the empty cell.
	 * @return A newly created empty cell of type boolean with supplied name.
	 */
	public static Cell emptyOf(String name) {
		return new EmptyCell(name, CellType.BOOLEAN);
	}
}
