package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestGreater extends BaseOParserTest {

	@Test
	public void testGtCharacter() throws Exception {
		compareResourceOMO("greater/gtCharacter.poc");
	}

	@Test
	public void testGtDate() throws Exception {
		compareResourceOMO("greater/gtDate.poc");
	}

	@Test
	public void testGtDateTime() throws Exception {
		compareResourceOMO("greater/gtDateTime.poc");
	}

	@Test
	public void testGtDecimal() throws Exception {
		compareResourceOMO("greater/gtDecimal.poc");
	}

	@Test
	public void testGtInteger() throws Exception {
		compareResourceOMO("greater/gtInteger.poc");
	}

	@Test
	public void testGtText() throws Exception {
		compareResourceOMO("greater/gtText.poc");
	}

	@Test
	public void testGtTime() throws Exception {
		compareResourceOMO("greater/gtTime.poc");
	}

	@Test
	public void testGtVersion() throws Exception {
		compareResourceOMO("greater/gtVersion.poc");
	}

	@Test
	public void testGteCharacter() throws Exception {
		compareResourceOMO("greater/gteCharacter.poc");
	}

	@Test
	public void testGteDate() throws Exception {
		compareResourceOMO("greater/gteDate.poc");
	}

	@Test
	public void testGteDateTime() throws Exception {
		compareResourceOMO("greater/gteDateTime.poc");
	}

	@Test
	public void testGteDecimal() throws Exception {
		compareResourceOMO("greater/gteDecimal.poc");
	}

	@Test
	public void testGteInteger() throws Exception {
		compareResourceOMO("greater/gteInteger.poc");
	}

	@Test
	public void testGteText() throws Exception {
		compareResourceOMO("greater/gteText.poc");
	}

	@Test
	public void testGteTime() throws Exception {
		compareResourceOMO("greater/gteTime.poc");
	}

}

