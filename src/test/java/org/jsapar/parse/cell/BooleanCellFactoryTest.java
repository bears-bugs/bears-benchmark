package org.jsapar.parse.cell;

import org.jsapar.model.BooleanCell;
import org.jsapar.text.BooleanFormat;
import org.junit.Test;

import java.text.Format;
import java.util.Locale;

import static org.junit.Assert.*;

public class BooleanCellFactoryTest {
    @Test
    public void makeCell() throws Exception {
        BooleanCellFactory instance = new BooleanCellFactory();
        assertEquals(new BooleanCell("nnn", Boolean.TRUE), instance.makeCell("nnn", "true", null));
        assertEquals(new BooleanCell("nnn", Boolean.TRUE), instance.makeCell("nnn", "J", new BooleanFormat("J", "N")));
        assertEquals(new BooleanCell("nnn", Boolean.FALSE), instance.makeCell("nnn", "false", null));
        assertEquals(new BooleanCell("nnn", Boolean.FALSE), instance.makeCell("nnn", "N", new BooleanFormat("J", "N")));
    }

    @Test
    public void makeFormat() throws Exception {
        BooleanCellFactory instance = new BooleanCellFactory();
        Format f = instance.makeFormat(Locale.getDefault());
        assertNotNull(f);
        assertEquals(f.getClass(), BooleanFormat.class);
        assertEquals("true", f.format(true));
        assertEquals("false", f.format(false));
        assertEquals(true, f.parseObject("yes"));
        assertEquals(true, f.parseObject("Y"));
        assertEquals(false, f.parseObject("0"));
        assertEquals(false, f.parseObject("false"));
    }

    @Test
    public void makeFormat_pattern() throws Exception {
        BooleanCellFactory instance = new BooleanCellFactory();
        Format f = instance.makeFormat(Locale.getDefault(), "J|JA|1;N|NEJ|0");
        assertNotNull(f);
        assertEquals(f.getClass(), BooleanFormat.class);
        assertEquals("J", f.format(true));
        assertEquals("N", f.format(false));
        assertEquals(true, f.parseObject("ja"));
        assertEquals(true, f.parseObject("J"));
        assertEquals(false, f.parseObject("NEJ"));
        assertEquals(false, f.parseObject("n"));
    }

}