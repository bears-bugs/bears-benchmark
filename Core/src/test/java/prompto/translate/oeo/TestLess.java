package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLess extends BaseOParserTest {

	@Test
	public void testLtCharacter() throws Exception {
		compareResourceOEO("less/ltCharacter.poc");
	}

	@Test
	public void testLtDate() throws Exception {
		compareResourceOEO("less/ltDate.poc");
	}

	@Test
	public void testLtDateTime() throws Exception {
		compareResourceOEO("less/ltDateTime.poc");
	}

	@Test
	public void testLtDecimal() throws Exception {
		compareResourceOEO("less/ltDecimal.poc");
	}

	@Test
	public void testLtInteger() throws Exception {
		compareResourceOEO("less/ltInteger.poc");
	}

	@Test
	public void testLtText() throws Exception {
		compareResourceOEO("less/ltText.poc");
	}

	@Test
	public void testLtTime() throws Exception {
		compareResourceOEO("less/ltTime.poc");
	}

	@Test
	public void testLtVersion() throws Exception {
		compareResourceOEO("less/ltVersion.poc");
	}

	@Test
	public void testLteCharacter() throws Exception {
		compareResourceOEO("less/lteCharacter.poc");
	}

	@Test
	public void testLteDate() throws Exception {
		compareResourceOEO("less/lteDate.poc");
	}

	@Test
	public void testLteDateTime() throws Exception {
		compareResourceOEO("less/lteDateTime.poc");
	}

	@Test
	public void testLteDecimal() throws Exception {
		compareResourceOEO("less/lteDecimal.poc");
	}

	@Test
	public void testLteInteger() throws Exception {
		compareResourceOEO("less/lteInteger.poc");
	}

	@Test
	public void testLteText() throws Exception {
		compareResourceOEO("less/lteText.poc");
	}

	@Test
	public void testLteTime() throws Exception {
		compareResourceOEO("less/lteTime.poc");
	}

}

