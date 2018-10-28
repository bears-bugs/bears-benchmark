package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMinus extends BaseOParserTest {

	@Test
	public void testMinusDecimal() throws Exception {
		compareResourceOMO("minus/minusDecimal.poc");
	}

	@Test
	public void testMinusInteger() throws Exception {
		compareResourceOMO("minus/minusInteger.poc");
	}

	@Test
	public void testMinusPeriod() throws Exception {
		compareResourceOMO("minus/minusPeriod.poc");
	}

}

