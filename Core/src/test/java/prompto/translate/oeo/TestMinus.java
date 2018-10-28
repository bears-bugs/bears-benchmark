package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMinus extends BaseOParserTest {

	@Test
	public void testMinusDecimal() throws Exception {
		compareResourceOEO("minus/minusDecimal.poc");
	}

	@Test
	public void testMinusInteger() throws Exception {
		compareResourceOEO("minus/minusInteger.poc");
	}

	@Test
	public void testMinusPeriod() throws Exception {
		compareResourceOEO("minus/minusPeriod.poc");
	}

}

