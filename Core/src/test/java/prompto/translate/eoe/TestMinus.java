package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMinus extends BaseEParserTest {

	@Test
	public void testMinusDecimal() throws Exception {
		compareResourceEOE("minus/minusDecimal.pec");
	}

	@Test
	public void testMinusInteger() throws Exception {
		compareResourceEOE("minus/minusInteger.pec");
	}

	@Test
	public void testMinusPeriod() throws Exception {
		compareResourceEOE("minus/minusPeriod.pec");
	}

}

