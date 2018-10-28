package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestTesting extends BaseEParserTest {

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
		checkInterpretedOutput("testing/and.pec");
	}

	@Test
	public void testCompiledAnd() throws Exception {
		checkCompiledOutput("testing/and.pec");
	}

	@Test
	public void testTranspiledAnd() throws Exception {
		checkTranspiledOutput("testing/and.pec");
	}

	@Test
	public void testInterpretedContains() throws Exception {
		checkInterpretedOutput("testing/contains.pec");
	}

	@Test
	public void testCompiledContains() throws Exception {
		checkCompiledOutput("testing/contains.pec");
	}

	@Test
	public void testTranspiledContains() throws Exception {
		checkTranspiledOutput("testing/contains.pec");
	}

	@Test
	public void testInterpretedGreater() throws Exception {
		checkInterpretedOutput("testing/greater.pec");
	}

	@Test
	public void testCompiledGreater() throws Exception {
		checkCompiledOutput("testing/greater.pec");
	}

	@Test
	public void testTranspiledGreater() throws Exception {
		checkTranspiledOutput("testing/greater.pec");
	}

	@Test
	public void testInterpretedMethod() throws Exception {
		checkInterpretedOutput("testing/method.pec");
	}

	@Test
	public void testCompiledMethod() throws Exception {
		checkCompiledOutput("testing/method.pec");
	}

	@Test
	public void testTranspiledMethod() throws Exception {
		checkTranspiledOutput("testing/method.pec");
	}

	@Test
	public void testInterpretedNegative() throws Exception {
		checkInterpretedOutput("testing/negative.pec");
	}

	@Test
	public void testCompiledNegative() throws Exception {
		checkCompiledOutput("testing/negative.pec");
	}

	@Test
	public void testTranspiledNegative() throws Exception {
		checkTranspiledOutput("testing/negative.pec");
	}

	@Test
	public void testInterpretedNegativeError() throws Exception {
		checkInterpretedOutput("testing/negativeError.pec");
	}

	@Test
	public void testCompiledNegativeError() throws Exception {
		checkCompiledOutput("testing/negativeError.pec");
	}

	@Test
	public void testTranspiledNegativeError() throws Exception {
		checkTranspiledOutput("testing/negativeError.pec");
	}

	@Test
	public void testInterpretedNot() throws Exception {
		checkInterpretedOutput("testing/not.pec");
	}

	@Test
	public void testCompiledNot() throws Exception {
		checkCompiledOutput("testing/not.pec");
	}

	@Test
	public void testTranspiledNot() throws Exception {
		checkTranspiledOutput("testing/not.pec");
	}

	@Test
	public void testInterpretedOr() throws Exception {
		checkInterpretedOutput("testing/or.pec");
	}

	@Test
	public void testCompiledOr() throws Exception {
		checkCompiledOutput("testing/or.pec");
	}

	@Test
	public void testTranspiledOr() throws Exception {
		checkTranspiledOutput("testing/or.pec");
	}

	@Test
	public void testInterpretedPositive() throws Exception {
		checkInterpretedOutput("testing/positive.pec");
	}

	@Test
	public void testCompiledPositive() throws Exception {
		checkCompiledOutput("testing/positive.pec");
	}

	@Test
	public void testTranspiledPositive() throws Exception {
		checkTranspiledOutput("testing/positive.pec");
	}

	@Test
	public void testInterpretedPositiveError() throws Exception {
		checkInterpretedOutput("testing/positiveError.pec");
	}

	@Test
	public void testCompiledPositiveError() throws Exception {
		checkCompiledOutput("testing/positiveError.pec");
	}

	@Test
	public void testTranspiledPositiveError() throws Exception {
		checkTranspiledOutput("testing/positiveError.pec");
	}

}

