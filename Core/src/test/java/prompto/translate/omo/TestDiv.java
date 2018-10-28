package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestDiv extends BaseOParserTest {

	@Test
	public void testDivDecimal() throws Exception {
		compareResourceOMO("div/divDecimal.poc");
	}

	@Test
	public void testDivInteger() throws Exception {
		compareResourceOMO("div/divInteger.poc");
	}

	@Test
	public void testIdivInteger() throws Exception {
		compareResourceOMO("div/idivInteger.poc");
	}

	@Test
	public void testModInteger() throws Exception {
		compareResourceOMO("div/modInteger.poc");
	}

}

