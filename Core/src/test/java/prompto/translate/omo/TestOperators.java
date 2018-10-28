package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestOperators extends BaseOParserTest {

	@Test
	public void testAddAmount() throws Exception {
		compareResourceOMO("operators/addAmount.poc");
	}

	@Test
	public void testDivAmount() throws Exception {
		compareResourceOMO("operators/divAmount.poc");
	}

	@Test
	public void testIdivAmount() throws Exception {
		compareResourceOMO("operators/idivAmount.poc");
	}

	@Test
	public void testModAmount() throws Exception {
		compareResourceOMO("operators/modAmount.poc");
	}

	@Test
	public void testMultAmount() throws Exception {
		compareResourceOMO("operators/multAmount.poc");
	}

	@Test
	public void testSubAmount() throws Exception {
		compareResourceOMO("operators/subAmount.poc");
	}

}

