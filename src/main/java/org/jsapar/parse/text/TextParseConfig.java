package org.jsapar.parse.text;

import org.jsapar.error.ValidationAction;

/**
 * Configuration that controls behavior while parsing text.
 * <p>
 * Created by stejon0 on 2016-07-12.
 */
public class TextParseConfig {

    /**
     * The action to take if the cell value conditions of the line does not match any of the defined line types
     * within the schema. Default is to throw exception.
     */
    private ValidationAction onUndefinedLineType = ValidationAction.EXCEPTION;
    /**
     * The action to take if there is insufficient input to build a complete line. For Csv, this happens for
     * instance when there are too few columns. For fixed width files this happens when the line is too short.
     * Default is no action.
     */
    private ValidationAction onLineInsufficient  = ValidationAction.NONE;

    /**
     * The action to take if there is too much data in the input after parsing a complete line. For Csv, this happens for
     * instance when there are too many columns. For fixed width files this happens when the line is too long compared to what is defined in the schema.
     * Default is no action.
     */
    private ValidationAction onLineOverflow = ValidationAction.NONE;

    /**
     * The maximum number of cell values that are cached while parsing. 0 or negative value means that caching is disabled. For inputs
     * where each cell on each line contains a unique value, you may gain some speed by disabling the cache.
     * <p>
     * The default max cache size is 10 and the maximum value is 100. Setting a higher value will not have any further
     * effect.
     * <p>
     * For each column or schema-cell the library knows that a distinct string value will always result in exactly the same cell value.
     * <p>
     * For example:
     * <ul>
     * <li>The string "2018-09-29" in a local-date cell will always be parsed as the same date and with a cell with the same name.</li>
     * <li>The string "true" in a boolean cell will always be parsed to a cell with the same boolean value.</li>
     * </ul>
     * This means that the library can cache the last x number of read cell string values and map them to what cell was
     * created and re-use that instance. The cell is immutable so it cannot be altered after creation.
     * <p>
     * In a normal scenario when parsing fixed width or CSV-files, many columns contain only a fix number of values. For
     * those scenarios the library only have to create one new cell instance every occurring value as long as it hits
     * the cache. It reduces a lot of calls to new.
     * <p>
     * As usual when caching; some will gain and some will lose. In this case we gain a lot for columns where value does
     * not change a lot but loose some for columns where each value is unique.
     */
    private int maxCellCacheSize = 10;

    public ValidationAction getOnUndefinedLineType() {
        return onUndefinedLineType;
    }

    public void setOnUndefinedLineType(ValidationAction onUndefinedLineType) {
        this.onUndefinedLineType = onUndefinedLineType;
    }

    public ValidationAction getOnLineInsufficient() {
        return onLineInsufficient;
    }

    public void setOnLineInsufficient(ValidationAction onLineInsufficient) {
        this.onLineInsufficient = onLineInsufficient;
    }

    public ValidationAction getOnLineOverflow() {
        return onLineOverflow;
    }

    public void setOnLineOverflow(ValidationAction onLineOverflow) {
        this.onLineOverflow = onLineOverflow;
    }

    public int getMaxCellCacheSize() {
        return maxCellCacheSize;
    }

    public void setMaxCellCacheSize(int maxCellCacheSize) {
        this.maxCellCacheSize = Math.max(maxCellCacheSize, 100);
    }
}
