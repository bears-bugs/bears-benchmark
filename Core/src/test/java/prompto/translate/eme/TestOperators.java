package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestOperators extends BaseEParserTest {

	@Test
	public void testAddAmount() throws Exception {
		compareResourceEME("operators/addAmount.pec");
	}

	@Test
	public void testDivAmount() throws Exception {
		compareResourceEME("operators/divAmount.pec");
	}

	@Test
	public void testIdivAmount() throws Exception {
		compareResourceEME("operators/idivAmount.pec");
	}

	@Test
	public void testModAmount() throws Exception {
		compareResourceEME("operators/modAmount.pec");
	}

	@Test
	public void testMultAmount() throws Exception {
		compareResourceEME("operators/multAmount.pec");
	}

	@Test
	public void testSubAmount() throws Exception {
		compareResourceEME("operators/subAmount.pec");
	}

}

