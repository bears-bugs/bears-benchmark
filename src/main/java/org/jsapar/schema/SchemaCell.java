package org.jsapar.schema;

import org.jsapar.model.Cell;
import org.jsapar.model.CellType;
import org.jsapar.model.EmptyCell;
import org.jsapar.parse.cell.CellParser;

import java.util.Locale;

/**
 * Denotes the structure of one cell/column. Describes how to parse input text into a {@link Cell } or to compose a {@link Cell } to a text
 * output.
 * @see SchemaLine
 * @see Schema
 */
public abstract class SchemaCell implements Cloneable {

    private final static SchemaCellFormat CELL_FORMAT_PROTOTYPE = new SchemaCellFormat(CellType.STRING);

    private final String                  name;
    private SchemaCellFormat              cellFormat            = CELL_FORMAT_PROTOTYPE;
    private boolean                       ignoreRead            = false;
    private boolean                       ignoreWrite           = false;
    private boolean                       mandatory             = false;
    private Cell                          minValue              = null;
    private Cell                          maxValue              = null;

    /**
     * If parsing an empty value this cell can be used, avoiding a lot of object creation.
     */
    private Cell               emptyCell      = null;
    private String             defaultValue   = null;
    private Locale             locale         = Locale.US;
    private CellValueCondition emptyCondition = null;
    private CellValueCondition lineCondition  = null;

    /**
     * Creates a string cell with specified name. The format can be added after creation by using the {@link #setCellFormat(CellType, String)} method.
     * @param sName The name of the cell.
     */
    public SchemaCell(String sName) {
        this(sName, CELL_FORMAT_PROTOTYPE);
    }

    /**
     * Creates a schema cell with the specified name and format parameters.
     * @param sName The name of the cell.
     * @param type The type of the cell.
     */
    public SchemaCell(String sName, CellType type) {
        this(sName, new SchemaCellFormat(type));
    }

    /**
     * Creates a schema cell with the specified name and format parameters.
     * @param sName The name of the cell.
     * @param type The type of the cell.
     * @param pattern The pattern to use while formatting and parsing. The pattern has different meaning depending on the type of the cell.
     * @param locale The locale to use while formatting and parsing dates and numbers that are locale specific. If null, US locale is used.
     */
    public SchemaCell(String sName, CellType type, String pattern, Locale locale) {
        this(sName, new SchemaCellFormat(type, pattern, locale));
        this.locale=(locale != null) ? locale : Locale.US;
    }

    protected SchemaCell(String sName, SchemaCellFormat cellFormat) {
        if(sName == null || sName.isEmpty())
            throw new IllegalArgumentException("SchemaCell.name cannot be null or empty.");
        this.cellFormat = cellFormat;
        this.name = sName;
        this.emptyCell = new EmptyCell(sName, cellFormat.getCellType());
    }

    /**
     * Indicates if this cell should be ignored after reading it from the buffer. If ignoreRead is
     * true the cell will not be stored to the current Line object.
     * 
     * @return the ignoreRead
     */
    public boolean isIgnoreRead() {
        return ignoreRead;
    }

    /**
     * @param ignoreRead
     *            Indicates if this cell should be ignored after reading it from the buffer. If
     *            ignoreRead is true the cell will not be stored to the current Line object.
     */
    public void setIgnoreRead(boolean ignoreRead) {
        this.ignoreRead = ignoreRead;
    }


    /**
     * @return true if the cell should be ignored while writing.
     */
    public boolean isIgnoreWrite() {
        return ignoreWrite;
    }

    /**
     * @param ignoreWrite If true, this cell will be blank while writing. 
     */
    public void setIgnoreWrite(boolean ignoreWrite) {
        this.ignoreWrite = ignoreWrite;
    }
    
    /**
     * The name of the cell
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @return the cellFormat
     */
    public SchemaCellFormat getCellFormat() {
        return cellFormat;
    }


    /**
     * Sets format of this schema cell using a type that needs no format pattern
     * @param cellType The type of the cell
     */
    public void setCellFormat(CellType cellType)  {
        this.setCellFormat(new SchemaCellFormat(cellType));
    }

    /**
     * Sets format of this schema cell using the locale of the cell.
     * @param cellType The type of the cell
     * @param sPattern The pattern
     */
    public void setCellFormat(CellType cellType, String sPattern)  {
        this.setCellFormat(new SchemaCellFormat(cellType, sPattern, locale));
    }

    /**
     * Sets format of this schema cell.
     * @param cellType The type of the cell
     * @param sPattern The pattern
     * @param locale The locale to use for the cell.
     */
    public void setCellFormat(CellType cellType, String sPattern, Locale locale)  {
        this.locale = locale;
        this.setCellFormat(new SchemaCellFormat(cellType, sPattern, locale));
    }

    /**
     * @param cellFormat
     *            the cellFormat to set
     */
    private void setCellFormat(SchemaCellFormat cellFormat) {
        if(cellFormat == null)
            throw new IllegalArgumentException("cellFormat argument cannot be null");
        this.cellFormat = cellFormat;
    }




    /**
     * Indicates if the corresponding cell is mandatory, that is an error will be reported if it
     * does not exist while parsing.
     * 
     * @return true if the cell is mandatory, false otherwise.
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @param mandatory
     *            Indicates if the corresponding cell is mandatory, that is an error will be
     *            reported if it does not exist while parsing.
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public SchemaCell clone() {
        try {
            return (SchemaCell) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SchemaCell name='");
        sb.append(this.name);
        sb.append("'");
        if (this.cellFormat != null) {
            sb.append(" cellFormat=");
            sb.append(this.cellFormat);
        }
        if (this.defaultValue != null) {
            sb.append(" defaultValue=");
            sb.append(this.defaultValue);
        }

        if (this.ignoreRead)
            sb.append(" IGNOREREAD");
        if (this.mandatory)
            sb.append(" MANDATORY");
        return sb.toString();
    }

    /**
     * @return the maxValue
     */
    public Cell getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue
     *            the maxValue to set
     */
    public void setMaxValue(Cell maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public Cell getMinValue() {
        return minValue;
    }

    /**
     * @param minValue
     *            the minValue to set
     */
    public void setMinValue(Cell minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale) {
        // Re-create the format since it may change depending on that locale changed.
        this.setCellFormat(cellFormat.getCellType(), cellFormat.getPattern(), locale);
    }

    /**
     * Validates that the default value is within the valid range. Throws a SchemaException if value is
     * not within borders.
     *
     * @throws SchemaException If validation fails.
     */
    @SuppressWarnings("unchecked")
    private void validateDefaultValueRange(Cell defaultCell) throws SchemaException {
        if (this.minValue != null && defaultCell.compareValueTo(this.minValue) < 0) {
            throw new SchemaException("The value is below minimum range limit.");
        } else if (this.maxValue != null && defaultCell.compareValueTo(this.maxValue) > 0) {
            throw new SchemaException("The value is above maximum range limit.");
        }

    }

    /**
     * @param value The string representation of the min value as it would be presented in the text input.
     * @throws java.text.ParseException If the string value could not be parsed according to this schema cell
     */
    public void setMinValue(String value) throws java.text.ParseException {
        this.minValue = CellParser.makeCell(this.getCellFormat().getCellType(), this.name, value, this.locale);
    }

    /**
     * @param value The string representation of the max value as it would be presented in the text input.
     * @throws java.text.ParseException If the string value could not be parsed according to this schema cell
     */
    public void setMaxValue(String value) throws java.text.ParseException {
        this.maxValue = CellParser.makeCell(this.getCellFormat().getCellType(), this.name, value, this.locale);
    }

    /**
     * Sets the default value as a string. The default value have to be parsable according to the
     * schema format. As long as it is parsable, it will be used exactly as is even though it might
     * not look the same as if it was formatted from a value.
     * 
     * @param sDefaultValue
     *            The default value formatted according to this schema. Will be used if input/output
     *            is missing for this cell.
     * @throws SchemaException If there was a configuration error in the schema.
     */
    public void setDefaultValue(String sDefaultValue) throws SchemaException {
        this.defaultValue = sDefaultValue;
        validateDefaultValueRange(CellParser.ofSchemaCell(this).makeDefaultCell());
    }

    /**
     * @return The default value formatted according to this schema. Will be used if input/output is
     *         missing.
     */
    public String getDefaultValue() {
        return defaultValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SchemaCell))
            return false;

        SchemaCell that = (SchemaCell) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @return true if there is a default value, false otherwise.
     */
    public boolean isDefaultValue() {
        return this.defaultValue != null;
    }

    /**
     * @return True if this cell schema has an empty condition.
     */
    public boolean hasEmptyCondition(){
        return this.emptyCondition != null;
    }
    /**
     * @return the emptyCondition
     */
    public CellValueCondition getEmptyCondition() {
        return emptyCondition;
    }

    /**
     * A condition that if satisfied for a specific text input, indicates that the cell is actually empty. For instance
     * the cell might contain the text NULL to indicate that there is no value even though this is a date or a numeric
     * field. In that case a {@link MatchingCellValueCondition} can be used to match the pattern "NULL".
     * 
     * @param emptyCondition the cell value condition that needs to be satisfied if this cell is to be considered empty
     */
    public void setEmptyCondition(CellValueCondition emptyCondition) {
        this.emptyCondition = emptyCondition;
    }

    /**
     * A regular expression that if matching for a specific text input, indicates that the cell is actually empty. For instance
     * the cell might contain the text NULL to indicate that there is no value even though this is a date or a numeric
     * field. In that case the pattern "NULL" can be used.
     * @param pattern The regex pattern to match against
     */
    @SuppressWarnings("SameParameterValue")
    public void setEmptyPattern(String pattern) {
        this.emptyCondition = new MatchingCellValueCondition(pattern);
    }

    /**
     * @return A condition that needs to be satisfied if the parser is going to use this line type.
     */
    public CellValueCondition getLineCondition() {
        return lineCondition;
    }

    /**
     * @param lineCondition A condition that needs to be satisfied if the parser is going to use this line type.
     */
    public void setLineCondition(CellValueCondition lineCondition) {
        this.lineCondition = lineCondition;
    }

    /**
     * @return True if this cell schema has a line condition, false otherwise.
     */
    public boolean hasLineCondition(){
        return this.lineCondition != null;
    }

    /**
     * @return An empty cell for this schema cell.
     */
    public Cell makeEmptyCell(){
        return this.emptyCell;
    }

}
