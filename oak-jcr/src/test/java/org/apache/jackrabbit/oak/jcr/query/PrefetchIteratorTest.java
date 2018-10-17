/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.jcr.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test the PrefetchIterator class.
 */
public class PrefetchIteratorTest {

    @Test
    public void testFastSize() {
        Iterable<Integer> s;
        PrefetchIterator<Integer> it;
        s = seq(0, 21);
        it = new PrefetchIterator<Integer>(s.iterator(), 20, 10, 100, -1, null);
        assertEquals(21, it.size());
    }

    @Test
    public void testKnownSize() {
        Iterable<Integer> s;
        PrefetchIterator<Integer> it;
        s = seq(0, 100);
        it = new PrefetchIterator<Integer>(s.iterator(), 5, 0, 10, 200, null);
        // reports the 'wrong' value as it was set manually
        assertEquals(200, it.size());
    }

    @Test
    public void testTimeout() {
        Iterable<Integer> s;
        PrefetchIterator<Integer> it;

        // long delay (10 ms per row)
        long timeout = 10;
        s = seq(0, 100, 10);
        it = new PrefetchIterator<Integer>(s.iterator(), 5, timeout, 1000, -1, null);
        assertEquals(-1, it.size());

        // no delay
        s = seq(0, 100);
        it = new PrefetchIterator<Integer>(s.iterator(), 5, timeout, 1000, -1, null);
        assertEquals(100, it.size());
    }

    @Test
    public void test() {
        // the following is the same as:
        // for (int size = 0; size < 100; size++)
        for (int size : seq(0, 100)) {
            for (int readBefore : seq(0, 30)) {
                // every 3th time, use a timeout
                long timeout = size % 3 == 0 ? 100 : 0;
                Iterable<Integer> s = seq(0, size);
                PrefetchIterator<Integer> it =
                        new PrefetchIterator<Integer>(s.iterator(), 20, timeout, 30, -1, null);
                for (int x : seq(0, readBefore)) {
                    boolean hasNext = it.hasNext();
                    if (!hasNext) {
                        assertEquals(x, size);
                        break;
                    }
                    String m = "s:" + size + " b:" + readBefore + " x:" + x;
                    assertTrue(m, hasNext);
                    assertEquals(m, x, it.next().intValue());
                }
                String m = "s:" + size + " b:" + readBefore;
                int max = timeout <= 0 ? 20 : 30;
                if (size > max && readBefore < size) {
                    assertEquals(m, -1, it.size());
                    // calling it twice must not change the result
                    assertEquals(m, -1, it.size());
                } else {
                    assertEquals(m, size, it.size());
                    // calling it twice must not change the result
                    assertEquals(m, size, it.size());
                }
                for (int x : seq(readBefore, size)) {
                    m = "s:" + size + " b:" + readBefore + " x:" + x;
                    assertTrue(m, it.hasNext());
                    assertEquals(m, x, it.next().intValue());
                }
                assertFalse(it.hasNext());
                try {
                    it.next();
                    fail();
                } catch (NoSuchElementException e) {
                    // expected
                }
                try {
                    it.remove();
                    fail();
                } catch (UnsupportedOperationException e) {
                    // expected
                }
            }
        }
    }

    /**
     * Create an integer sequence.
     *
     * @param start the first value
     * @param limit the last value + 1
     * @return a sequence of the values [start .. limit-1]
     */
    private static Iterable<Integer> seq(final int start, final int limit) {
        return seq(start, limit, 0);
    }

    /**
     * Create an integer sequence.
     *
     * @param start the first value
     * @param limit the last value + 1
     * @param sleep the time to wait for each element
     * @return a sequence of the values [start .. limit-1]
     */
    private static Iterable<Integer> seq(final int start, final int limit, final int sleep) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    int x = start;
                    @Override
                    public boolean hasNext() {
                        return x < limit;
                    }
                    @Override
                    public Integer next() {
                        if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException e) {
                                // ignore
                            }
                        }
                        return x++;
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            @Override
            public String toString() {
                return "[" + start + ".." + (limit - 1) + "]";
            }
        };
    }

}
