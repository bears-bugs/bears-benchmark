package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestGreater extends BaseEParserTest {

	@Test
	public void testGtCharacter() throws Exception {
		compareResourceEOE("greater/gtCharacter.pec");
	}

	@Test
	public void testGtDate() throws Exception {
		compareResourceEOE("greater/gtDate.pec");
	}

	@Test
	public void testGtDateTime() throws Exception {
		compareResourceEOE("greater/gtDateTime.pec");
	}

	@Test
	public void testGtDecimal() throws Exception {
		compareResourceEOE("greater/gtDecimal.pec");
	}

	@Test
	public void testGtInteger() throws Exception {
		compareResourceEOE("greater/gtInteger.pec");
	}

	@Test
	public void testGtText() throws Exception {
		compareResourceEOE("greater/gtText.pec");
	}

	@Test
	public void testGtTime() throws Exception {
		compareResourceEOE("greater/gtTime.pec");
	}

	@Test
	public void testGtVersion() throws Exception {
		compareResourceEOE("greater/gtVersion.pec");
	}

	@Test
	public void testGteCharacter() throws Exception {
		compareResourceEOE("greater/gteCharacter.pec");
	}

	@Test
	public void testGteDate() throws Exception {
		compareResourceEOE("greater/gteDate.pec");
	}

	@Test
	public void testGteDateTime() throws Exception {
		compareResourceEOE("greater/gteDateTime.pec");
	}

	@Test
	public void testGteDecimal() throws Exception {
		compareResourceEOE("greater/gteDecimal.pec");
	}

	@Test
	public void testGteInteger() throws Exception {
		compareResourceEOE("greater/gteInteger.pec");
	}

	@Test
	public void testGteText() throws Exception {
		compareResourceEOE("greater/gteText.pec");
	}

	@Test
	public void testGteTime() throws Exception {
		compareResourceEOE("greater/gteTime.pec");
	}

}

