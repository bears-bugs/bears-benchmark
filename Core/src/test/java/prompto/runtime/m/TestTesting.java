package prompto.runtime.m;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.m.BaseMParserTest;
import prompto.runtime.utils.Out;

public class TestTesting extends BaseMParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAnd() throws Exception {
		checkInterpretedOutput("testing/and.pmc");
	}

	@Test
	public void testCompiledAnd() throws Exception {
		checkCompiledOutput("testing/and.pmc");
	}

	@Test
	public void testTranspiledAnd() throws Exception {
		checkTranspiledOutput("testing/and.pmc");
	}

	@Test
	public void testInterpretedContains() throws Exception {
		checkInterpretedOutput("testing/contains.pmc");
	}

	@Test
	public void testCompiledContains() throws Exception {
		checkCompiledOutput("testing/contains.pmc");
	}

	@Test
	public void testTranspiledContains() throws Exception {
		checkTranspiledOutput("testing/contains.pmc");
	}

	@Test
	public void testInterpretedGreater() throws Exception {
		checkInterpretedOutput("testing/greater.pmc");
	}

	@Test
	public void testCompiledGreater() throws Exception {
		checkCompiledOutput("testing/greater.pmc");
	}

	@Test
	public void testTranspiledGreater() throws Exception {
		checkTranspiledOutput("testing/greater.pmc");
	}

	@Test
	public void testInterpretedMethod() throws Exception {
		checkInterpretedOutput("testing/method.pmc");
	}

	@Test
	public void testCompiledMethod() throws Exception {
		checkCompiledOutput("testing/method.pmc");
	}

	@Test
	public void testTranspiledMethod() throws Exception {
		checkTranspiledOutput("testing/method.pmc");
	}

	@Test
	public void testInterpretedNegative() throws Exception {
		checkInterpretedOutput("testing/negative.pmc");
	}

	@Test
	public void testCompiledNegative() throws Exception {
		checkCompiledOutput("testing/negative.pmc");
	}

	@Test
	public void testTranspiledNegative() throws Exception {
		checkTranspiledOutput("testing/negative.pmc");
	}

	@Test
	public void testInterpretedNegativeError() throws Exception {
		checkInterpretedOutput("testing/negativeError.pmc");
	}

	@Test
	public void testCompiledNegativeError() throws Exception {
		checkCompiledOutput("testing/negativeError.pmc");
	}

	@Test
	public void testTranspiledNegativeError() throws Exception {
		checkTranspiledOutput("testing/negativeError.pmc");
	}

	@Test
	public void testInterpretedNot() throws Exception {
		checkInterpretedOutput("testing/not.pmc");
	}

	@Test
	public void testCompiledNot() throws Exception {
		checkCompiledOutput("testing/not.pmc");
	}

	@Test
	public void testTranspiledNot() throws Exception {
		checkTranspiledOutput("testing/not.pmc");
	}

	@Test
	public void testInterpretedOr() throws Exception {
		checkInterpretedOutput("testing/or.pmc");
	}

	@Test
	public void testCompiledOr() throws Exception {
		checkCompiledOutput("testing/or.pmc");
	}

	@Test
	public void testTranspiledOr() throws Exception {
		checkTranspiledOutput("testing/or.pmc");
	}

	@Test
	public void testInterpretedPositive() throws Exception {
		checkInterpretedOutput("testing/positive.pmc");
	}

	@Test
	public void testCompiledPositive() throws Exception {
		checkCompiledOutput("testing/positive.pmc");
	}

	@Test
	public void testTranspiledPositive() throws Exception {
		checkTranspiledOutput("testing/positive.pmc");
	}

	@Test
	public void testInterpretedPositiveError() throws Exception {
		checkInterpretedOutput("testing/positiveError.pmc");
	}

	@Test
	public void testCompiledPositiveError() throws Exception {
		checkCompiledOutput("testing/positiveError.pmc");
	}

	@Test
	public void testTranspiledPositiveError() throws Exception {
		checkTranspiledOutput("testing/positiveError.pmc");
	}

}

