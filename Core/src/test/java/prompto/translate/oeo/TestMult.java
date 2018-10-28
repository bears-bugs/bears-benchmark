package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMult extends BaseOParserTest {

	@Test
	public void testMultCharacter() throws Exception {
		compareResourceOEO("mult/multCharacter.poc");
	}

	@Test
	public void testMultDecimal() throws Exception {
		compareResourceOEO("mult/multDecimal.poc");
	}

	@Test
	public void testMultInteger() throws Exception {
		compareResourceOEO("mult/multInteger.poc");
	}

	@Test
	public void testMultList() throws Exception {
		compareResourceOEO("mult/multList.poc");
	}

	@Test
	public void testMultPeriod() throws Exception {
		compareResourceOEO("mult/multPeriod.poc");
	}

	@Test
	public void testMultText() throws Exception {
		compareResourceOEO("mult/multText.poc");
	}

}

