package org.jsapar.parse.csv;

import org.jsapar.error.JSaParException;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created by stejon0 on 2016-05-01.
 */
public class CsvLineReaderTest {

    @Test
    public void testReset() throws Exception {
        Reader reader = new StringReader("First;line|second,'line'|third,line||fifth;one");
        CsvLineReader item = new CsvLineReader("|", reader);
        assertArrayEquals( new String[]{"First", "line"}, item.readLine(";", (char) 0));
        assertArrayEquals( new String[]{"second,'line'"}, item.readLine(";", (char) 0));
        item.reset();
        assertArrayEquals( new String[]{"second", "line"}, item.readLine(",", '\''));
        item.reset();
        assertArrayEquals( new String[]{"second", "line"}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{"third", "line"}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{"fifth;one"}, item.readLine(" ", (char) 0));
        assertTrue(item.eofReached());
        item.reset();
        assertFalse(item.eofReached());
        assertArrayEquals( new String[]{"fifth", "one"}, item.readLine(";", (char) 0));
        assertTrue(item.eofReached());
    }



    @Test
    public void testReadLine() throws Exception {
        Reader reader = new StringReader("First;line|second,'line'|third,line||fifth;one");
        CsvLineReader item = new CsvLineReader("|", reader);
        assertArrayEquals( new String[]{"First", "line"}, item.readLine(";", (char) 0));
        assertArrayEquals( new String[]{"second", "line"}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{"third", "line"}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{}, item.readLine(",", '\''));
        assertArrayEquals( new String[]{"fifth", "one"}, item.readLine(";", (char) 0));

    }

    @Test
    public  void testLastLineWasEmpty() throws IOException, JSaParException {
        Reader reader = new StringReader("First;line||third,line|");
        CsvLineReader item = new CsvLineReader("|", reader);
        assertArrayEquals( new String[]{"First", "line"}, item.readLine(";", (char) 0));
        assertFalse(item.lastLineWasEmpty());
        assertArrayEquals( new String[0], item.readLine(";", (char) 0));
        assertTrue(item.lastLineWasEmpty());
        assertArrayEquals( new String[]{"third", "line"}, item.readLine(",", '\''));
        assertFalse(item.lastLineWasEmpty());
        assertFalse(item.eofReached());
        assertArrayEquals( new String[0], item.readLine(",", '\''));
        assertTrue(item.lastLineWasEmpty());
        assertTrue(item.eofReached());

    }
}