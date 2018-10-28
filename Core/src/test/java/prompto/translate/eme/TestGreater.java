package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestGreater extends BaseEParserTest {

	@Test
	public void testGtCharacter() throws Exception {
		compareResourceEME("greater/gtCharacter.pec");
	}

	@Test
	public void testGtDate() throws Exception {
		compareResourceEME("greater/gtDate.pec");
	}

	@Test
	public void testGtDateTime() throws Exception {
		compareResourceEME("greater/gtDateTime.pec");
	}

	@Test
	public void testGtDecimal() throws Exception {
		compareResourceEME("greater/gtDecimal.pec");
	}

	@Test
	public void testGtInteger() throws Exception {
		compareResourceEME("greater/gtInteger.pec");
	}

	@Test
	public void testGtText() throws Exception {
		compareResourceEME("greater/gtText.pec");
	}

	@Test
	public void testGtTime() throws Exception {
		compareResourceEME("greater/gtTime.pec");
	}

	@Test
	public void testGtVersion() throws Exception {
		compareResourceEME("greater/gtVersion.pec");
	}

	@Test
	public void testGteCharacter() throws Exception {
		compareResourceEME("greater/gteCharacter.pec");
	}

	@Test
	public void testGteDate() throws Exception {
		compareResourceEME("greater/gteDate.pec");
	}

	@Test
	public void testGteDateTime() throws Exception {
		compareResourceEME("greater/gteDateTime.pec");
	}

	@Test
	public void testGteDecimal() throws Exception {
		compareResourceEME("greater/gteDecimal.pec");
	}

	@Test
	public void testGteInteger() throws Exception {
		compareResourceEME("greater/gteInteger.pec");
	}

	@Test
	public void testGteText() throws Exception {
		compareResourceEME("greater/gteText.pec");
	}

	@Test
	public void testGteTime() throws Exception {
		compareResourceEME("greater/gteTime.pec");
	}

}

