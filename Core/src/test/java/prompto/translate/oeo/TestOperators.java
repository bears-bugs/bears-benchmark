package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestOperators extends BaseOParserTest {

	@Test
	public void testAddAmount() throws Exception {
		compareResourceOEO("operators/addAmount.poc");
	}

	@Test
	public void testDivAmount() throws Exception {
		compareResourceOEO("operators/divAmount.poc");
	}

	@Test
	public void testIdivAmount() throws Exception {
		compareResourceOEO("operators/idivAmount.poc");
	}

	@Test
	public void testModAmount() throws Exception {
		compareResourceOEO("operators/modAmount.poc");
	}

	@Test
	public void testMultAmount() throws Exception {
		compareResourceOEO("operators/multAmount.poc");
	}

	@Test
	public void testSubAmount() throws Exception {
		compareResourceOEO("operators/subAmount.poc");
	}

}

