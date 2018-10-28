package prompto.runtime.m;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.m.BaseMParserTest;
import prompto.runtime.utils.Out;

public class TestCss extends BaseMParserTest {

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
		checkInterpretedOutput("css/codeValue.pmc");
	}

	@Test
	public void testCompiledCodeValue() throws Exception {
		checkCompiledOutput("css/codeValue.pmc");
	}

	@Test
	public void testTranspiledCodeValue() throws Exception {
		checkTranspiledOutput("css/codeValue.pmc");
	}

	@Test
	public void testInterpretedHyphenName() throws Exception {
		checkInterpretedOutput("css/hyphenName.pmc");
	}

	@Test
	public void testCompiledHyphenName() throws Exception {
		checkCompiledOutput("css/hyphenName.pmc");
	}

	@Test
	public void testTranspiledHyphenName() throws Exception {
		checkTranspiledOutput("css/hyphenName.pmc");
	}

	@Test
	public void testInterpretedMultiValue() throws Exception {
		checkInterpretedOutput("css/multiValue.pmc");
	}

	@Test
	public void testCompiledMultiValue() throws Exception {
		checkCompiledOutput("css/multiValue.pmc");
	}

	@Test
	public void testTranspiledMultiValue() throws Exception {
		checkTranspiledOutput("css/multiValue.pmc");
	}

	@Test
	public void testInterpretedNumberValue() throws Exception {
		checkInterpretedOutput("css/numberValue.pmc");
	}

	@Test
	public void testCompiledNumberValue() throws Exception {
		checkCompiledOutput("css/numberValue.pmc");
	}

	@Test
	public void testTranspiledNumberValue() throws Exception {
		checkTranspiledOutput("css/numberValue.pmc");
	}

	@Test
	public void testInterpretedPixelValue() throws Exception {
		checkInterpretedOutput("css/pixelValue.pmc");
	}

	@Test
	public void testCompiledPixelValue() throws Exception {
		checkCompiledOutput("css/pixelValue.pmc");
	}

	@Test
	public void testTranspiledPixelValue() throws Exception {
		checkTranspiledOutput("css/pixelValue.pmc");
	}

	@Test
	public void testInterpretedTextValue() throws Exception {
		checkInterpretedOutput("css/textValue.pmc");
	}

	@Test
	public void testCompiledTextValue() throws Exception {
		checkCompiledOutput("css/textValue.pmc");
	}

	@Test
	public void testTranspiledTextValue() throws Exception {
		checkTranspiledOutput("css/textValue.pmc");
	}

}

