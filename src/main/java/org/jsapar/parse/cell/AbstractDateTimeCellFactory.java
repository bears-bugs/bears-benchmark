package org.jsapar.parse.cell;

import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 */
public abstract class AbstractDateTimeCellFactory implements CellFactory {

    private final Format defaultFormat;

    protected AbstractDateTimeCellFactory(Format defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    protected Format getDefaultFormat() {
        return defaultFormat;
    }

    @Override
    public Format makeFormat(Locale locale) {
        return defaultFormat;
    }

    @Override
    public Format makeFormat(Locale locale, String pattern) {
        if (pattern == null || pattern.isEmpty())
            return makeFormat(locale);
        return DateTimeFormatter.ofPattern(pattern, locale).toFormat();
    }


}
