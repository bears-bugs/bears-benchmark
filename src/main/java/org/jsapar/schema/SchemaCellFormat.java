package org.jsapar.schema;

import org.jsapar.model.CellType;
import org.jsapar.parse.cell.CellFactory;

import java.text.Format;
import java.util.Locale;

/**
 * Describes the format of a cell when converted to or from text, including what data type that the cell is expected
 * to have.
 * <p>
 * When creating an instance of this class you may choose to add a {@link java.text.Format} or a pattern and optionally a
 * {@link java.util.Locale}. The pattern has different meaning depending on the data type:
 * <ul>
 * <li>When the data type is of type String, the pattern is a regular expression as described in {@link java.util.regex.Pattern} to validate against.</li>
 * <li>When the data type is of a numerical type, the pattern is the same as described in {@link java.text.DecimalFormat}</li>
 * <li>If the type is boolean, the pattern should contain the true and false values separated with a ; character.
 * Example: pattern="Y;N" will imply that Y represents true and N to represents false.
 * Comparison while parsing is not case sensitive.
 * Multiple true or false values can be specified, separated with the | character but the first value is always the
 * one used while composing. Example: pattern="Y|YES;N|NO"</li>
 * <li>If the type is of a date or time type, the pattern should be described according to {@link java.text.SimpleDateFormat}</li>
 * </ul>
 */
public class SchemaCellFormat implements Cloneable {
    public final static Locale defaultLocale= Locale.US;

    private final CellType cellType;
    private final Format format;
    private final String pattern;


    /**
     * Creates a new cell format object of supplied type.
     * @param cellType The expected data type of the cell.
     */
    public SchemaCellFormat(CellType cellType) {
        this.cellType = cellType;
        this.format = null;
        this.pattern = null;
    }

    /**
     * Creates a schema cell format object with an already created format.
     * @param cellType The type of the cell
     * @param format The format class to use.
     */
    public SchemaCellFormat(CellType cellType, Format format) {
        this.cellType = cellType;
        this.format = format;
        this.pattern = null;
    }

    /**
     * Creates a new cell format object of supplied type and pattern.
     * @param cellType The type of the cell.
     * @param pattern The pattern of the cell. See class documentation.
     */
    public SchemaCellFormat(CellType cellType, String pattern) {
        this(cellType, pattern, defaultLocale);
    }

    /**
     * Creates a new cell format object of supplied type, pattern and locale.
     * @param cellType The type of the cell.
     * @param pattern The pattern of the cell. See class documentation.
     * @param locale   The locale determines for instance how decimal separator should be formatted etc.
     */
    public SchemaCellFormat(CellType cellType, String pattern, Locale locale)  {
        this.cellType = cellType;
        this.pattern = pattern;
        if (pattern == null) {
            this.format = null;
            return;
        }
        this.format = CellFactory.getInstance(cellType).makeFormat(locale, pattern);
    }


    /**
     * @return the cellType
     */
    public CellType getCellType() {
        return cellType;
    }

    /**
     * @return the format
     */
    public java.text.Format getFormat() {
        return format;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CellType=");
        sb.append(this.cellType);
        if (this.format != null) {
            sb.append(", Format={");
            sb.append(this.format);
            sb.append("}");
        }
        return sb.toString();
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellType == null) ? 0 : cellType.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SchemaCellFormat)) {
            return false;
        }
        SchemaCellFormat other = (SchemaCellFormat) obj;
        if (cellType == null) {
            if (other.cellType != null) {
                return false;
            }
        } else if (!cellType.equals(other.cellType)) {
            return false;
        }
        if (format == null) {
            if (other.format != null) {
                return false;
            }
        } else if (!format.equals(other.format)) {
            return false;
        }
        if (pattern == null) {
            return other.pattern == null;
        } else
            return pattern.equals(other.pattern);
    }

    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override
    protected SchemaCellFormat clone() {
        try {
            return (SchemaCellFormat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Should never happen");
        }
    }
}
