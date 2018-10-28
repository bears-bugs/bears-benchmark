package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestTesting extends BaseOParserTest {

	@Test
	public void testAnd() throws Exception {
		compareResourceOMO("testing/and.poc");
	}

	@Test
	public void testContains() throws Exception {
		compareResourceOMO("testing/contains.poc");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceOMO("testing/greater.poc");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceOMO("testing/method.poc");
	}

	@Test
	public void testNegative() throws Exception {
		compareResourceOMO("testing/negative.poc");
	}

	@Test
	public void testNegativeError() throws Exception {
		compareResourceOMO("testing/negativeError.poc");
	}

	@Test
	public void testNot() throws Exception {
		compareResourceOMO("testing/not.poc");
	}

	@Test
	public void testOr() throws Exception {
		compareResourceOMO("testing/or.poc");
	}

	@Test
	public void testPositive() throws Exception {
		compareResourceOMO("testing/positive.poc");
	}

	@Test
	public void testPositiveError() throws Exception {
		compareResourceOMO("testing/positiveError.poc");
	}

}

