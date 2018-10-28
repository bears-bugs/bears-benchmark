package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestTesting extends BaseEParserTest {

	@Test
	public void testAnd() throws Exception {
		compareResourceEME("testing/and.pec");
	}

	@Test
	public void testContains() throws Exception {
		compareResourceEME("testing/contains.pec");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceEME("testing/greater.pec");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceEME("testing/method.pec");
	}

	@Test
	public void testNegative() throws Exception {
		compareResourceEME("testing/negative.pec");
	}

	@Test
	public void testNegativeError() throws Exception {
		compareResourceEME("testing/negativeError.pec");
	}

	@Test
	public void testNot() throws Exception {
		compareResourceEME("testing/not.pec");
	}

	@Test
	public void testOr() throws Exception {
		compareResourceEME("testing/or.pec");
	}

	@Test
	public void testPositive() throws Exception {
		compareResourceEME("testing/positive.pec");
	}

	@Test
	public void testPositiveError() throws Exception {
		compareResourceEME("testing/positiveError.pec");
	}

}

