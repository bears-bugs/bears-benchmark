/**
 * Copyright: Jonas Stenberg
 */
package org.jsapar.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * @author Jonas Stenberg
 *
 */
public class DateCellTest {
    Date now;
    Date aDate;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        now = new Date();

        java.util.Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Sweden"));
        calendar.set(2007, Calendar.SEPTEMBER, 1, 12, 5);
        calendar.set(Calendar.SECOND, 30);
        calendar.set(Calendar.MILLISECOND, 555);
        aDate = calendar.getTime();
    }

    /**
     * Test method for
     * {@link DateCell#DateCell(java.lang.String, java.util.Date)}
     * .
     */
    @Test
    public final void testDateCell() {
        DateCell cell = new DateCell("Name", now);
        assertEquals("Name", cell.getName());
        assertEquals(now, cell.getValue());
    }

    @Test
    public void testGetSetDateValue() throws Exception {
        Date date = new Date();
        DateCell cell = new DateCell("Name", date);
        assertEquals(date, cell.getValue());

    }
}
