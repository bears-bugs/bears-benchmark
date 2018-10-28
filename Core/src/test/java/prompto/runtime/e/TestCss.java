package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestCss extends BaseEParserTest {

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
		checkInterpretedOutput("css/codeValue.pec");
	}

	@Test
	public void testCompiledCodeValue() throws Exception {
		checkCompiledOutput("css/codeValue.pec");
	}

	@Test
	public void testTranspiledCodeValue() throws Exception {
		checkTranspiledOutput("css/codeValue.pec");
	}

	@Test
	public void testInterpretedHyphenName() throws Exception {
		checkInterpretedOutput("css/hyphenName.pec");
	}

	@Test
	public void testCompiledHyphenName() throws Exception {
		checkCompiledOutput("css/hyphenName.pec");
	}

	@Test
	public void testTranspiledHyphenName() throws Exception {
		checkTranspiledOutput("css/hyphenName.pec");
	}

	@Test
	public void testInterpretedMultiValue() throws Exception {
		checkInterpretedOutput("css/multiValue.pec");
	}

	@Test
	public void testCompiledMultiValue() throws Exception {
		checkCompiledOutput("css/multiValue.pec");
	}

	@Test
	public void testTranspiledMultiValue() throws Exception {
		checkTranspiledOutput("css/multiValue.pec");
	}

	@Test
	public void testInterpretedNumberValue() throws Exception {
		checkInterpretedOutput("css/numberValue.pec");
	}

	@Test
	public void testCompiledNumberValue() throws Exception {
		checkCompiledOutput("css/numberValue.pec");
	}

	@Test
	public void testTranspiledNumberValue() throws Exception {
		checkTranspiledOutput("css/numberValue.pec");
	}

	@Test
	public void testInterpretedPixelValue() throws Exception {
		checkInterpretedOutput("css/pixelValue.pec");
	}

	@Test
	public void testCompiledPixelValue() throws Exception {
		checkCompiledOutput("css/pixelValue.pec");
	}

	@Test
	public void testTranspiledPixelValue() throws Exception {
		checkTranspiledOutput("css/pixelValue.pec");
	}

	@Test
	public void testInterpretedTextValue() throws Exception {
		checkInterpretedOutput("css/textValue.pec");
	}

	@Test
	public void testCompiledTextValue() throws Exception {
		checkCompiledOutput("css/textValue.pec");
	}

	@Test
	public void testTranspiledTextValue() throws Exception {
		checkTranspiledOutput("css/textValue.pec");
	}

}

