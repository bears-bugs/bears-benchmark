package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLess extends BaseOParserTest {

	@Test
	public void testLtCharacter() throws Exception {
		compareResourceOMO("less/ltCharacter.poc");
	}

	@Test
	public void testLtDate() throws Exception {
		compareResourceOMO("less/ltDate.poc");
	}

	@Test
	public void testLtDateTime() throws Exception {
		compareResourceOMO("less/ltDateTime.poc");
	}

	@Test
	public void testLtDecimal() throws Exception {
		compareResourceOMO("less/ltDecimal.poc");
	}

	@Test
	public void testLtInteger() throws Exception {
		compareResourceOMO("less/ltInteger.poc");
	}

	@Test
	public void testLtText() throws Exception {
		compareResourceOMO("less/ltText.poc");
	}

	@Test
	public void testLtTime() throws Exception {
		compareResourceOMO("less/ltTime.poc");
	}

	@Test
	public void testLtVersion() throws Exception {
		compareResourceOMO("less/ltVersion.poc");
	}

	@Test
	public void testLteCharacter() throws Exception {
		compareResourceOMO("less/lteCharacter.poc");
	}

	@Test
	public void testLteDate() throws Exception {
		compareResourceOMO("less/lteDate.poc");
	}

	@Test
	public void testLteDateTime() throws Exception {
		compareResourceOMO("less/lteDateTime.poc");
	}

	@Test
	public void testLteDecimal() throws Exception {
		compareResourceOMO("less/lteDecimal.poc");
	}

	@Test
	public void testLteInteger() throws Exception {
		compareResourceOMO("less/lteInteger.poc");
	}

	@Test
	public void testLteText() throws Exception {
		compareResourceOMO("less/lteText.poc");
	}

	@Test
	public void testLteTime() throws Exception {
		compareResourceOMO("less/lteTime.poc");
	}

}

