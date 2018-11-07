package org.jsapar.parse.cell;

import org.jsapar.model.Cell;
import org.jsapar.model.DateCell;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Parses date values into {@link Cell} objects
 */
public class
DateCellFactory implements CellFactory {

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    @Override
    public Cell makeCell(String name, String value, Format format) throws ParseException {
        if(format == null)
            format = ISO_DATE_FORMAT;

        return new DateCell(name, (Date) format.parseObject(value));
    }

    @Override
    public Format makeFormat(Locale locale) {
        // If pattern is not specified we always use ISO format because Java default format sucks.
        return ISO_DATE_FORMAT;
    }

    @Override
    public Format makeFormat(Locale locale, String pattern) {
        if(pattern == null || pattern.isEmpty())
            return makeFormat(locale);
        return new SimpleDateFormat(pattern);
    }
}
