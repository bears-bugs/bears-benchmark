package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSub extends BaseOParserTest {

	@Test
	public void testSubDate() throws Exception {
		compareResourceOEO("sub/subDate.poc");
	}

	@Test
	public void testSubDateTime() throws Exception {
		compareResourceOEO("sub/subDateTime.poc");
	}

	@Test
	public void testSubDecimal() throws Exception {
		compareResourceOEO("sub/subDecimal.poc");
	}

	@Test
	public void testSubInteger() throws Exception {
		compareResourceOEO("sub/subInteger.poc");
	}

	@Test
	public void testSubPeriod() throws Exception {
		compareResourceOEO("sub/subPeriod.poc");
	}

	@Test
	public void testSubTime() throws Exception {
		compareResourceOEO("sub/subTime.poc");
	}

}

