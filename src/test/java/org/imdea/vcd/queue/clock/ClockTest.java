package org.imdea.vcd.queue.clock;

import java.util.Arrays;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vitor Enes
 */
public class ClockTest {

    @Test
    public void testContains() {
        HashMap<Integer, Long> a = new HashMap<>();
        a.put(0, 10L);
        a.put(1, 17L);
        Clock<MaxInt> clockA = Clock.vclock(a);

        assertTrue(clockA.contains(new Dot(0, 9L)));
        assertTrue(clockA.contains(new Dot(0, 10L)));
        assertFalse(clockA.contains(new Dot(0, 11L)));
        assertTrue(clockA.contains(new Dot(1, 11L)));
    }

    @Test
    public void testMerge() {
        HashMap<Integer, Long> a = new HashMap<>();
        a.put(0, 10L);
        a.put(1, 17L);
        Clock<MaxInt> clockA = Clock.vclock(a);

        HashMap<Integer, Long> b = new HashMap<>();
        b.put(0, 12L);
        b.put(1, 12L);
        Clock<MaxInt> clockB = Clock.vclock(b);

        clockA.merge(clockB);

        assertTrue(clockA.contains(new Dot(0, 9L)));
        assertTrue(clockA.contains(new Dot(0, 10L)));
        assertTrue(clockA.contains(new Dot(0, 11L)));
        assertTrue(clockA.contains(new Dot(0, 12L)));
        assertFalse(clockA.contains(new Dot(0, 13L)));
        assertTrue(clockA.contains(new Dot(1, 11L)));
        assertTrue(clockA.contains(new Dot(1, 17L)));
        assertFalse(clockA.contains(new Dot(1, 18L)));
    }

    @Test
    public void testIntersects() {
        HashMap<Integer, Long> a = new HashMap<>();
        a.put(0, 10L);
        a.put(1, 17L);
        Clock<MaxInt> clockA = Clock.vclock(a);

        assertTrue(clockA.intersects(new Dots(new Dot(0, 10L))));
        assertFalse(clockA.intersects(new Dots(new Dot(0, 11L))));
    }

    @Test
    public void testSubtract() {
        HashMap<Integer, Long> a = new HashMap<>();
        a.put(0, 7L);
        a.put(1, 5L);
        Clock<MaxInt> clockA = Clock.vclock(a);

        HashMap<Integer, Long> b = new HashMap<>();
        b.put(0, 5L);
        b.put(1, 5L);
        Clock<MaxInt> clockB = Clock.vclock(b);

        Dots r1 = clockA.subtract(clockB);
        Dots r2 = clockB.subtract(clockA);

        // r1 = [(0, 6), (0, 7)]
        assertTrue(r1.contains(new Dot(0, 6L)));
        assertTrue(r1.contains(new Dot(0, 7L)));
        assertEquals(r1.size(), 2);

        // r2 = []
        assertTrue(r2.isEmpty());
    }
}
