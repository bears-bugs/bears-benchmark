package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSub extends BaseEParserTest {

	@Test
	public void testSubDate() throws Exception {
		compareResourceEOE("sub/subDate.pec");
	}

	@Test
	public void testSubDateTime() throws Exception {
		compareResourceEOE("sub/subDateTime.pec");
	}

	@Test
	public void testSubDecimal() throws Exception {
		compareResourceEOE("sub/subDecimal.pec");
	}

	@Test
	public void testSubDecimalEnum() throws Exception {
		compareResourceEOE("sub/subDecimalEnum.pec");
	}

	@Test
	public void testSubInteger() throws Exception {
		compareResourceEOE("sub/subInteger.pec");
	}

	@Test
	public void testSubIntegerEnum() throws Exception {
		compareResourceEOE("sub/subIntegerEnum.pec");
	}

	@Test
	public void testSubPeriod() throws Exception {
		compareResourceEOE("sub/subPeriod.pec");
	}

	@Test
	public void testSubTime() throws Exception {
		compareResourceEOE("sub/subTime.pec");
	}

}

