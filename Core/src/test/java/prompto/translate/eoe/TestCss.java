package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCss extends BaseEParserTest {

	@Test
	public void testCodeValue() throws Exception {
		compareResourceEOE("css/codeValue.pec");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceEOE("css/hyphenName.pec");
	}

	@Test
	public void testMultiValue() throws Exception {
		compareResourceEOE("css/multiValue.pec");
	}

	@Test
	public void testNumberValue() throws Exception {
		compareResourceEOE("css/numberValue.pec");
	}

	@Test
	public void testPixelValue() throws Exception {
		compareResourceEOE("css/pixelValue.pec");
	}

	@Test
	public void testTextValue() throws Exception {
		compareResourceEOE("css/textValue.pec");
	}

}

