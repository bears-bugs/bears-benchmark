package org.jsapar.parse.text;

import org.jsapar.error.JSaParException;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class TextLineReaderAnyCRLFTest {

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("FirstLine\nSecondLine\r\nThirdLine");
        LineReader r = new TextLineReaderAnyCRLF("\r\n", reader);
        assertEquals("\r\n", r.getLineSeparator());
        assertEquals("FirstLine", r.readLine());
        assertEquals("\n", r.getLineSeparator());
        assertEquals("SecondLine", r.readLine());
        assertEquals("\r\n", r.getLineSeparator());
        assertEquals("ThirdLine", r.readLine());
        assertEquals("\r\n", r.getLineSeparator());
        assertNull(r.readLine());
    }

    @Test
    public void testReadLine_emptyLine() throws IOException, JSaParException {
        Reader reader = new StringReader("FirstLine\n\r\nThirdLine");
        LineReader r = new TextLineReaderAnyCRLF("\n", reader);
        assertEquals("FirstLine", r.readLine());
        assertEquals("", r.readLine());
        assertEquals("ThirdLine", r.readLine());
        assertNull(r.readLine());
    }

    @Test
    public void testReadLine_emptyLineLast() throws IOException, JSaParException {
        Reader reader = new StringReader("FirstLine\nSecondLine\n");
        TextLineReader r = new TextLineReader("\n", reader);
        assertEquals("FirstLine", r.readLine());
        assertEquals("SecondLine", r.readLine());
        assertEquals("", r.readLine());
        assertNull(r.readLine());
    }

}