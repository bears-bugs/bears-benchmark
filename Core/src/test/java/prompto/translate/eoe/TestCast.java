package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCast extends BaseEParserTest {

	@Test
	public void testAutoDecimalCast() throws Exception {
		compareResourceEOE("cast/autoDecimalCast.pec");
	}

	@Test
	public void testAutoDowncast() throws Exception {
		compareResourceEOE("cast/autoDowncast.pec");
	}

	@Test
	public void testAutoIntegerCast() throws Exception {
		compareResourceEOE("cast/autoIntegerCast.pec");
	}

	@Test
	public void testCastChild() throws Exception {
		compareResourceEOE("cast/castChild.pec");
	}

	@Test
	public void testCastDecimal() throws Exception {
		compareResourceEOE("cast/castDecimal.pec");
	}

	@Test
	public void testCastDocument() throws Exception {
		compareResourceEOE("cast/castDocument.pec");
	}

	@Test
	public void testCastInteger() throws Exception {
		compareResourceEOE("cast/castInteger.pec");
	}

	@Test
	public void testCastMethod() throws Exception {
		compareResourceEOE("cast/castMethod.pec");
	}

	@Test
	public void testCastMissing() throws Exception {
		compareResourceEOE("cast/castMissing.pec");
	}

	@Test
	public void testCastNull() throws Exception {
		compareResourceEOE("cast/castNull.pec");
	}

	@Test
	public void testCastRoot() throws Exception {
		compareResourceEOE("cast/castRoot.pec");
	}

	@Test
	public void testIsAChild() throws Exception {
		compareResourceEOE("cast/isAChild.pec");
	}

	@Test
	public void testIsAText() throws Exception {
		compareResourceEOE("cast/isAText.pec");
	}

}

