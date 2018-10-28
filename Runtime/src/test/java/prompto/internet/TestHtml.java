package prompto.internet;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestHtml {

	@Test
	public void testEncodeStart() {
		assertEquals("&lt;ab", Html.encode("<ab"));
	}

	@Test
	public void testEncodeMiddle() {
		assertEquals("a&lt;b", Html.encode("a<b"));
	}

	@Test
	public void testEncodeEnd() {
		assertEquals("ab&lt;", Html.encode("ab<"));
	}

	@Test
	public void testEncodeTwice() {
		assertEquals("a&lt;b&gt;", Html.encode("a<b>"));
	}

	@Test
	public void testDecodeEntityStart() {
		assertEquals("<ab", Html.decode("&lt;ab"));
	}

	@Test
	public void testDecodeEntityMiddle() {
		assertEquals("a<b", Html.decode("a&lt;b"));
	}

	@Test
	public void testDecodeEntityEnd() {
		assertEquals("ab<", Html.decode("ab&lt;"));
	}

	@Test
	public void testDecodeEntityMulti() {
		assertEquals("a<b>", Html.decode("a&lt;b&gt;"));
	}

	@Test
	public void testDecodeCharCode() {
		assertEquals("a'b", Html.decode("a&#39;b"));
	}

}
