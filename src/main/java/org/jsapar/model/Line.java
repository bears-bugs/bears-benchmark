package org.jsapar.model;

import org.jsapar.parse.CellParseException;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * A line is one row of the input buffer. Each line contains a list of cells. Cells within the line can be retrieved
 * by name. When building a line by parsing an input and using a {@link org.jsapar.schema.Schema}, the name of a cell
 * will be the same as the name of the corresponding {@link org.jsapar.schema.SchemaCell} and the name of a line
 * will be the same as the name or the corresponding {@link org.jsapar.schema.SchemaLine}.
 * <p>
 * Note that the class is not synchronized internally. If
 * multiple threads access the same instance, external synchronization is required.
 * <p>
 * In order to make it easier to retrieve and alter cell values within a {@link org.jsapar.model.Line}, you may use the {@link org.jsapar.model.LineUtils} class.
 * @see LineUtils
 * @see Cell
 * @see Document
 */
@SuppressWarnings("WeakerAccess")
public class Line implements Serializable, Cloneable, Iterable<Cell> {

    private static final long   serialVersionUID = 6026541900371948403L;

    private Map<String, Cell> cells;
    private Map<String, CellParseException> cellErrors = new LinkedHashMap<>();

    /**
     * Line type.
     */
    private final String lineType;

    /**
     * Assigned when parsing to the line number of the input source. Used primarily for logging and tracking. Has no
     * significance when composing. First line has lineNumber=1. Has the value 0 if not assigned.
     */
    private long lineNumber = 0L;

    /**
     * Creates an empty line of a specified type, without any cells but with an initial capacity.
     *
     * @param sLineType The type of the line.
     */
    public Line(String sLineType) {
        cells = new LinkedHashMap<>();
        lineType = sLineType;
    }

    /**
     * Creates an empty line of a specified type, without any cells but with an initial capacity.
     *
     * @param sLineType       The type of the line.
     * @param initialCapacity The initial capacity. Used only to reserve space. If capacity is exceeded, the
     *                        capacity grows automatically.
     */
    public Line(String sLineType, int initialCapacity) {
        lineType = sLineType;
        cells = new LinkedHashMap<>(initialCapacity);
    }

    /**
     * Returns a clone of the internal collection that contains all the cells.
     * For better performance while iterating multiple lines, it is better to use the
     * {@link #iterator()} or the {@link #stream()} method.
     *
     * @return A shallow clone of the internal collection that contains all the cells of this line.
     * Altering the returned collection will not alter the original collection of the this
     * Line but altering one of its cells will alter the cell within this line.
     * @see #iterator()
     * @see #stream()
     */
    public List<Cell> getCells() {
        return new ArrayList<>(cells.values());
    }

    /**
     * Returns an iterator that will iterate all the cells of this line.
     *
     * @return An iterator that will iterate all the cells of this line.
     */
    @Override
    public Iterator<Cell> iterator() {
        return cells.values().iterator();
    }

    /**
     * Adds a cell to the end of the line. Requires that there is not already a cell with the same name within this
     * line. Use method {@link #putCell(Cell)} instead if you want the new cell to replace any existing cell with the same name.
     *
     * @param cell The cell to add
     * @throws IllegalStateException if cell with the same name already exist. Use method replaceCell() instead if you
     *                               want existing cells with the same name to be replaced instead.
     * @return This line. Makes it possible to chain calls to addCell.
     * @see #putCell(Cell)
     */
    public Line addCell(Cell cell) {
        Cell oldCell = cells.get(cell.getName());
        if (oldCell != null)
            throw new IllegalStateException(
                    "A cell with the name '" + cell.getName() + "' already exists. Failed to add cell.");
        this.cells.put(cell.getName(), cell);
        return this;
    }

    /**
     * Removes cell with the given name.
     *
     * @param sName The name of the cell to remove.
     * @return Optional that contains the removed cell if found
     */
    public Optional<Cell> removeCell(String sName) {
        return Optional.ofNullable(this.cells.remove(sName));
    }

    /**
     * Adds a cell to the line, replacing any existing cell with the same name.
     *
     * @param cell The cell to add
     * @return Optional containing the replaced cell if there was one within the line with the same name.
     * @see #addCell(Cell)
     */
    public Optional<Cell> putCell(Cell cell) {
        return Optional.ofNullable(this.cells.put(cell.getName(), cell));
    }

    /**
     * Adds an object cell created with the cellCreator to the line, replacing any existing cell with the same name.
     * If supplied value is null, any existing cell will be removed and no new cell will be added.
     *
     * @param cellName The name of the cell to add.
     * @param value The value of the cell to add.
     * @param cellCreator {@link BiFunction} that takes cell name and values as parameters and creates the cell to add.
     * @param <T> The value type of the cell.
     *           @see #addCell(Cell)
     */
    public <T> void putCellValue(String cellName, T value, BiFunction<String, T, Cell> cellCreator) {
        if (value == null)
            this.cells.remove(cellName);
        else
            this.cells.put(cellName, cellCreator.apply(cellName, value));
    }

    /**
     * Gets a cell with specified name. Name is specified by the schema.
     *
     * @param name The name of the cell to get
     * @return Optional cell that is set if there is a cell with specified name.
     */
    public Optional<Cell> getCell(String name) {
        return Optional.ofNullable(this.cells.get(name));
    }

    /**
     * Gets a cell with specified name. Name is specified by the schema.

     * @param name The name of the cell to get
     * @return A cell with the supplied name
     * @throws IllegalStateException if there is no cell with the specified name.
     */
    public Cell getExistingCell(String name) {
        return getCell(name).orElseThrow(()->new IllegalStateException("The line does not contain any cell with name " + name + " This happens when the parsing schema does not contain any cell with that name."));
    }

    /**
     * Gets a non empty cell with specified name. Name is specified by the schema.
     *
     * @param name The name of the cell to get
     * @return Optional cell that is set if there is a cell with specified name and that cell is not empty.
     */
    public Optional<Cell> getNonEmptyCell(String name) {
        return getCell(name).filter(c->!c.isEmpty());
    }
    /**
     * Gets the number of cells that this line contains.
     *
     * @return the number of cells that this line contains.
     */
    public int size() {
        return this.cells.size();
    }

    /**
     * Returns the type of this line. The line type attribute is primarily used when parsing lines
     * of different types, distinguished by a control cell.
     *
     * @return the lineType or an empty string if no line type has been set.
     */
    public String getLineType() {
        return lineType;
    }


    /**
     * Returns a string representation of the line that can be used for debugging purposes.
     * @return A string representation of the line that can be used for debugging purposes.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.lineType != null && this.lineType.length() > 0) {
            sb.append("Line type=[");
            sb.append(this.lineType);
            sb.append("]");
        }
        if (this.getLineNumber() > 0) {
            sb.append(" Line number=");
            sb.append(this.lineNumber);
        }
        sb.append(" Cells: ");
        sb.append(this.cells.values());
        return sb.toString();
    }

    /**
     * Checks if there is a cell with the specified name.
     *
     * @param cellName The name of the cell to check.
     * @return True if there is a cell with the specified name, false otherwise.
     */
    boolean isCell(String cellName) {
        return this.getCell(cellName).isPresent();
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return A clone of this line. Since all cell values are final, only shallow copy is needed of all the cells.
     */
    @Override
    public Line clone() {
        Line clone;
        try {
            clone = (Line) super.clone();
        } catch (CloneNotSupportedException e) {
            // This should never happen.
            throw new AssertionError(e);
        }

        // No need to make a deep copy since cells are all final.
        clone.cells = new LinkedHashMap<>(this.cells);
        clone.cellErrors  = new LinkedHashMap<>(this.cellErrors);

        return clone;
    }

    /**
     * @param error The cell error to add.
     */
    public void addCellError(CellParseException error) {
        this.cellErrors.put(error.getCellName(), error);
    }

    /**
     * @return True if the line has errors on any of the cells.
     */
    public boolean hasCellErrors(){
        return !this.cellErrors.isEmpty();
    }

    /**
     * @param cellName The name of the cell to get error for.
     * @return If there is an error for the given cell name, that error is returned.The error with the given cell name.
     */
    public Optional<CellParseException> getCellError(String cellName){
        return Optional.ofNullable(cellErrors.get(cellName));
    }

    /**
     * All cell errors of this line.
     * @return All cell errors of this line.
     */
    public Collection<CellParseException> getCellErrors(){
        return cellErrors.values();
    }

    /**
     * Checks if there is a cell with the specified name and if it is not empty.
     *
     * @param cellName The name of the cell to check.
     * @return true if the cell with the specified name exists and that it contains a value. False otherwise.
     */
    public boolean isCellSet(String cellName) {
        return getCell(cellName)
                .filter(cell->!cell.isEmpty())
                .isPresent();
    }

    /**
     * Checks if there is a cell with the specified name and type and that is not empty.
     *
     * @param cellName The name of the cell to check.
     * @param type     The type to check.
     * @return true if the cell with the specified name contains a value of the specified type.
     */
    public boolean containsNonEmptyCell(String cellName, CellType type) {
        return getCell(cellName)
                .filter(cell->!cell.isEmpty())
                .filter(cell1 -> cell1.getCellType().equals(type))
                .isPresent();
    }

    /**
     * Returns a stream of all cells within this line.
     * @return A stream of all cells within this line.
     */
    public Stream<Cell> stream() {
        return this.cells.values().stream();
    }

    /**
     * Allows scripting languages such as Groovy to access cell values with simple . notation.
     * @param cellName The name of the cell to get
     * @return The cell value in its native type.
     */
    public Object getProperty(String cellName){
        return getCell(cellName).map(Cell::getValue).orElse(null);
    }

}
