package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCss extends BaseEParserTest {

	@Test
	public void testCodeValue() throws Exception {
		compareResourceEME("css/codeValue.pec");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceEME("css/hyphenName.pec");
	}

	@Test
	public void testMultiValue() throws Exception {
		compareResourceEME("css/multiValue.pec");
	}

	@Test
	public void testNumberValue() throws Exception {
		compareResourceEME("css/numberValue.pec");
	}

	@Test
	public void testPixelValue() throws Exception {
		compareResourceEME("css/pixelValue.pec");
	}

	@Test
	public void testTextValue() throws Exception {
		compareResourceEME("css/textValue.pec");
	}

}

