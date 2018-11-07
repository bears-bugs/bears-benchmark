package org.jsapar.parse.cell;

import org.jsapar.model.Cell;
import org.jsapar.schema.SchemaCellFormat;
import org.jsapar.utils.StringUtils;

import java.text.*;
import java.util.Locale;

/**
 * Abstract base class for parsing number values into {@link Cell} objects
 */
public abstract class NumberCellFactory implements CellFactory{

    @Override
    public Format makeFormat(Locale locale) {
        return NumberFormat.getInstance(locale);
    }

    protected Number parseObject(Format format, String value) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        value = adjustValueForOddLocales(value, format);
        Number number= (Number) format.parseObject(value, pos);

        if (pos.getIndex() < value.length())
            // It is not acceptable to parse only a part of the string. That can happen for instance if there is a space
            // in an integer value.
            throw new java.text.ParseException("Invalid characters found while parsing number.", pos.getIndex());
        return number;
    }

    /**
     * As of JDK 9 some locales have got new decimal symbols. This is a work around to also still be able to parse the
     * old format since they are still widely used.
     * @param sValue The value to parse
     * @param format The format to use
     * @return A modified value that can be parsed with supplied Format.
     */
    private String adjustValueForOddLocales(String sValue, Format format) {
        if (format instanceof DecimalFormat) {
            // This is necessary because some locales (e.g. swedish)
            // have non breakable space as thousands grouping character. Naturally
            // we want to remove all space characters including the non breakable.
            DecimalFormat decFormat = (DecimalFormat) format;
            char groupingSeparator = decFormat.getDecimalFormatSymbols().getGroupingSeparator();
            if (Character.isSpaceChar(groupingSeparator)) {
                sValue = StringUtils.removeAllSpaces(sValue);
            }

            String minusPrefix = decFormat.getNegativePrefix();
            if(!minusPrefix.equals("-") && !sValue.isEmpty() && sValue.charAt(0)=='-'){
                sValue = decFormat.getNegativePrefix() + sValue.substring(1);
            }

            String exp = decFormat.getDecimalFormatSymbols().getExponentSeparator();
            if(!exp.equals("E")){
                int pos = sValue.indexOf('E');
                if(pos > 0)
                    sValue = sValue.substring(0, pos) + exp + sValue.substring(pos+1);
            }
        }
        return sValue;
    }

    @Override
    public Format makeFormat(Locale locale, String pattern) {
        if (locale == null)
            locale = SchemaCellFormat.defaultLocale;
        if (pattern != null && !pattern.isEmpty())
            return new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
        else
            return makeFormat(locale);
    }
}
