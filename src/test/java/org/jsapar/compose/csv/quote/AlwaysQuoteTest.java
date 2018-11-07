package org.jsapar.compose.csv.quote;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 */
public class AlwaysQuoteTest {
    @Test
    public void writeQuoted_maxLength() throws Exception {
        AlwaysQuote instance = new AlwaysQuote( '/', 10);

        StringWriter w = new StringWriter();
        instance.writeValue(w, "hej");
        assertEquals("/hej/", w.toString());


        w = new StringWriter();
        instance.writeValue(w, "hej;san123456");
        assertEquals("/hej;san1/", w.toString());
    }

    @Test
    public void writeQuoted_atomic() throws Exception {
        AlwaysQuote instance = new AlwaysQuote( '/', -1);

        StringWriter w = new StringWriter();
        instance.writeValue(w, "hej");
        assertEquals("/hej/", w.toString());


        w = new StringWriter();
        instance.writeValue(w, "hej;san123456");
        assertEquals("/hej;san123456/", w.toString());
    }

}