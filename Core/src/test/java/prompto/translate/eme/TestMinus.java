package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMinus extends BaseEParserTest {

	@Test
	public void testMinusDecimal() throws Exception {
		compareResourceEME("minus/minusDecimal.pec");
	}

	@Test
	public void testMinusInteger() throws Exception {
		compareResourceEME("minus/minusInteger.pec");
	}

	@Test
	public void testMinusPeriod() throws Exception {
		compareResourceEME("minus/minusPeriod.pec");
	}

}

