package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMult extends BaseOParserTest {

	@Test
	public void testMultCharacter() throws Exception {
		compareResourceOMO("mult/multCharacter.poc");
	}

	@Test
	public void testMultDecimal() throws Exception {
		compareResourceOMO("mult/multDecimal.poc");
	}

	@Test
	public void testMultInteger() throws Exception {
		compareResourceOMO("mult/multInteger.poc");
	}

	@Test
	public void testMultList() throws Exception {
		compareResourceOMO("mult/multList.poc");
	}

	@Test
	public void testMultPeriod() throws Exception {
		compareResourceOMO("mult/multPeriod.poc");
	}

	@Test
	public void testMultText() throws Exception {
		compareResourceOMO("mult/multText.poc");
	}

}

