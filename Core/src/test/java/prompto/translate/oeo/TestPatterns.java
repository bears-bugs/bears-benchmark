package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestPatterns extends BaseOParserTest {

	@Test
	public void testIntegerEnumeration() throws Exception {
		compareResourceOEO("patterns/integerEnumeration.poc");
	}

	@Test
	public void testIntegerPattern() throws Exception {
		compareResourceOEO("patterns/integerPattern.poc");
	}

	@Test
	public void testNegativeIntegerRange() throws Exception {
		compareResourceOEO("patterns/negativeIntegerRange.poc");
	}

	@Test
	public void testPositiveIntegerRange() throws Exception {
		compareResourceOEO("patterns/positiveIntegerRange.poc");
	}

	@Test
	public void testTextEnumeration() throws Exception {
		compareResourceOEO("patterns/textEnumeration.poc");
	}

	@Test
	public void testTextPattern() throws Exception {
		compareResourceOEO("patterns/textPattern.poc");
	}

}

