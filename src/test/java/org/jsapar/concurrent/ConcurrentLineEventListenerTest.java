package org.jsapar.concurrent;

import org.jsapar.error.JSaParException;
import org.jsapar.model.Line;
import org.jsapar.parse.LineParsedEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentLineEventListenerTest {
    private volatile int count = 0;
    private boolean started = false;
    private boolean stopped = false;

    @Before
    public void setUp() throws Exception {
        count = 0;
        started = false;
        stopped = false;
    }

    @Test
    public void testLineParsedEvent() throws Exception {
        try (ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> {
        })) {
            assertEquals(0, instance.size());
            instance.registerOnStart(() -> this.started = true);
            instance.registerOnStop(() -> this.stopped = true);
            instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
            assertEquals(1, instance.size());
            assertFalse(this.started);
            instance.start();
            assertTrue(this.started);
            Thread.sleep(10L);
            assertEquals(0, instance.size());
            assertFalse(this.stopped);
            assertTrue(instance.isRunning());
        }
        assertTrue(this.stopped);

    }

    @Test
    public void testAddLineEventListener_run() {
        try (ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> count += 1)) {
            assertEquals(0, instance.size());
            instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
            assertEquals(1, instance.size());
            assertEquals(0, count);
            instance.start();
            assertTrue(instance.isRunning());
        }
        assertEquals(1, count);

    }

    @Test
    public void testWorkerTakesLongTime() {
        try (ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> {
            try {
                Thread.sleep(10L);
                count += 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        )) {
            assertEquals(0, instance.size());
            instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
            assertEquals(1, instance.size());
            assertEquals(0, count);
            instance.start();
        }
        assertEquals(1, count);

    }

    @Test
    public void testProducerTakesLongTime() throws Exception {
        ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> count += 1);
        assertEquals(0, instance.size());
        instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
        instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));

        assertEquals(2, instance.size());
        assertEquals(0, count);
        instance.start();

        instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
        Thread.sleep(10L);
        instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
        instance.close();
        assertEquals(0, instance.size());
        assertFalse(instance.isRunning());
        assertEquals(4, count);
    }

    @Test(expected = JSaParException.class)
    public void testExceptionFromListener_slow() {
        try (ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> {
            try {
                Thread.sleep(10); // Do some work.
                throw new AssertionError("Testing error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })) {
            instance.start();
            instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
        }
        fail("Exception expected");

    }

    @Test(expected = JSaParException.class)
    public void testExceptionFromListener_fast() {
        try (ConcurrentLineEventListener instance = new ConcurrentLineEventListener(event -> {
            throw new AssertionError("Testing error");
        })) {
            instance.start();
            instance.lineParsedEvent(new LineParsedEvent(this, new Line("")));
        }
        fail("Exception expected");

    }

}