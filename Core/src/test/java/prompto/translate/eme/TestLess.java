package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLess extends BaseEParserTest {

	@Test
	public void testLtCharacter() throws Exception {
		compareResourceEME("less/ltCharacter.pec");
	}

	@Test
	public void testLtDate() throws Exception {
		compareResourceEME("less/ltDate.pec");
	}

	@Test
	public void testLtDateTime() throws Exception {
		compareResourceEME("less/ltDateTime.pec");
	}

	@Test
	public void testLtDecimal() throws Exception {
		compareResourceEME("less/ltDecimal.pec");
	}

	@Test
	public void testLtInteger() throws Exception {
		compareResourceEME("less/ltInteger.pec");
	}

	@Test
	public void testLtText() throws Exception {
		compareResourceEME("less/ltText.pec");
	}

	@Test
	public void testLtTime() throws Exception {
		compareResourceEME("less/ltTime.pec");
	}

	@Test
	public void testLtVersion() throws Exception {
		compareResourceEME("less/ltVersion.pec");
	}

	@Test
	public void testLteCharacter() throws Exception {
		compareResourceEME("less/lteCharacter.pec");
	}

	@Test
	public void testLteDate() throws Exception {
		compareResourceEME("less/lteDate.pec");
	}

	@Test
	public void testLteDateTime() throws Exception {
		compareResourceEME("less/lteDateTime.pec");
	}

	@Test
	public void testLteDecimal() throws Exception {
		compareResourceEME("less/lteDecimal.pec");
	}

	@Test
	public void testLteInteger() throws Exception {
		compareResourceEME("less/lteInteger.pec");
	}

	@Test
	public void testLteText() throws Exception {
		compareResourceEME("less/lteText.pec");
	}

	@Test
	public void testLteTime() throws Exception {
		compareResourceEME("less/lteTime.pec");
	}

}

