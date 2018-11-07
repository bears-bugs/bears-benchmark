package org.jsapar.model;

import org.jsapar.parse.cell.*;

/**
 * Enum used to be able to denote the type of an instance of a {@link Cell}.
 *
 */
public enum CellType {

    /**
     * Cell contains {@link String} content.
     */
    STRING(new StringCellFactory(), false, false),
    /**
     * Cell contains {@link java.util.Date} content
     */
    DATE(new DateCellFactory(), false, false),
    /**
     * Cell contains integer content, up to the size of a long.
     */
    INTEGER(new IntegerCellFactory(), true, true),
    /**
     * Cell contains boolean content.
     */
    BOOLEAN(new BooleanCellFactory(), true, false),
    /**
     * Cell contains floating point precision content up to the size of a double.
     */
    FLOAT(new FloatCellFactory(), false, true),
    /**
     * Cell contains {@link java.math.BigDecimal} or {@link java.math.BigInteger} content.
     */
    DECIMAL(new BigDecimalCellFactory(), false, true),
    /**
     * Cell contains single character content.
     */
    CHARACTER(new CharacterCellFactory(), true, false),
    /**
     * Cell contains custom content.
     */
    CUSTOM(null, false, false),
    /**
     * Cell contains {@link java.time.LocalDateTime} content.
     */
    LOCAL_DATE_TIME(new LocalDateTimeCellFactory(), false, false),
    /**
     * Cell contains {@link java.time.LocalDate} content.
     */
    LOCAL_DATE(new LocalDateCellFactory(), false, false),
    /**
     * Cell contains {@link java.time.LocalTime} content.
     */
    LOCAL_TIME(new LocalTimeCellFactory(), false, false),
    /**
     * Cell contains {@link java.time.ZonedDateTime} content.
     */
    ZONED_DATE_TIME(new ZonedDateTimeCellFactory(), false, false);

    private CellFactory cellFactory;
    private boolean atomic;
    private boolean number;

    CellType(CellFactory cellFactory, boolean atomic, boolean number) {
        this.cellFactory = cellFactory;
        this.atomic = atomic;
        this.number = number;
    }

    public CellFactory getCellFactory() {
        return cellFactory;
    }

    /**
     * @return True if this type is atomic in a sense that it cannot contain cell or line separator character.
     */
    public boolean isAtomic() {
        return atomic;
    }

    /**
     * @return True if this type is numeric, that is if the content can be seen as a sub type of {@link Number}
     */
    public boolean isNumber(){
        return number;
    }
}
