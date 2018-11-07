package org.jsapar.compose.csv.quote;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by jonas on 2017-05-07.
 */
public class NeverQuoteButReplaceTest {
    @Test
    public void writeQuoted() throws Exception {
        NeverQuoteButReplace instance = new NeverQuoteButReplace( 10, ";", "\n", "*");

        StringWriter w = new StringWriter();
        instance.writeValue(w, "hej");
        assertEquals("hej", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej;san");
        assertEquals("hej*san", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej\nsan");
        assertEquals("hej*san", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "@hej");
        assertEquals("@hej", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej@");
        assertEquals("hej@", w.toString());

        w = new StringWriter();
        instance.writeValue(w, "hej;san123456");
        assertEquals("hej*san123", w.toString());
    }

}