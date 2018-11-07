package org.jsapar.concurrent;

import org.jsapar.TextComposer;
import org.jsapar.convert.ConvertTask;
import org.jsapar.error.JSaParException;
import org.jsapar.parse.text.TextParseTask;
import org.jsapar.schema.CsvSchema;
import org.jsapar.schema.FixedWidthSchema;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class ConcurrentConvertTaskTest {

    @Test
    public void testConcurrentText2TextConverter() throws IOException, JSaParException {
        TextParseTask p = new TextParseTask(new CsvSchema(), new StringReader(""));
        TextComposer c = new TextComposer(new FixedWidthSchema(), new StringWriter());
        ConvertTask instance = new ConcurrentConvertTask(p, c);
        assertSame(p, instance.getParseTask());
        assertSame(c, instance.getComposer());
    }

}