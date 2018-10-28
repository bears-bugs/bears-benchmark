package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestPatterns extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedIntegerEnumeration() throws Exception {
		checkInterpretedOutput("patterns/integerEnumeration.pec");
	}

	@Test
	public void testCompiledIntegerEnumeration() throws Exception {
		checkCompiledOutput("patterns/integerEnumeration.pec");
	}

	@Test
	public void testTranspiledIntegerEnumeration() throws Exception {
		checkTranspiledOutput("patterns/integerEnumeration.pec");
	}

	@Test
	public void testInterpretedIntegerPattern() throws Exception {
		checkInterpretedOutput("patterns/integerPattern.pec");
	}

	@Test
	public void testCompiledIntegerPattern() throws Exception {
		checkCompiledOutput("patterns/integerPattern.pec");
	}

	@Test
	public void testTranspiledIntegerPattern() throws Exception {
		checkTranspiledOutput("patterns/integerPattern.pec");
	}

	@Test
	public void testInterpretedNegativeIntegerRange() throws Exception {
		checkInterpretedOutput("patterns/negativeIntegerRange.pec");
	}

	@Test
	public void testCompiledNegativeIntegerRange() throws Exception {
		checkCompiledOutput("patterns/negativeIntegerRange.pec");
	}

	@Test
	public void testTranspiledNegativeIntegerRange() throws Exception {
		checkTranspiledOutput("patterns/negativeIntegerRange.pec");
	}

	@Test
	public void testInterpretedPositiveIntegerRange() throws Exception {
		checkInterpretedOutput("patterns/positiveIntegerRange.pec");
	}

	@Test
	public void testCompiledPositiveIntegerRange() throws Exception {
		checkCompiledOutput("patterns/positiveIntegerRange.pec");
	}

	@Test
	public void testTranspiledPositiveIntegerRange() throws Exception {
		checkTranspiledOutput("patterns/positiveIntegerRange.pec");
	}

	@Test
	public void testInterpretedTextEnumeration() throws Exception {
		checkInterpretedOutput("patterns/textEnumeration.pec");
	}

	@Test
	public void testCompiledTextEnumeration() throws Exception {
		checkCompiledOutput("patterns/textEnumeration.pec");
	}

	@Test
	public void testTranspiledTextEnumeration() throws Exception {
		checkTranspiledOutput("patterns/textEnumeration.pec");
	}

	@Test
	public void testInterpretedTextPattern() throws Exception {
		checkInterpretedOutput("patterns/textPattern.pec");
	}

	@Test
	public void testCompiledTextPattern() throws Exception {
		checkCompiledOutput("patterns/textPattern.pec");
	}

	@Test
	public void testTranspiledTextPattern() throws Exception {
		checkTranspiledOutput("patterns/textPattern.pec");
	}

}

