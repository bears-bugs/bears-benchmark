package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestOperators extends BaseEParserTest {

	@Test
	public void testAddAmount() throws Exception {
		compareResourceEOE("operators/addAmount.pec");
	}

	@Test
	public void testDivAmount() throws Exception {
		compareResourceEOE("operators/divAmount.pec");
	}

	@Test
	public void testIdivAmount() throws Exception {
		compareResourceEOE("operators/idivAmount.pec");
	}

	@Test
	public void testModAmount() throws Exception {
		compareResourceEOE("operators/modAmount.pec");
	}

	@Test
	public void testMultAmount() throws Exception {
		compareResourceEOE("operators/multAmount.pec");
	}

	@Test
	public void testSubAmount() throws Exception {
		compareResourceEOE("operators/subAmount.pec");
	}

}

