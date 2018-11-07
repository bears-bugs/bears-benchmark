package org.jsapar.parse.cell;

import org.jsapar.model.Cell;
import org.jsapar.model.LocalTimeCell;

import java.text.Format;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Parses date values into {@link Cell} objects
 */
public class LocalTimeCellFactory extends AbstractDateTimeCellFactory {

    public LocalTimeCellFactory() {
        super(DateTimeFormatter.ISO_TIME.toFormat());
    }

    @Override
    public Cell makeCell(String name, String value, Format format) throws ParseException {
        if (format == null)
            format = getDefaultFormat();

        return new LocalTimeCell(name, LocalTime.from((TemporalAccessor) format.parseObject(value)));
    }

}
