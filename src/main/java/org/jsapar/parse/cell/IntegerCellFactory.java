package org.jsapar.parse.cell;

import org.jsapar.model.Cell;
import org.jsapar.model.IntegerCell;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Parses integer values into {@link Cell} objects
 */
public class IntegerCellFactory extends NumberCellFactory {
    @Override
    public Cell makeCell(String name, String value, Format format) throws ParseException {
        if(format == null)
            return new IntegerCell(name, Long.valueOf(value));
        Number number = super.parseObject(format, value);
        return new IntegerCell(name, number.longValue());
    }

    @Override
    public Format makeFormat(Locale locale) {
        return NumberFormat.getIntegerInstance(locale);
    }
}
