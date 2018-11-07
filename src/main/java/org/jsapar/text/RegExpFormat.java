package org.jsapar.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.regex.Pattern;

/**
 * A formatter that while both parsing and formatting, checks that the value matches supplied regex pattern.
 *
 */
public class RegExpFormat extends Format {

    private static final long serialVersionUID = 650069334821232790L;
    private final Pattern pattern;

    /**
     * @param pattern The regex pattern to match while parsing and formatting.
     */
    public RegExpFormat(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks that the string value of the provided object matches the regex pattern
     * @param obj The object to format.
     * @param toAppendTo The destination buffer to append the value to.
     * @param pos Not used.
     * @return The destination buffer after appending the value.
     * @throws IllegalArgumentException if string representation of the provided object does not match the regex pattern.
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        String sValue = String.valueOf(obj);
        if (!this.pattern.matcher(sValue).matches())
            throw new IllegalArgumentException(
                    "Value [" + sValue + "] does not match regular expression [" + this.pattern.pattern() + "].");
        toAppendTo.append(sValue);
        return toAppendTo;
    }

    /* (non-Javadoc)
     * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        String sToParse = source.substring(pos.getIndex(), source.length());
        if (!this.pattern.matcher(source).matches()) {
            pos.setErrorIndex(pos.getIndex());
        } else
            pos.setIndex(source.length());
        return sToParse;
    }

    /**
     * @param source The string source to parse
     * @return The same string as provided as source, if regex pattern matches.
     * @throws ParseException If regex pattern does not match.
     */
    @Override
    public Object parseObject(String source) throws ParseException {
        if (!this.pattern.matcher(source).matches())
            throw new ParseException(
                    "Value [" + source + "] does not match regular expression [" + this.pattern.pattern() + "].", 0);
        return source;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Pattern='" + this.pattern + "'";
    }

}
