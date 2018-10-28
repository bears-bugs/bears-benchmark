package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestDiv extends BaseEParserTest {

	@Test
	public void testDivDecimal() throws Exception {
		compareResourceEOE("div/divDecimal.pec");
	}

	@Test
	public void testDivInteger() throws Exception {
		compareResourceEOE("div/divInteger.pec");
	}

	@Test
	public void testIdivInteger() throws Exception {
		compareResourceEOE("div/idivInteger.pec");
	}

	@Test
	public void testModInteger() throws Exception {
		compareResourceEOE("div/modInteger.pec");
	}

}

