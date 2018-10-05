package org.imdea.vcd.queue.clock;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vitor Enes
 */
public class MaxIntTest {

    @Test
    public void testContains() {
        MaxInt a = new MaxInt(10L);
        assertTrue(a.contains(9L));
        assertFalse(a.contains(11L));
    }

    @Test
    public void testMerge() {
        MaxInt a = new MaxInt(10L);
        MaxInt b = new MaxInt(17L);
        a.merge(b);
        assertTrue(a.contains(16L));
        assertTrue(a.contains(17L));
        assertFalse(a.contains(18L));
    }

    @Test
    public void testSubtract() {
        MaxInt a = new MaxInt(7L);
        MaxInt b = new MaxInt(5L);

        List<Long> r1 = a.subtract(b);
        List<Long> r2 = b.subtract(a);

        // r1 = [6, 7]
        assertTrue(r1.contains(6L));
        assertTrue(r1.contains(7L));
        assertEquals(r1.size(), 2);

        // r2 = []
        assertTrue(r2.isEmpty());
    }
}
