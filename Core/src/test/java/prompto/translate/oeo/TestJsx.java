package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestJsx extends BaseOParserTest {

	@Test
	public void testChildElement() throws Exception {
		compareResourceOEO("jsx/childElement.poc");
	}

	@Test
	public void testCodeAttribute() throws Exception {
		compareResourceOEO("jsx/codeAttribute.poc");
	}

	@Test
	public void testCodeElement() throws Exception {
		compareResourceOEO("jsx/codeElement.poc");
	}

	@Test
	public void testDotName() throws Exception {
		compareResourceOEO("jsx/dotName.poc");
	}

	@Test
	public void testEmpty() throws Exception {
		compareResourceOEO("jsx/empty.poc");
	}

	@Test
	public void testEmptyAttribute() throws Exception {
		compareResourceOEO("jsx/emptyAttribute.poc");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceOEO("jsx/hyphenName.poc");
	}

	@Test
	public void testLiteralAttribute() throws Exception {
		compareResourceOEO("jsx/literalAttribute.poc");
	}

	@Test
	public void testSelfClosingDiv() throws Exception {
		compareResourceOEO("jsx/selfClosingDiv.poc");
	}

	@Test
	public void testSelfClosingEmptyAttribute() throws Exception {
		compareResourceOEO("jsx/selfClosingEmptyAttribute.poc");
	}

	@Test
	public void testTextElement() throws Exception {
		compareResourceOEO("jsx/textElement.poc");
	}

}

