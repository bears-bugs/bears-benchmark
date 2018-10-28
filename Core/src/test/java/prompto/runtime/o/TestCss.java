package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestCss extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedCodeValue() throws Exception {
		checkInterpretedOutput("css/codeValue.poc");
	}

	@Test
	public void testCompiledCodeValue() throws Exception {
		checkCompiledOutput("css/codeValue.poc");
	}

	@Test
	public void testTranspiledCodeValue() throws Exception {
		checkTranspiledOutput("css/codeValue.poc");
	}

	@Test
	public void testInterpretedHyphenName() throws Exception {
		checkInterpretedOutput("css/hyphenName.poc");
	}

	@Test
	public void testCompiledHyphenName() throws Exception {
		checkCompiledOutput("css/hyphenName.poc");
	}

	@Test
	public void testTranspiledHyphenName() throws Exception {
		checkTranspiledOutput("css/hyphenName.poc");
	}

	@Test
	public void testInterpretedMultiValue() throws Exception {
		checkInterpretedOutput("css/multiValue.poc");
	}

	@Test
	public void testCompiledMultiValue() throws Exception {
		checkCompiledOutput("css/multiValue.poc");
	}

	@Test
	public void testTranspiledMultiValue() throws Exception {
		checkTranspiledOutput("css/multiValue.poc");
	}

	@Test
	public void testInterpretedNumberValue() throws Exception {
		checkInterpretedOutput("css/numberValue.poc");
	}

	@Test
	public void testCompiledNumberValue() throws Exception {
		checkCompiledOutput("css/numberValue.poc");
	}

	@Test
	public void testTranspiledNumberValue() throws Exception {
		checkTranspiledOutput("css/numberValue.poc");
	}

	@Test
	public void testInterpretedPixelValue() throws Exception {
		checkInterpretedOutput("css/pixelValue.poc");
	}

	@Test
	public void testCompiledPixelValue() throws Exception {
		checkCompiledOutput("css/pixelValue.poc");
	}

	@Test
	public void testTranspiledPixelValue() throws Exception {
		checkTranspiledOutput("css/pixelValue.poc");
	}

	@Test
	public void testInterpretedTextValue() throws Exception {
		checkInterpretedOutput("css/textValue.poc");
	}

	@Test
	public void testCompiledTextValue() throws Exception {
		checkCompiledOutput("css/textValue.poc");
	}

	@Test
	public void testTranspiledTextValue() throws Exception {
		checkTranspiledOutput("css/textValue.poc");
	}

}

