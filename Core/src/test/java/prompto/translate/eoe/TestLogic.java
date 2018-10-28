package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLogic extends BaseEParserTest {

	@Test
	public void testAndBoolean() throws Exception {
		compareResourceEOE("logic/andBoolean.pec");
	}

	@Test
	public void testNotBoolean() throws Exception {
		compareResourceEOE("logic/notBoolean.pec");
	}

	@Test
	public void testOrBoolean() throws Exception {
		compareResourceEOE("logic/orBoolean.pec");
	}

}

