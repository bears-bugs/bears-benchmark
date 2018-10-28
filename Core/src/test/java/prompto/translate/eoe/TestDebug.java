package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestDebug extends BaseEParserTest {

	@Test
	public void testStack() throws Exception {
		compareResourceEOE("debug/stack.pec");
	}

	@Test
	public void testVariables() throws Exception {
		compareResourceEOE("debug/variables.pec");
	}

}

