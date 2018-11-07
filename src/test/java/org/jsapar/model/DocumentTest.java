package org.jsapar.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDocument() {
        Document d = new Document();
        assertNotNull(d.getLines());
        assertEquals(0, d.size());
    }


    @Test
    public void testAddAndGetLines() {
        Document d = new Document();
        assertNotNull(d.getLines());
        assertEquals(0, d.size());
        Line line = new Line("");
        d.addLine(line);
        assertNotNull(d.getLines());
        assertEquals(1, d.size());
        assertSame(line, d.getLines().iterator().next());
    }

    @Test
    public void testGetLine() {
        Document d = new Document();
        Line l1 = new Line("L1");
        Line l2 = new Line("L2");
        d.addLine(l1);
        d.addLine(l2);
        assertNotNull(d.getLines());
        assertEquals(2, d.size());
        assertSame(l1, d.getLine(0));
        assertSame(l2, d.getLine(1));
    }

    @Test
    public void testRemoveFirstLine() {
        Document d = new Document();
        Line l1 = new Line("L1");
        Line l2 = new Line("L2");
        d.addLine(l1);
        d.addLine(l2);
        d.removeFirstLine();
        assertEquals(1, d.size());
        assertSame(l2, d.getLine(0));
    }

    @Test
    public void testGetLineIterator() {
        Document d = new Document();
        Line l1 = new Line("type");
        d.addLine(l1);
        Line l2 = new Line("type");
        d.addLine(l2);
        Line l3 = new Line("some other type");
        d.addLine(l3);
        Iterator<Line> iter = d.iterator();
        assertTrue(iter.hasNext());
        assertSame(l1, iter.next());
        assertSame(l2, iter.next());
        assertSame(l3, iter.next());
        assertFalse(iter.hasNext());
    }


    @Test
    public void testIsEmpty() {
        Document d = new Document();
        assertTrue(d.isEmpty());
        Line line = new Line("");
        d.addLine(line);
        assertFalse(d.isEmpty());
    }

    @Test
    public void testContainsLineType() {
        Document d = new Document();
        assertFalse(d.containsLineType("type"));
        Line line = new Line("type");
        d.addLine(line);
        assertTrue(d.containsLineType("type"));
    }

    @Test
    public void testFindFirstLineOfType() {
        Document d = new Document();
        Line l1 = new Line("type");
        d.addLine(l1);
        Line l2 = new Line("type");
        d.addLine(l2);
        Line l3 = new Line("some other type");
        d.addLine(l3);
        assertSame(l1, d.findFirstLineOfType("type").orElse(null));
        assertSame(l3, d.findFirstLineOfType("some other type").orElse(null));
    }

    @Test
    public void testFindLinesOfType() {
        Document d = new Document();
        Line l1 = new Line("type");
        d.addLine(l1);
        Line l2 = new Line("type");
        d.addLine(l2);
        Line l3 = new Line("some other type");
        d.addLine(l3);
        List<Line> typeLines = d.findLinesOfType("type");
        assertEquals(2, typeLines.size());
        assertSame(l1, typeLines.get(0));
        assertSame(l2, typeLines.get(1));
    }

    @Test
    public void testSize() {
        Document d = new Document();
        assertEquals(0, d.size());
        Line line = new Line("");
        d.addLine(line);
        assertEquals(1, d.size());
    }

    @Test
    public void testRemoveLine() {
        Document d = new Document();
        Line l1 = new Line("type");
        d.addLine(l1);
        Line l2 = new Line("type");
        d.addLine(l2);
        Line l3 = new Line("some other type");
        d.addLine(l3);
        assertEquals(3, d.size());
        assertSame(l2, d.getLine(1));
        assertTrue(d.removeLine(l2));
        assertEquals(2, d.size());
        assertNotSame(l2, d.getLine(1));
        assertFalse(d.removeLine(l2));
        assertEquals(2, d.size());
    }

    @Test
    public void testRemoveLineAt() {
        Document d = new Document();
        Line l1 = new Line("type");
        d.addLine(l1);
        Line l2 = new Line("type");
        d.addLine(l2);
        Line l3 = new Line("some other type");
        d.addLine(l3);
        assertEquals(3, d.size());
        assertSame(l2, d.getLine(1));
        assertSame(l2, d.removeLineAt(1));
        assertEquals(2, d.size());
    }

}
