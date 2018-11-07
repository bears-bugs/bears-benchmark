package org.jsapar.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanCellTest {

    @Test
    public void testGetSetBooleanValue() throws Exception {
        BooleanCell cell = new BooleanCell("test", false);
        assertFalse(cell.getValue());
    }

    @Test
    public void testGetStringValue() throws Exception {
        BooleanCell cell = new BooleanCell("test", false);
        assertEquals("false", cell.getStringValue());
    }

    @Test
    public void testCompareValueTo() throws Exception {
        BooleanCell cell1 = new BooleanCell("test", false);
        BooleanCell cell2 = new BooleanCell("test", false);
        BooleanCell cell3 = new BooleanCell("test", true);
        BooleanCell cell4 = new BooleanCell("test", true);
        assertEquals(0, cell1.compareValueTo(cell1));
        assertEquals(0, cell1.compareValueTo(cell2));
        assertTrue(cell1.compareValueTo(cell3) < 0);
        assertTrue(cell3.compareValueTo(cell1) > 0);
        assertEquals(0, cell3.compareValueTo(cell3));
        assertEquals(0, cell3.compareValueTo(cell4));
    }
}