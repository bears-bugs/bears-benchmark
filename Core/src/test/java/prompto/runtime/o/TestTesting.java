package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestTesting extends BaseOParserTest {

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
		checkInterpretedOutput("testing/and.poc");
	}

	@Test
	public void testCompiledAnd() throws Exception {
		checkCompiledOutput("testing/and.poc");
	}

	@Test
	public void testTranspiledAnd() throws Exception {
		checkTranspiledOutput("testing/and.poc");
	}

	@Test
	public void testInterpretedContains() throws Exception {
		checkInterpretedOutput("testing/contains.poc");
	}

	@Test
	public void testCompiledContains() throws Exception {
		checkCompiledOutput("testing/contains.poc");
	}

	@Test
	public void testTranspiledContains() throws Exception {
		checkTranspiledOutput("testing/contains.poc");
	}

	@Test
	public void testInterpretedGreater() throws Exception {
		checkInterpretedOutput("testing/greater.poc");
	}

	@Test
	public void testCompiledGreater() throws Exception {
		checkCompiledOutput("testing/greater.poc");
	}

	@Test
	public void testTranspiledGreater() throws Exception {
		checkTranspiledOutput("testing/greater.poc");
	}

	@Test
	public void testInterpretedMethod() throws Exception {
		checkInterpretedOutput("testing/method.poc");
	}

	@Test
	public void testCompiledMethod() throws Exception {
		checkCompiledOutput("testing/method.poc");
	}

	@Test
	public void testTranspiledMethod() throws Exception {
		checkTranspiledOutput("testing/method.poc");
	}

	@Test
	public void testInterpretedNegative() throws Exception {
		checkInterpretedOutput("testing/negative.poc");
	}

	@Test
	public void testCompiledNegative() throws Exception {
		checkCompiledOutput("testing/negative.poc");
	}

	@Test
	public void testTranspiledNegative() throws Exception {
		checkTranspiledOutput("testing/negative.poc");
	}

	@Test
	public void testInterpretedNegativeError() throws Exception {
		checkInterpretedOutput("testing/negativeError.poc");
	}

	@Test
	public void testCompiledNegativeError() throws Exception {
		checkCompiledOutput("testing/negativeError.poc");
	}

	@Test
	public void testTranspiledNegativeError() throws Exception {
		checkTranspiledOutput("testing/negativeError.poc");
	}

	@Test
	public void testInterpretedNot() throws Exception {
		checkInterpretedOutput("testing/not.poc");
	}

	@Test
	public void testCompiledNot() throws Exception {
		checkCompiledOutput("testing/not.poc");
	}

	@Test
	public void testTranspiledNot() throws Exception {
		checkTranspiledOutput("testing/not.poc");
	}

	@Test
	public void testInterpretedOr() throws Exception {
		checkInterpretedOutput("testing/or.poc");
	}

	@Test
	public void testCompiledOr() throws Exception {
		checkCompiledOutput("testing/or.poc");
	}

	@Test
	public void testTranspiledOr() throws Exception {
		checkTranspiledOutput("testing/or.poc");
	}

	@Test
	public void testInterpretedPositive() throws Exception {
		checkInterpretedOutput("testing/positive.poc");
	}

	@Test
	public void testCompiledPositive() throws Exception {
		checkCompiledOutput("testing/positive.poc");
	}

	@Test
	public void testTranspiledPositive() throws Exception {
		checkTranspiledOutput("testing/positive.poc");
	}

	@Test
	public void testInterpretedPositiveError() throws Exception {
		checkInterpretedOutput("testing/positiveError.poc");
	}

	@Test
	public void testCompiledPositiveError() throws Exception {
		checkCompiledOutput("testing/positiveError.poc");
	}

	@Test
	public void testTranspiledPositiveError() throws Exception {
		checkTranspiledOutput("testing/positiveError.poc");
	}

}

