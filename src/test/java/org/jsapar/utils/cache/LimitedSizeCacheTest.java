package org.jsapar.utils.cache;

import org.junit.Test;

import static org.junit.Assert.*;

public class LimitedSizeCacheTest {

    @Test
    public void get() {
        Cache<String, String> cache = new LimitedSizeCache<>(3);
        cache.put("one", "1");
        assertEquals("1", cache.get("one"));
        cache.put("two", "2");
        assertEquals("1", cache.get("one"));
        assertEquals("2", cache.get("two"));
        cache.put("three", "3");
        assertEquals("1", cache.get("one"));
        assertEquals("2", cache.get("two"));
        assertEquals("3", cache.get("three"));
        cache.put("four", "4");
        assertNull( cache.get("one")); // First one released when maxSize reached.

    }

}