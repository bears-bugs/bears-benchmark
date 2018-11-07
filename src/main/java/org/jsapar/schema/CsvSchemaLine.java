package org.jsapar.schema;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Describes the schema for a delimiter separated line. For instance if you want to ignore a header line you
 * can add a SchemaLine instance to your schema with occurs==1 and ignoreRead==true;
 * @see CsvSchema
 * @see CsvSchemaCell
 */
public class CsvSchemaLine extends SchemaLine {

    private Map<String, CsvSchemaCell> schemaCells       = new LinkedHashMap<>();

    /**
     * Set this to true when the first line of the input text contains the names and order of the cells in the following
     * lines, i.e. the first line is a header line that works as a schema for the rest of the file. In that case this
     * schema line instance will only be used to get
     * formatting instructions and default values, it will not denote the order of the cells. The order is given by the
     * first line of the input instead.
     */
    private boolean                    firstLineAsSchema = false;

    /**
     * The character sequence that separates each cell. Default is the ';' (semicolon) character.
     */
    private String cellSeparator = ";";

    /**
     * Specifies quote characters used to encapsulate cells. Numerical value 0 indicates that quotes are not used.
     * <p>
     * If quoted cells contain cell separator or line separator characters, these will be treated as content of the cell
     * instead.
     */
    private char quoteChar = 0;

    /**
     * Creates an empty schema line.
     */
    public CsvSchemaLine() {
        super();
    }

    /**
     * Creates an empty schema line which occurs nOccurs number of times.
     *
     * @param nOccurs The number of times this type of line occurs in the input.
     */
    public CsvSchemaLine(int nOccurs) {
        super(nOccurs);
    }

    /**
     * The type of the line. You could say that this is the class of the line.
     *
     * @param lineType The type of the line
     */
    public CsvSchemaLine(String lineType) {
        super(lineType);
    }

    /**
     * Creates a CsvSchemaLine with the supplied line type and occurs supplied number of times.
     * @param lineType The type of the line
     * @param nOccurs The number of times this type of line occurs in the input/output. Use {@link #OCCURS_INFINITE} constant for infinite number of times.
     */
    public CsvSchemaLine(String lineType, int nOccurs) {
        super(lineType, nOccurs);
    }

    /**
     * @return the cells
     */
    public Collection<CsvSchemaCell> getSchemaCells() {
        return schemaCells.values();
    }

    /**
     * Adds a schema cell to this row.
     *
     * @param cell The cell to add
     * @return This instance of the schema line, allows to chain calls.
     */
    public CsvSchemaLine addSchemaCell(CsvSchemaCell cell) {
        this.schemaCells.put(cell.getName(), cell);
        return this;
    }

    public String getCellSeparator() {
        return cellSeparator;
    }

    /**
     * Sets the character sequence that separates each cell.
     * <p>
     * In output schemas the non-breaking space character '\u00A0' is not allowed unless quote character is specified
     * since that character is used to replace any occurrence of the separator within each cell.
     *
     * @param cellSeparator the cellSeparator to set
     */
    public void setCellSeparator(String cellSeparator) {
        this.cellSeparator = cellSeparator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public CsvSchemaLine clone() {
        CsvSchemaLine line;
        line = (CsvSchemaLine) super.clone();
        line.schemaCells = new LinkedHashMap<>();

        for (CsvSchemaCell cell : this.schemaCells.values()) {
            line.addSchemaCell(cell.clone());
        }
        return line;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" cellSeparator='");
        sb.append(this.cellSeparator);
        sb.append("'");
        sb.append(" firstLineAsSchema=");
        sb.append(this.firstLineAsSchema);
        if (this.quoteChar != 0) {
            sb.append(" quoteChar=");
            sb.append(this.quoteChar);
        }
        sb.append(" schemaCells=");
        sb.append(this.schemaCells);
        return sb.toString();
    }

    public boolean isFirstLineAsSchema() {
        return firstLineAsSchema;
    }

    public void setFirstLineAsSchema(boolean firstLineAsSchema) {
        this.firstLineAsSchema = firstLineAsSchema;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    /**
     * @return true if quote character is used, false otherwise.
     */
    public boolean isQuoteCharUsed() {
        return this.quoteChar != 0;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsapar.schema.SchemaLine#getSchemaCell(java.lang.String)
     */
    @Override
    public SchemaCell getSchemaCell(String cellName) {
        return getCsvSchemaCell(cellName);
    }

    /**
     * @param cellName The name of the cell to get.
     * @return {@link CsvSchemaCell} with specified name that is attached to this line or null if no such {@link CsvSchemaCell} exist.
     */
    public CsvSchemaCell getCsvSchemaCell(String cellName) {
        return schemaCells.get(cellName);
    }

    @Override
    public int size() {
        return this.schemaCells.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CsvSchemaLine;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Stream<CsvSchemaCell> stream() {
        return schemaCells.values().stream();
    }

    public Iterator<CsvSchemaCell> iterator() {
        return schemaCells.values().iterator();
    }

    public void forEach(Consumer<? super SchemaCell> consumer) {
        schemaCells.values().forEach(consumer);
    }



}
