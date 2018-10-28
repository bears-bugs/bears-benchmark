package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestPatterns extends BaseEParserTest {

	@Test
	public void testIntegerEnumeration() throws Exception {
		compareResourceEME("patterns/integerEnumeration.pec");
	}

	@Test
	public void testIntegerPattern() throws Exception {
		compareResourceEME("patterns/integerPattern.pec");
	}

	@Test
	public void testNegativeIntegerRange() throws Exception {
		compareResourceEME("patterns/negativeIntegerRange.pec");
	}

	@Test
	public void testPositiveIntegerRange() throws Exception {
		compareResourceEME("patterns/positiveIntegerRange.pec");
	}

	@Test
	public void testTextEnumeration() throws Exception {
		compareResourceEME("patterns/textEnumeration.pec");
	}

	@Test
	public void testTextPattern() throws Exception {
		compareResourceEME("patterns/textPattern.pec");
	}

}

