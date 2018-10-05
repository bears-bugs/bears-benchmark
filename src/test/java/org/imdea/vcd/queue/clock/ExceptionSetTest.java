package org.imdea.vcd.queue.clock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vitor Enes
 */
public class ExceptionSetTest {

    @Test
    public void testContains() {
        ExceptionSet a = new ExceptionSet(10L, new HashSet<>(Arrays.asList(2L, 4L)));
        assertTrue(a.contains(9L));
        assertFalse(a.contains(11L));
        assertFalse(a.contains(2L));
    }

    @Test
    public void testAdd() {
        ExceptionSet a = new ExceptionSet(10L, new HashSet<>(Arrays.asList(2L, 4L)));
        assertTrue(a.contains(9L));
        a.add(9L);
        assertTrue(a.contains(9L));
        a.add(2L);
        assertTrue(a.contains(2L));
        assertFalse(a.contains(4L));
        a.add(12L);
        assertTrue(a.contains(12L));
        assertFalse(a.contains(11L));
        assertFalse(a.contains(4L));
        a.add(4L);
        assertFalse(a.contains(11L));
        assertTrue(a.contains(4L));
        a.add(11L);
        assertTrue(a.contains(11L));
    }

    @Test
    public void testMerge() {
        ExceptionSet a = new ExceptionSet(10L, new HashSet<>(Arrays.asList(2L, 4L)));
        ExceptionSet b = new ExceptionSet(11L, new HashSet<>(Arrays.asList(3L, 4L)));

        a.merge(b);
        assertTrue(a.contains(10L));
        assertTrue(a.contains(11L));
        assertTrue(a.contains(2L));
        assertTrue(a.contains(3L));
        assertFalse(a.contains(4L));
    }

    @Test
    public void testSubtract() {
        ExceptionSet a = new ExceptionSet(7L, new HashSet<>(Arrays.asList(1L)));
        ExceptionSet b = new ExceptionSet(5L, new HashSet<>(Arrays.asList(2L)));

        List<Long> r1 = a.subtract(b);
        List<Long> r2 = b.subtract(a);

        // r1 = [2, 6, 7]
        assertTrue(r1.contains(2L));
        assertTrue(r1.contains(6L));
        assertTrue(r1.contains(7L));
        assertEquals(r1.size(), 3);

        // r2 = [1]
        assertTrue(r2.contains(1L));
        assertEquals(r2.size(), 1);
    }
}
