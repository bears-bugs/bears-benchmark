package org.traccar.helper;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateBuilderTest {
    
    @Test
    public void testDateBuilder() throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateBuilder dateBuilder = new DateBuilder()
                .setDate(2015, 10, 20).setTime(1, 21, 11);

        Assert.assertEquals(dateFormat.parse("2015-10-20 01:21:11"), dateBuilder.getDate());

    }

}
