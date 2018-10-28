package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCast extends BaseEParserTest {

	@Test
	public void testAutoDecimalCast() throws Exception {
		compareResourceEME("cast/autoDecimalCast.pec");
	}

	@Test
	public void testAutoDowncast() throws Exception {
		compareResourceEME("cast/autoDowncast.pec");
	}

	@Test
	public void testAutoIntegerCast() throws Exception {
		compareResourceEME("cast/autoIntegerCast.pec");
	}

	@Test
	public void testCastChild() throws Exception {
		compareResourceEME("cast/castChild.pec");
	}

	@Test
	public void testCastDecimal() throws Exception {
		compareResourceEME("cast/castDecimal.pec");
	}

	@Test
	public void testCastDocument() throws Exception {
		compareResourceEME("cast/castDocument.pec");
	}

	@Test
	public void testCastInteger() throws Exception {
		compareResourceEME("cast/castInteger.pec");
	}

	@Test
	public void testCastMethod() throws Exception {
		compareResourceEME("cast/castMethod.pec");
	}

	@Test
	public void testCastMissing() throws Exception {
		compareResourceEME("cast/castMissing.pec");
	}

	@Test
	public void testCastNull() throws Exception {
		compareResourceEME("cast/castNull.pec");
	}

	@Test
	public void testCastRoot() throws Exception {
		compareResourceEME("cast/castRoot.pec");
	}

	@Test
	public void testIsAChild() throws Exception {
		compareResourceEME("cast/isAChild.pec");
	}

	@Test
	public void testIsAText() throws Exception {
		compareResourceEME("cast/isAText.pec");
	}

}

