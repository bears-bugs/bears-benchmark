package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMult extends BaseEParserTest {

	@Test
	public void testMultCharacter() throws Exception {
		compareResourceEOE("mult/multCharacter.pec");
	}

	@Test
	public void testMultDecimal() throws Exception {
		compareResourceEOE("mult/multDecimal.pec");
	}

	@Test
	public void testMultInteger() throws Exception {
		compareResourceEOE("mult/multInteger.pec");
	}

	@Test
	public void testMultList() throws Exception {
		compareResourceEOE("mult/multList.pec");
	}

	@Test
	public void testMultPeriod() throws Exception {
		compareResourceEOE("mult/multPeriod.pec");
	}

	@Test
	public void testMultText() throws Exception {
		compareResourceEOE("mult/multText.pec");
	}

}

