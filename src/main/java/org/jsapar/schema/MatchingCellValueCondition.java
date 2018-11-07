package org.jsapar.schema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link CellValueCondition} that matches based on a regex expression. The whole expression needs to match.
 * @see Pattern
 */
public class MatchingCellValueCondition implements CellValueCondition {
    private final Pattern pattern;

    /**
     * Creates a condition based on regular expression.
     * @param regex A string regular expression to match against.
     * @see Pattern
     */
    public MatchingCellValueCondition(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean satisfies(String value) {
        Matcher m = pattern.matcher(value);
        return m.matches();
    }

    /**
     * @return The string regular expression to match against
     */
    public String getPattern() {
        return pattern.pattern();
    }
}
