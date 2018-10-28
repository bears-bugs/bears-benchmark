package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestJsx extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedChildElement() throws Exception {
		checkInterpretedOutput("jsx/childElement.pec");
	}

	@Test
	public void testCompiledChildElement() throws Exception {
		checkCompiledOutput("jsx/childElement.pec");
	}

	@Test
	public void testTranspiledChildElement() throws Exception {
		checkTranspiledOutput("jsx/childElement.pec");
	}

	@Test
	public void testInterpretedCodeAttribute() throws Exception {
		checkInterpretedOutput("jsx/codeAttribute.pec");
	}

	@Test
	public void testCompiledCodeAttribute() throws Exception {
		checkCompiledOutput("jsx/codeAttribute.pec");
	}

	@Test
	public void testTranspiledCodeAttribute() throws Exception {
		checkTranspiledOutput("jsx/codeAttribute.pec");
	}

	@Test
	public void testInterpretedCodeElement() throws Exception {
		checkInterpretedOutput("jsx/codeElement.pec");
	}

	@Test
	public void testCompiledCodeElement() throws Exception {
		checkCompiledOutput("jsx/codeElement.pec");
	}

	@Test
	public void testTranspiledCodeElement() throws Exception {
		checkTranspiledOutput("jsx/codeElement.pec");
	}

	@Test
	public void testInterpretedDotName() throws Exception {
		checkInterpretedOutput("jsx/dotName.pec");
	}

	@Test
	public void testCompiledDotName() throws Exception {
		checkCompiledOutput("jsx/dotName.pec");
	}

	@Test
	public void testTranspiledDotName() throws Exception {
		checkTranspiledOutput("jsx/dotName.pec");
	}

	@Test
	public void testInterpretedEmpty() throws Exception {
		checkInterpretedOutput("jsx/empty.pec");
	}

	@Test
	public void testCompiledEmpty() throws Exception {
		checkCompiledOutput("jsx/empty.pec");
	}

	@Test
	public void testTranspiledEmpty() throws Exception {
		checkTranspiledOutput("jsx/empty.pec");
	}

	@Test
	public void testInterpretedEmptyAttribute() throws Exception {
		checkInterpretedOutput("jsx/emptyAttribute.pec");
	}

	@Test
	public void testCompiledEmptyAttribute() throws Exception {
		checkCompiledOutput("jsx/emptyAttribute.pec");
	}

	@Test
	public void testTranspiledEmptyAttribute() throws Exception {
		checkTranspiledOutput("jsx/emptyAttribute.pec");
	}

	@Test
	public void testInterpretedHyphenName() throws Exception {
		checkInterpretedOutput("jsx/hyphenName.pec");
	}

	@Test
	public void testCompiledHyphenName() throws Exception {
		checkCompiledOutput("jsx/hyphenName.pec");
	}

	@Test
	public void testTranspiledHyphenName() throws Exception {
		checkTranspiledOutput("jsx/hyphenName.pec");
	}

	@Test
	public void testInterpretedLiteralAttribute() throws Exception {
		checkInterpretedOutput("jsx/literalAttribute.pec");
	}

	@Test
	public void testCompiledLiteralAttribute() throws Exception {
		checkCompiledOutput("jsx/literalAttribute.pec");
	}

	@Test
	public void testTranspiledLiteralAttribute() throws Exception {
		checkTranspiledOutput("jsx/literalAttribute.pec");
	}

	@Test
	public void testInterpretedSelfClosingDiv() throws Exception {
		checkInterpretedOutput("jsx/selfClosingDiv.pec");
	}

	@Test
	public void testCompiledSelfClosingDiv() throws Exception {
		checkCompiledOutput("jsx/selfClosingDiv.pec");
	}

	@Test
	public void testTranspiledSelfClosingDiv() throws Exception {
		checkTranspiledOutput("jsx/selfClosingDiv.pec");
	}

	@Test
	public void testInterpretedSelfClosingEmptyAttribute() throws Exception {
		checkInterpretedOutput("jsx/selfClosingEmptyAttribute.pec");
	}

	@Test
	public void testCompiledSelfClosingEmptyAttribute() throws Exception {
		checkCompiledOutput("jsx/selfClosingEmptyAttribute.pec");
	}

	@Test
	public void testTranspiledSelfClosingEmptyAttribute() throws Exception {
		checkTranspiledOutput("jsx/selfClosingEmptyAttribute.pec");
	}

	@Test
	public void testInterpretedTextElement() throws Exception {
		checkInterpretedOutput("jsx/textElement.pec");
	}

	@Test
	public void testCompiledTextElement() throws Exception {
		checkCompiledOutput("jsx/textElement.pec");
	}

	@Test
	public void testTranspiledTextElement() throws Exception {
		checkTranspiledOutput("jsx/textElement.pec");
	}

}

