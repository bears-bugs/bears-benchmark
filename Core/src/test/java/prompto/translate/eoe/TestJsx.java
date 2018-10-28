package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestJsx extends BaseEParserTest {

	@Test
	public void testChildElement() throws Exception {
		compareResourceEOE("jsx/childElement.pec");
	}

	@Test
	public void testCodeAttribute() throws Exception {
		compareResourceEOE("jsx/codeAttribute.pec");
	}

	@Test
	public void testCodeElement() throws Exception {
		compareResourceEOE("jsx/codeElement.pec");
	}

	@Test
	public void testDotName() throws Exception {
		compareResourceEOE("jsx/dotName.pec");
	}

	@Test
	public void testEmpty() throws Exception {
		compareResourceEOE("jsx/empty.pec");
	}

	@Test
	public void testEmptyAttribute() throws Exception {
		compareResourceEOE("jsx/emptyAttribute.pec");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceEOE("jsx/hyphenName.pec");
	}

	@Test
	public void testLiteralAttribute() throws Exception {
		compareResourceEOE("jsx/literalAttribute.pec");
	}

	@Test
	public void testSelfClosingDiv() throws Exception {
		compareResourceEOE("jsx/selfClosingDiv.pec");
	}

	@Test
	public void testSelfClosingEmptyAttribute() throws Exception {
		compareResourceEOE("jsx/selfClosingEmptyAttribute.pec");
	}

	@Test
	public void testTextElement() throws Exception {
		compareResourceEOE("jsx/textElement.pec");
	}

}

