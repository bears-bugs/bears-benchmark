package org.jsapar.compose.csv.quote;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 */
public class NeverQuoteTest {
    @Test
    public void writeQuoted_maxLength() throws Exception {
        NeverQuote instance = new NeverQuote( 10);

        StringWriter w = new StringWriter();
        instance.writeValue(w, "hej");
        assertEquals("hej", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej;san123456");
        assertEquals("hej;san123", w.toString());
    }

    @Test
    public void writeQuoted_atomic() throws Exception {
        NeverQuote instance = new NeverQuote( -1);

        StringWriter w = new StringWriter();
        instance.writeValue(w, "hej");
        assertEquals("hej", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej;san123456");
        assertEquals("hej;san123456", w.toString());
    }

}