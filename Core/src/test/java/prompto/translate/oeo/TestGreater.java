package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestGreater extends BaseOParserTest {

	@Test
	public void testGtCharacter() throws Exception {
		compareResourceOEO("greater/gtCharacter.poc");
	}

	@Test
	public void testGtDate() throws Exception {
		compareResourceOEO("greater/gtDate.poc");
	}

	@Test
	public void testGtDateTime() throws Exception {
		compareResourceOEO("greater/gtDateTime.poc");
	}

	@Test
	public void testGtDecimal() throws Exception {
		compareResourceOEO("greater/gtDecimal.poc");
	}

	@Test
	public void testGtInteger() throws Exception {
		compareResourceOEO("greater/gtInteger.poc");
	}

	@Test
	public void testGtText() throws Exception {
		compareResourceOEO("greater/gtText.poc");
	}

	@Test
	public void testGtTime() throws Exception {
		compareResourceOEO("greater/gtTime.poc");
	}

	@Test
	public void testGtVersion() throws Exception {
		compareResourceOEO("greater/gtVersion.poc");
	}

	@Test
	public void testGteCharacter() throws Exception {
		compareResourceOEO("greater/gteCharacter.poc");
	}

	@Test
	public void testGteDate() throws Exception {
		compareResourceOEO("greater/gteDate.poc");
	}

	@Test
	public void testGteDateTime() throws Exception {
		compareResourceOEO("greater/gteDateTime.poc");
	}

	@Test
	public void testGteDecimal() throws Exception {
		compareResourceOEO("greater/gteDecimal.poc");
	}

	@Test
	public void testGteInteger() throws Exception {
		compareResourceOEO("greater/gteInteger.poc");
	}

	@Test
	public void testGteText() throws Exception {
		compareResourceOEO("greater/gteText.poc");
	}

	@Test
	public void testGteTime() throws Exception {
		compareResourceOEO("greater/gteTime.poc");
	}

}

