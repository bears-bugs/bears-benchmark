package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestTesting extends BaseOParserTest {

	@Test
	public void testAnd() throws Exception {
		compareResourceOEO("testing/and.poc");
	}

	@Test
	public void testContains() throws Exception {
		compareResourceOEO("testing/contains.poc");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceOEO("testing/greater.poc");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceOEO("testing/method.poc");
	}

	@Test
	public void testNegative() throws Exception {
		compareResourceOEO("testing/negative.poc");
	}

	@Test
	public void testNegativeError() throws Exception {
		compareResourceOEO("testing/negativeError.poc");
	}

	@Test
	public void testNot() throws Exception {
		compareResourceOEO("testing/not.poc");
	}

	@Test
	public void testOr() throws Exception {
		compareResourceOEO("testing/or.poc");
	}

	@Test
	public void testPositive() throws Exception {
		compareResourceOEO("testing/positive.poc");
	}

	@Test
	public void testPositiveError() throws Exception {
		compareResourceOEO("testing/positiveError.poc");
	}

}

