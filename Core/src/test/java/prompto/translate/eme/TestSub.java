package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSub extends BaseEParserTest {

	@Test
	public void testSubDate() throws Exception {
		compareResourceEME("sub/subDate.pec");
	}

	@Test
	public void testSubDateTime() throws Exception {
		compareResourceEME("sub/subDateTime.pec");
	}

	@Test
	public void testSubDecimal() throws Exception {
		compareResourceEME("sub/subDecimal.pec");
	}

	@Test
	public void testSubDecimalEnum() throws Exception {
		compareResourceEME("sub/subDecimalEnum.pec");
	}

	@Test
	public void testSubInteger() throws Exception {
		compareResourceEME("sub/subInteger.pec");
	}

	@Test
	public void testSubIntegerEnum() throws Exception {
		compareResourceEME("sub/subIntegerEnum.pec");
	}

	@Test
	public void testSubPeriod() throws Exception {
		compareResourceEME("sub/subPeriod.pec");
	}

	@Test
	public void testSubTime() throws Exception {
		compareResourceEME("sub/subTime.pec");
	}

}

