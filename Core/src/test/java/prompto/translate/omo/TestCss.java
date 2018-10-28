package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestCss extends BaseOParserTest {

	@Test
	public void testCodeValue() throws Exception {
		compareResourceOMO("css/codeValue.poc");
	}

	@Test
	public void testHyphenName() throws Exception {
		compareResourceOMO("css/hyphenName.poc");
	}

	@Test
	public void testMultiValue() throws Exception {
		compareResourceOMO("css/multiValue.poc");
	}

	@Test
	public void testNumberValue() throws Exception {
		compareResourceOMO("css/numberValue.poc");
	}

	@Test
	public void testPixelValue() throws Exception {
		compareResourceOMO("css/pixelValue.poc");
	}

	@Test
	public void testTextValue() throws Exception {
		compareResourceOMO("css/textValue.poc");
	}

}

