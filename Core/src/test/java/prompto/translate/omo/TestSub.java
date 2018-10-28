package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSub extends BaseOParserTest {

	@Test
	public void testSubDate() throws Exception {
		compareResourceOMO("sub/subDate.poc");
	}

	@Test
	public void testSubDateTime() throws Exception {
		compareResourceOMO("sub/subDateTime.poc");
	}

	@Test
	public void testSubDecimal() throws Exception {
		compareResourceOMO("sub/subDecimal.poc");
	}

	@Test
	public void testSubInteger() throws Exception {
		compareResourceOMO("sub/subInteger.poc");
	}

	@Test
	public void testSubPeriod() throws Exception {
		compareResourceOMO("sub/subPeriod.poc");
	}

	@Test
	public void testSubTime() throws Exception {
		compareResourceOMO("sub/subTime.poc");
	}

}

