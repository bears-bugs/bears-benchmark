package org.jsapar.schema;

/**
 * Interface for cell validation.
 */
public interface CellValueCondition {

    /**
     * @param value The string value to test.
     * @return True if the given string value satisfies the condition on this cell. False otherwise.
     */
    boolean satisfies(String value);

}
