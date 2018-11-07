package org.jsapar.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {
    @Test
    public void countMatches() throws Exception {
        assertEquals(0, StringUtils.countMatches(null, ""));
        assertEquals(0, StringUtils.countMatches("", ""));
        assertEquals(2, StringUtils.countMatches("abABabc", "ab"));
        assertEquals(2, StringUtils.countMatches("ababaABabac", "aba"));
    }

    @Test
	public final void testRemoveAll() {
		String sOriginal = ".This.text.has.lots.of.dots.";
		String sResult = StringUtils.removeAll(sOriginal, '.');
		assertEquals("Thistexthaslotsofdots", sResult);
	}

	@Test
	public final void testRemoveAll_nothing() {
		String sOriginal = "This text has no colons";
		String sResult = StringUtils.removeAll(sOriginal, ':');
		assertEquals("This text has no colons", sResult);
	}

	@Test
	public final void testRemoveAllWhitespace() {
		String sOriginal = "This text has\nwhitespaces";
		String sResult = StringUtils.removeAllWhitespaces(sOriginal);
		assertEquals("Thistexthaswhitespaces", sResult);
	}

	
	@Test
	public final void testRemoveAllSpace() {
		String sOriginal = "This text has\u00A0spaces";
		String sResult = StringUtils.removeAllSpaces(sOriginal);
		assertEquals("Thistexthasspaces", sResult);
	}
	
}
