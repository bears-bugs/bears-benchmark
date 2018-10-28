package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestDiv extends BaseOParserTest {

	@Test
	public void testDivDecimal() throws Exception {
		compareResourceOEO("div/divDecimal.poc");
	}

	@Test
	public void testDivInteger() throws Exception {
		compareResourceOEO("div/divInteger.poc");
	}

	@Test
	public void testIdivInteger() throws Exception {
		compareResourceOEO("div/idivInteger.poc");
	}

	@Test
	public void testModInteger() throws Exception {
		compareResourceOEO("div/modInteger.poc");
	}

}

