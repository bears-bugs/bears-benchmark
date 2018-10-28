package prompto.runtime.m;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.m.BaseMParserTest;
import prompto.runtime.utils.Out;

public class TestJsx extends BaseMParserTest {

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
		checkInterpretedOutput("jsx/childElement.pmc");
	}

	@Test
	public void testCompiledChildElement() throws Exception {
		checkCompiledOutput("jsx/childElement.pmc");
	}

	@Test
	public void testTranspiledChildElement() throws Exception {
		checkTranspiledOutput("jsx/childElement.pmc");
	}

	@Test
	public void testInterpretedCodeAttribute() throws Exception {
		checkInterpretedOutput("jsx/codeAttribute.pmc");
	}

	@Test
	public void testCompiledCodeAttribute() throws Exception {
		checkCompiledOutput("jsx/codeAttribute.pmc");
	}

	@Test
	public void testTranspiledCodeAttribute() throws Exception {
		checkTranspiledOutput("jsx/codeAttribute.pmc");
	}

	@Test
	public void testInterpretedCodeElement() throws Exception {
		checkInterpretedOutput("jsx/codeElement.pmc");
	}

	@Test
	public void testCompiledCodeElement() throws Exception {
		checkCompiledOutput("jsx/codeElement.pmc");
	}

	@Test
	public void testTranspiledCodeElement() throws Exception {
		checkTranspiledOutput("jsx/codeElement.pmc");
	}

	@Test
	public void testInterpretedDotName() throws Exception {
		checkInterpretedOutput("jsx/dotName.pmc");
	}

	@Test
	public void testCompiledDotName() throws Exception {
		checkCompiledOutput("jsx/dotName.pmc");
	}

	@Test
	public void testTranspiledDotName() throws Exception {
		checkTranspiledOutput("jsx/dotName.pmc");
	}

	@Test
	public void testInterpretedEmpty() throws Exception {
		checkInterpretedOutput("jsx/empty.pmc");
	}

	@Test
	public void testCompiledEmpty() throws Exception {
		checkCompiledOutput("jsx/empty.pmc");
	}

	@Test
	public void testTranspiledEmpty() throws Exception {
		checkTranspiledOutput("jsx/empty.pmc");
	}

	@Test
	public void testInterpretedEmptyAttribute() throws Exception {
		checkInterpretedOutput("jsx/emptyAttribute.pmc");
	}

	@Test
	public void testCompiledEmptyAttribute() throws Exception {
		checkCompiledOutput("jsx/emptyAttribute.pmc");
	}

	@Test
	public void testTranspiledEmptyAttribute() throws Exception {
		checkTranspiledOutput("jsx/emptyAttribute.pmc");
	}

	@Test
	public void testInterpretedHyphenName() throws Exception {
		checkInterpretedOutput("jsx/hyphenName.pmc");
	}

	@Test
	public void testCompiledHyphenName() throws Exception {
		checkCompiledOutput("jsx/hyphenName.pmc");
	}

	@Test
	public void testTranspiledHyphenName() throws Exception {
		checkTranspiledOutput("jsx/hyphenName.pmc");
	}

	@Test
	public void testInterpretedLiteralAttribute() throws Exception {
		checkInterpretedOutput("jsx/literalAttribute.pmc");
	}

	@Test
	public void testCompiledLiteralAttribute() throws Exception {
		checkCompiledOutput("jsx/literalAttribute.pmc");
	}

	@Test
	public void testTranspiledLiteralAttribute() throws Exception {
		checkTranspiledOutput("jsx/literalAttribute.pmc");
	}

	@Test
	public void testInterpretedSelfClosingDiv() throws Exception {
		checkInterpretedOutput("jsx/selfClosingDiv.pmc");
	}

	@Test
	public void testCompiledSelfClosingDiv() throws Exception {
		checkCompiledOutput("jsx/selfClosingDiv.pmc");
	}

	@Test
	public void testTranspiledSelfClosingDiv() throws Exception {
		checkTranspiledOutput("jsx/selfClosingDiv.pmc");
	}

	@Test
	public void testInterpretedSelfClosingEmptyAttribute() throws Exception {
		checkInterpretedOutput("jsx/selfClosingEmptyAttribute.pmc");
	}

	@Test
	public void testCompiledSelfClosingEmptyAttribute() throws Exception {
		checkCompiledOutput("jsx/selfClosingEmptyAttribute.pmc");
	}

	@Test
	public void testTranspiledSelfClosingEmptyAttribute() throws Exception {
		checkTranspiledOutput("jsx/selfClosingEmptyAttribute.pmc");
	}

	@Test
	public void testInterpretedTextElement() throws Exception {
		checkInterpretedOutput("jsx/textElement.pmc");
	}

	@Test
	public void testCompiledTextElement() throws Exception {
		checkCompiledOutput("jsx/textElement.pmc");
	}

	@Test
	public void testTranspiledTextElement() throws Exception {
		checkTranspiledOutput("jsx/textElement.pmc");
	}

}

