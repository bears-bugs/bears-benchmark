package org.jsapar.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by stejon0 on 2016-10-29.
 */
public class StringCellTest {

    @Test
    public void testGetSetStringValue() throws Exception {
        StringCell cell = new StringCell("name", "value");
        assertEquals("value", cell.getStringValue());
    }

    @Test
    public void testGetStringValue() throws Exception {

    }

    @Test
    public void testIsEmpty() throws Exception {
        StringCell c = new StringCell("empty", "");
        assertTrue(c.isEmpty());
    }
}