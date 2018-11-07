package org.jsapar.parse.text;

import org.jsapar.error.JSaParException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class TextLineReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReaderLineReader() {
        String lineSeparator = "|";
        Reader reader = new StringReader("FirstLine|SecondLine");
        TextLineReader r = new TextLineReader(lineSeparator, reader);
        assertSame(lineSeparator, r.getLineSeparator());
        assertSame(reader, r.getReader());
    }

    @Test
    public void testReadLine() throws IOException, JSaParException {
        String lineSeparator = "|";
        Reader reader = new StringReader("FirstLine|SecondLine");
        TextLineReader r = new TextLineReader(lineSeparator, reader);
        assertEquals("FirstLine", r.readLine());
        assertEquals("SecondLine", r.readLine());
        assertNull(r.readLine());
    }

    @Test
    public void testReadLine_emptyLine() throws IOException, JSaParException {
        String lineSeparator = "|";
        Reader reader = new StringReader("FirstLine||ThirdLine");
        TextLineReader r = new TextLineReader(lineSeparator, reader);
        assertEquals("FirstLine", r.readLine());
        assertEquals("", r.readLine());
        assertEquals("ThirdLine", r.readLine());
        assertNull(r.readLine());
    }
    
    @Test
    public void testReadLine_emptyLineLast() throws IOException, JSaParException {
        String lineSeparator = "|";
        Reader reader = new StringReader("FirstLine|SecondLine|");
        TextLineReader r = new TextLineReader(lineSeparator, reader);
        assertEquals("FirstLine", r.readLine());
        assertEquals("SecondLine", r.readLine());
        assertEquals("", r.readLine());
        assertNull(r.readLine());
    }
}
