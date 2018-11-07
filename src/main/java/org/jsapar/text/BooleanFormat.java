package org.jsapar.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Format class that can be used to parse or format boolean values based on a true and a false value. For instance, you
 * may have a text where Y is supposed to mean true and N is supposed to mean false. In that case you can create an
 * instance of this class in this way:
 * <pre>
 * {@code
 * BooleanFormat format = new BooleanFormat("Y", "N");
 * assert format.format(true).equals("Y");
 * assert format.format(false).equals("N");
 * assert format.parse("Y");
 * assert !format.parse("N");
 * }
 * </pre>
 * You can provide an array of valid true and false strings. In that case the first true and the first false strings
 * are used when formatting.
 *
 */
public class BooleanFormat extends Format {

    private String trueValue;
    private String falseValue;
    private String[] optionalTrue;
    private String[] optionalFalse;

    /**
     *
     */
    private static final long serialVersionUID = -281569113302316449L;

    /**
     * Creates a default instance where the text "true" is the true value and "false" is the false value.
     * <ul>
     * <li>Optional true values while parsing are: "on", "1", "yes", "y"</li>
     * <li>Optional false values while parsing are: "off", "0", "no", "n"</li>
     * </ul>
     */
    @SuppressWarnings("WeakerAccess")
    public BooleanFormat() {
        this(new String[]{"true", "on", "1", "yes", "y"}, new String[]{"false", "off", "0", "no", "n"});
    }

    /**
     * Creates a formatter for boolean values.
     *
     * @param trueValue  The string that represents the true value.
     * @param falseValue The string that represents the false value.
     */
    public BooleanFormat(String trueValue, String falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
        this.optionalTrue = new String[0];
        this.optionalFalse = new String[0];
        if (trueValue.equals(falseValue))
            throw new IllegalArgumentException("true and false values cannot be the same");
    }

    /**
     * Creates a formatter for boolean values where multiple values are accepted as true or false values. When parsing,
     * the supplied
     * values are tested for equality against the input in following order:
     * <ol>
     *     <li>The first true value.</li>
     *     <li>The first false value.</li>
     *     <li>The rest of the true values are tested in supplied order.</li>
     *     <li>The rest of the false values are tested in supplied order.</li>
     * </ol>
     *
     * @param trueValues  An array of all of the strings that represents the true value. The first item in the array is used when formatting.
     * @param falseValues An array of all of the strings that represents the false value. The first item in the array is used when formatting.
     */
    public BooleanFormat(String[] trueValues, String[] falseValues) {
        assert trueValues != null: "trueValues parameter cannot be null";
        assert falseValues != null: "falseValues parameter cannot be null";
        assert trueValues.length > 0: "trueValues needs to contain at least one value";
        assert falseValues.length > 0: "falseValues needs to contain at least one value";
        this.trueValue = trueValues[0];
        this.falseValue = falseValues[0];
        this.optionalTrue = Arrays.copyOfRange(trueValues, 1, trueValues.length);
        this.optionalFalse = Arrays.copyOfRange(falseValues, 1, falseValues.length);
        if (trueValue.equals(falseValue))
            throw new IllegalArgumentException("true and false values cannot be the same");
    }

    /* (non-Javadoc)
     * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
     */
    @Override
    public StringBuffer format(Object toFormat, StringBuffer appendToBuffer, FieldPosition pos) {
        int startPos = appendToBuffer.length();
        String value;
        if (toFormat.equals(Boolean.TRUE))
            value = trueValue;
        else if (toFormat.equals(Boolean.FALSE))
            value = falseValue;
        else
            throw new IllegalArgumentException("Only boolean objects can be formatted with this formatter.");

        appendToBuffer.append(value);
        int endPos = appendToBuffer.length();
        pos.setBeginIndex(startPos);
        pos.setEndIndex(endPos);
        return appendToBuffer;
    }

    /**
     * Formats a boolean value.
     *
     * @param value The value to format
     * @return the string value that represents the supplied boolean value.
     */
    public String format(boolean value) {
        return value ? trueValue : falseValue;
    }

    /* (non-Javadoc)
     * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
     */
    @Override
    public Object parseObject(String toParse, ParsePosition pos) {
        final boolean ignoreCase = true;
        if (toParse.regionMatches(ignoreCase, pos.getIndex(), trueValue, 0, trueValue.length())) {
            pos.setIndex(pos.getIndex() + trueValue.length());
            return Boolean.TRUE;
        }
        if (toParse.regionMatches(ignoreCase, pos.getIndex(), falseValue, 0, falseValue.length())) {
            pos.setIndex(pos.getIndex() + falseValue.length());
            return Boolean.FALSE;
        }
        return matchValue(Arrays.stream(optionalTrue), Boolean.TRUE, toParse, pos, ignoreCase)
                .orElseGet(()->matchValue(Arrays.stream(optionalFalse), Boolean.FALSE, toParse, pos, ignoreCase)
                        .orElseGet(() -> {
                            pos.setErrorIndex(pos.getIndex());
                            return null;
                        }));
    }

    private Optional<Boolean> matchValue(Stream<String> values, Boolean result, String toParse, ParsePosition pos, boolean ignoreCase) {
        return values
                .filter(v->toParse.regionMatches(ignoreCase, pos.getIndex(), v, 0, v.length()))
                .peek(v->pos.setIndex(pos.getIndex() + v.length()))
                .map(v->result)
                .findFirst();
    }

    /**
     * @param toParse The value to parse
     * @return true or false depending on value to parse.
     */
    public boolean parse(String toParse) {
        if (toParse.equalsIgnoreCase(trueValue))
            return true;
        else if (toParse.equalsIgnoreCase(falseValue))
            return false;
        else
            throw new NumberFormatException(
                    "Faled to parse [" + toParse + "] to boolean value only [" + trueValue + "] or [" + falseValue
                            + "] is allowed.");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[true=" + trueValue + ", false=" + falseValue + "]";
    }

}
