package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestJsx extends BaseEParserTest {

	@Test
	public void testChildElement() throws Exception {
		compareResourceEME("jsx/childElement.pec");
	}

	@Test
	public void testCodeAttribute() throws Exception {
		compareResourceEME("jsx/codeAttribute.pec");
	}

	@Test
	public void testCodeElement() throws Exception {
		compareResourceEME("jsx/codeElement.pec");
	}

	@Test
	public void testDotName() throws Exception {
		compareResourceEME("jsx/dotName.pec");
	}

	@Test
	public void testEmpty() throws Exception {
		compareResourceEME("jsx/empty.pec");
	}

	@Test
	public void testEmptyAttribute() throws Exception {
		compareResourceEME("jsx/emptyAttribute.pec");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceEME("jsx/hyphenName.pec");
	}

	@Test
	public void testLiteralAttribute() throws Exception {
		compareResourceEME("jsx/literalAttribute.pec");
	}

	@Test
	public void testSelfClosingDiv() throws Exception {
		compareResourceEME("jsx/selfClosingDiv.pec");
	}

	@Test
	public void testSelfClosingEmptyAttribute() throws Exception {
		compareResourceEME("jsx/selfClosingEmptyAttribute.pec");
	}

	@Test
	public void testTextElement() throws Exception {
		compareResourceEME("jsx/textElement.pec");
	}

}

