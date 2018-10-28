package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestTesting extends BaseEParserTest {

	@Test
	public void testAnd() throws Exception {
		compareResourceEOE("testing/and.pec");
	}

	@Test
	public void testContains() throws Exception {
		compareResourceEOE("testing/contains.pec");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceEOE("testing/greater.pec");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceEOE("testing/method.pec");
	}

	@Test
	public void testNegative() throws Exception {
		compareResourceEOE("testing/negative.pec");
	}

	@Test
	public void testNegativeError() throws Exception {
		compareResourceEOE("testing/negativeError.pec");
	}

	@Test
	public void testNot() throws Exception {
		compareResourceEOE("testing/not.pec");
	}

	@Test
	public void testOr() throws Exception {
		compareResourceEOE("testing/or.pec");
	}

	@Test
	public void testPositive() throws Exception {
		compareResourceEOE("testing/positive.pec");
	}

	@Test
	public void testPositiveError() throws Exception {
		compareResourceEOE("testing/positiveError.pec");
	}

}

