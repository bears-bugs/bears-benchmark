package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestPatterns extends BaseOParserTest {

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
		checkInterpretedOutput("patterns/integerEnumeration.poc");
	}

	@Test
	public void testCompiledIntegerEnumeration() throws Exception {
		checkCompiledOutput("patterns/integerEnumeration.poc");
	}

	@Test
	public void testTranspiledIntegerEnumeration() throws Exception {
		checkTranspiledOutput("patterns/integerEnumeration.poc");
	}

	@Test
	public void testInterpretedIntegerPattern() throws Exception {
		checkInterpretedOutput("patterns/integerPattern.poc");
	}

	@Test
	public void testCompiledIntegerPattern() throws Exception {
		checkCompiledOutput("patterns/integerPattern.poc");
	}

	@Test
	public void testTranspiledIntegerPattern() throws Exception {
		checkTranspiledOutput("patterns/integerPattern.poc");
	}

	@Test
	public void testInterpretedNegativeIntegerRange() throws Exception {
		checkInterpretedOutput("patterns/negativeIntegerRange.poc");
	}

	@Test
	public void testCompiledNegativeIntegerRange() throws Exception {
		checkCompiledOutput("patterns/negativeIntegerRange.poc");
	}

	@Test
	public void testTranspiledNegativeIntegerRange() throws Exception {
		checkTranspiledOutput("patterns/negativeIntegerRange.poc");
	}

	@Test
	public void testInterpretedPositiveIntegerRange() throws Exception {
		checkInterpretedOutput("patterns/positiveIntegerRange.poc");
	}

	@Test
	public void testCompiledPositiveIntegerRange() throws Exception {
		checkCompiledOutput("patterns/positiveIntegerRange.poc");
	}

	@Test
	public void testTranspiledPositiveIntegerRange() throws Exception {
		checkTranspiledOutput("patterns/positiveIntegerRange.poc");
	}

	@Test
	public void testInterpretedTextEnumeration() throws Exception {
		checkInterpretedOutput("patterns/textEnumeration.poc");
	}

	@Test
	public void testCompiledTextEnumeration() throws Exception {
		checkCompiledOutput("patterns/textEnumeration.poc");
	}

	@Test
	public void testTranspiledTextEnumeration() throws Exception {
		checkTranspiledOutput("patterns/textEnumeration.poc");
	}

	@Test
	public void testInterpretedTextPattern() throws Exception {
		checkInterpretedOutput("patterns/textPattern.poc");
	}

	@Test
	public void testCompiledTextPattern() throws Exception {
		checkCompiledOutput("patterns/textPattern.poc");
	}

	@Test
	public void testTranspiledTextPattern() throws Exception {
		checkTranspiledOutput("patterns/textPattern.poc");
	}

}

