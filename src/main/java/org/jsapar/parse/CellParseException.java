package org.jsapar.parse;

import org.jsapar.schema.SchemaCellFormat;

import java.text.ParseException;

/**
 * This class is used as a way for the parser to report back parsing errors. The
 * class contains error information about a cell that failed to parse.
 *
 */
public final class CellParseException extends LineParseException {

    private final String           cellName;
    private final String           cellValue;
    private final SchemaCellFormat cellFormat;

    /**
     * Creates a new cell parsing exception
     * @param lineNumber The line number where the error occurred.
     * @param cellName The cell name where the error occurred.
     * @param cellValue The cell value that caused the error.
     * @param cellFormat Expected cell format. Can be null.
     * @param errorDescription Description of the error.
     */
    public CellParseException(long lineNumber,
                              String cellName,
                              String cellValue,
                              SchemaCellFormat cellFormat,
                              String errorDescription) {
        super(lineNumber, errorDescription);
        this.cellName = cellName;
        this.cellValue = cellValue;
        this.cellFormat = cellFormat;
    }

    /**
     * Creates a new cell parsing exception
     * @param cellName The cell name where the error occurred.
     * @param cellValue The cell value that caused the error.
     * @param cellFormat Expected cell format. Can be null.
     * @param errorDescription Description of the error.
     */
    public CellParseException(String cellName, String cellValue, SchemaCellFormat cellFormat, String errorDescription) {
        super(0, errorDescription);
        this.cellName = cellName;
        this.cellValue = cellValue;
        this.cellFormat = cellFormat;
    }

    public CellParseException(String cellName, String value, SchemaCellFormat cellFormat, Throwable cause) {
        super(0, cause);
        this.cellName = cellName;
        this.cellValue = value;
        this.cellFormat = cellFormat;
    }


    /**
     * @return the cellName
     */
    public String getCellName() {
        return cellName;
    }

    /**
     * @return the cellValue
     */
    public String getCellValue() {
        return cellValue;
    }

    /**
     * @return the cellFormat
     */
    public SchemaCellFormat getCellFormat() {
        return cellFormat;
    }

    /**
     * @return A simple message describing the error and it's location.
     */
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if(this.getLineNumber()>0) {
            sb.append("Line=");
            sb.append(this.getLineNumber());
            sb.append(' ');
        }
        sb.append("Cell='");
        sb.append(this.cellName);
        sb.append("'");
        sb.append(" Value='");
        sb.append(this.cellValue);
        sb.append("'");
        if (cellFormat != null) {
            sb.append(" Expected: ");
            sb.append(this.cellFormat);
        }
        sb.append(" - ");
        sb.append(super.getOriginalMessage());
        return sb.toString();
    }

 }
