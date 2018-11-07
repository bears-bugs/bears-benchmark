package org.jsapar.parse.cell;

import org.jsapar.model.FloatCell;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by stejon0 on 2016-10-23.
 */
public class FloatCellFactoryTest {
    FloatCellFactory cellFactory = new FloatCellFactory();

    @Test
    public void testSetValueStringLocale() throws ParseException {
        FloatCell cell = (FloatCell) cellFactory.makeCell("test", "3.141,59", cellFactory.makeFormat(Locale.GERMANY));

        Assert.assertEquals(3141.59, cell.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testSetValueStringFormat() throws ParseException {
        DecimalFormat format = new DecimalFormat("#,###.##", DecimalFormatSymbols.getInstance(Locale.GERMANY));
        FloatCell cell = (FloatCell) cellFactory.makeCell("test", "3.141,59", format);

        Assert.assertEquals(3141.59, cell.getValue().doubleValue(), 0.001);
    }

}