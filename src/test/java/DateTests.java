import org.junit.jupiter.api.Test;
import valuestreams.DateValue;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class DateTests {

    @Test
    void testAll() {
        Date first = new GregorianCalendar(2000, Calendar.OCTOBER, 24).getTime();
        Date second = new GregorianCalendar(2003, Calendar.MARCH, 25).getTime();
        Date farFuture = new GregorianCalendar(2100, Calendar.MARCH, 26).getTime();

        assertNotNull(DateValue.of(first));

        assertTrue(DateValue.of(first).inMonth(Month.OCTOBER).inYear(2000).onDay(24).isPresent());
        assertFalse(DateValue.of(first).inMonth(Month.JANUARY).inYear(2000).onDay(24).isPresent());
        assertFalse(DateValue.of(first).inMonth(Month.OCTOBER).inYear(2001).onDay(24).isPresent());
        assertFalse(DateValue.of(first).inMonth(Month.OCTOBER).inYear(2000).onDay(26).isPresent());

        assertTrue(DateValue.of(first).isBefore(second).isPresent());
        assertTrue(DateValue.of(second).isAfter(first).isPresent());

        assertTrue(DateValue.of(first).past().isPresent());
        assertFalse(DateValue.of(first).future().isPresent());

        assertTrue(DateValue.of(farFuture).future().isPresent());
        assertFalse(DateValue.of(farFuture).past().isPresent());
    }
}
