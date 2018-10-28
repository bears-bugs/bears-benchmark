package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestCss extends BaseOParserTest {

	@Test
	public void testCodeValue() throws Exception {
		compareResourceOEO("css/codeValue.poc");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceOEO("css/hyphenName.poc");
	}

	@Test
	public void testMultiValue() throws Exception {
		compareResourceOEO("css/multiValue.poc");
	}

	@Test
	public void testNumberValue() throws Exception {
		compareResourceOEO("css/numberValue.poc");
	}

	@Test
	public void testPixelValue() throws Exception {
		compareResourceOEO("css/pixelValue.poc");
	}

	@Test
	public void testTextValue() throws Exception {
		compareResourceOEO("css/textValue.poc");
	}

}

