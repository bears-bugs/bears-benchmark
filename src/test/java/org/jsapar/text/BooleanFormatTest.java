/**
 *
 */
package org.jsapar.text;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.*;

/**
 * @author stejon0
 */
public class BooleanFormatTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testJavadocSample() {
        BooleanFormat format = new BooleanFormat("Y", "N");
        assert format.format(true).equals("Y");
        assert format.format(false).equals("N");
        assert format.parse("Y");
        assert !format.parse("N");
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)}.
     */
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        BooleanFormat f = new BooleanFormat("ja", "nej");
        assertEquals("nej", f.format(Boolean.FALSE));
        assertEquals("ja", f.format(Boolean.TRUE));
    }

    @Test
    public void testFormatDefault() {
        BooleanFormat f = new BooleanFormat();
        assertEquals("false", f.format(Boolean.FALSE));
        assertEquals("true", f.format(Boolean.TRUE));
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)}.
     */
    @Test
    public void testFormatObjectStringBufferFieldPosition_empty() {
        BooleanFormat f = new BooleanFormat("ja", "");
        assertEquals("", f.format(Boolean.FALSE));
        assertEquals("ja", f.format(Boolean.TRUE));
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#format(boolean)}.
     */
    @Test
    public void testFormatBoolean() {
        BooleanFormat f = new BooleanFormat("ja", "nej");
        assertEquals("nej", f.format(false));
        assertEquals("ja", f.format(true));
    }

    @Test
    public void testParseDefault() throws ParseException {
        BooleanFormat f = new BooleanFormat();
        assertEquals(Boolean.TRUE, f.parseObject("true"));
        assertEquals(Boolean.TRUE, f.parseObject("on"));
        assertEquals(Boolean.TRUE, f.parseObject("1"));
        assertEquals(Boolean.TRUE, f.parseObject("ON"));
        assertEquals(Boolean.TRUE, f.parseObject("yes"));
        assertEquals(Boolean.TRUE, f.parseObject("y"));
        assertEquals(Boolean.TRUE, f.parseObject("Y"));
        assertEquals(Boolean.FALSE, f.parseObject("false"));
        assertEquals(Boolean.FALSE, f.parseObject("off"));
        assertEquals(Boolean.FALSE, f.parseObject("0"));
        assertEquals(Boolean.FALSE, f.parseObject("OFF"));
        assertEquals(Boolean.FALSE, f.parseObject("NO"));
        assertEquals(Boolean.FALSE, f.parseObject("N"));
        assertEquals(Boolean.FALSE, f.parseObject("n"));
    }

    @Test(expected = ParseException.class)
    public void testParseFailed() throws ParseException {
        BooleanFormat f = new BooleanFormat("true", "false");
        assertEquals(null, f.parseObject("something different"));
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#parseObject(java.lang.String, java.text.ParsePosition)}.
     *
     * @throws ParseException
     */
    @Test
    public void testParseObjectString() throws ParseException {
        BooleanFormat f = new BooleanFormat("ja", "nej");
        assertEquals(Boolean.TRUE, f.parseObject("ja"));
        assertEquals(Boolean.TRUE, f.parseObject("JA"));
        assertEquals(Boolean.FALSE, f.parseObject("nej"));
        assertEquals(Boolean.FALSE, f.parseObject("NEJ"));
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#parseObject(java.lang.String, java.text.ParsePosition)}.
     *
     * @throws ParseException
     */
    @Test
    public void testParseObjectStringParsePosition() throws ParseException {
        BooleanFormat f = new BooleanFormat("ja", "nej");
        assertEquals(Boolean.TRUE, f.parseObject("   ja", new ParsePosition(3)));

        ParsePosition pos = new ParsePosition(3);
        assertEquals(Boolean.TRUE, f.parseObject("   JA  ", pos));
        assertEquals(5, pos.getIndex());

        assertEquals(Boolean.FALSE, f.parseObject("   nej ", new ParsePosition(3)));
        assertEquals(Boolean.FALSE, f.parseObject("   NEJ", new ParsePosition(3)));
    }

    /**
     * Test method for {@link org.jsapar.text.BooleanFormat#parse(java.lang.String)}.
     */
    @Test
    public void testParse() {
        BooleanFormat f = new BooleanFormat("ja", "nej");
        assertTrue(f.parse("ja"));
        assertTrue(f.parse("JA"));
        assertFalse(f.parse("nej"));
        assertFalse(f.parse("NEJ"));
    }

}
