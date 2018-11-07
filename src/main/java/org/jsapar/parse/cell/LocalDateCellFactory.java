package org.jsapar.parse.cell;

import org.jsapar.model.Cell;
import org.jsapar.model.LocalDateCell;

import java.text.Format;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Parses date values into {@link Cell} objects
 */
public class LocalDateCellFactory extends AbstractDateTimeCellFactory {

    public LocalDateCellFactory() {
        super(DateTimeFormatter.ISO_DATE.toFormat());
    }

    @Override
    public Cell makeCell(String name, String value, Format format) throws ParseException {
        if (format == null)
            format = getDefaultFormat();

        return new LocalDateCell(name, LocalDate.from((TemporalAccessor) format.parseObject(value)));
    }

}
