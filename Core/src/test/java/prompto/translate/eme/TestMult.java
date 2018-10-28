package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMult extends BaseEParserTest {

	@Test
	public void testMultCharacter() throws Exception {
		compareResourceEME("mult/multCharacter.pec");
	}

	@Test
	public void testMultDecimal() throws Exception {
		compareResourceEME("mult/multDecimal.pec");
	}

	@Test
	public void testMultInteger() throws Exception {
		compareResourceEME("mult/multInteger.pec");
	}

	@Test
	public void testMultList() throws Exception {
		compareResourceEME("mult/multList.pec");
	}

	@Test
	public void testMultPeriod() throws Exception {
		compareResourceEME("mult/multPeriod.pec");
	}

	@Test
	public void testMultText() throws Exception {
		compareResourceEME("mult/multText.pec");
	}

}

