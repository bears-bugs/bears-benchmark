package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestDiv extends BaseEParserTest {

	@Test
	public void testDivDecimal() throws Exception {
		compareResourceEME("div/divDecimal.pec");
	}

	@Test
	public void testDivInteger() throws Exception {
		compareResourceEME("div/divInteger.pec");
	}

	@Test
	public void testIdivInteger() throws Exception {
		compareResourceEME("div/idivInteger.pec");
	}

	@Test
	public void testModInteger() throws Exception {
		compareResourceEME("div/modInteger.pec");
	}

}

