package org.jsapar.schema;

import org.jsapar.model.CellType;

import java.util.Locale;

/**
 * Describes the schema for a specific csv cell.
 */
@SuppressWarnings("WeakerAccess")
public class CsvSchemaCell extends SchemaCell {

    /**
     * The quote behavior for the cell when composing. Default is {@link QuoteBehavior#AUTOMATIC}. Not used while parsing.
     */
    private QuoteBehavior quoteBehavior = QuoteBehavior.AUTOMATIC;

    /**
     * The maximum number of characters that are read or written to/from the cell. Input and output
     * value will be silently truncated to this length. If you want to get an error when field is to
     * long, use the format regexp pattern instead.
     * <p>
     * A negative value indicates that max length will not be checked.
     */
    private int maxLength = -1;

    /**
     * Creates a CSV string cell with specified name. The format can be added after creation by using the {@link #setCellFormat(CellType, String)} method.
     *
     * @param sName The name of the cell.
     */
    public CsvSchemaCell(String sName) {
        super(sName);
    }

    /**
     * Creates a CSV schema cell with the specified name and format parameters.
     *
     * @param sName   The name of the cell.
     * @param type    The type of the cell.
     * @param pattern The pattern to use while formatting and parsing. The pattern has different meaning depending on the type of the cell.
     * @param locale  The locale to use while formatting and parsing dates and numbers that are locale specific. If null, US locale is used.
     */
    public CsvSchemaCell(String sName, CellType type, String pattern, Locale locale) {
        super(sName, type, pattern, locale);
    }

    public CsvSchemaCell(String name, CellType type) {
        super(name, type);
    }

    @Override
    public CsvSchemaCell clone() {
        return (CsvSchemaCell) super.clone();
    }

    public int getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return True if maxLength should be considered.
     */
    public boolean isMaxLength() {
        return this.maxLength > 0;
    }

    public QuoteBehavior getQuoteBehavior() {
        return quoteBehavior;
    }
    public void setQuoteBehavior(QuoteBehavior quoteBehavior) {
        this.quoteBehavior = quoteBehavior;
    }
}
