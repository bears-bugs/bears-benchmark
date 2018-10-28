package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestSub extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedSubDate() throws Exception {
		checkInterpretedOutput("sub/subDate.pec");
	}

	@Test
	public void testCompiledSubDate() throws Exception {
		checkCompiledOutput("sub/subDate.pec");
	}

	@Test
	public void testTranspiledSubDate() throws Exception {
		checkTranspiledOutput("sub/subDate.pec");
	}

	@Test
	public void testInterpretedSubDateTime() throws Exception {
		checkInterpretedOutput("sub/subDateTime.pec");
	}

	@Test
	public void testCompiledSubDateTime() throws Exception {
		checkCompiledOutput("sub/subDateTime.pec");
	}

	@Test
	public void testTranspiledSubDateTime() throws Exception {
		checkTranspiledOutput("sub/subDateTime.pec");
	}

	@Test
	public void testInterpretedSubDecimal() throws Exception {
		checkInterpretedOutput("sub/subDecimal.pec");
	}

	@Test
	public void testCompiledSubDecimal() throws Exception {
		checkCompiledOutput("sub/subDecimal.pec");
	}

	@Test
	public void testTranspiledSubDecimal() throws Exception {
		checkTranspiledOutput("sub/subDecimal.pec");
	}

	@Test
	public void testInterpretedSubDecimalEnum() throws Exception {
		checkInterpretedOutput("sub/subDecimalEnum.pec");
	}

	@Test
	public void testCompiledSubDecimalEnum() throws Exception {
		checkCompiledOutput("sub/subDecimalEnum.pec");
	}

	@Test
	public void testTranspiledSubDecimalEnum() throws Exception {
		checkTranspiledOutput("sub/subDecimalEnum.pec");
	}

	@Test
	public void testInterpretedSubInteger() throws Exception {
		checkInterpretedOutput("sub/subInteger.pec");
	}

	@Test
	public void testCompiledSubInteger() throws Exception {
		checkCompiledOutput("sub/subInteger.pec");
	}

	@Test
	public void testTranspiledSubInteger() throws Exception {
		checkTranspiledOutput("sub/subInteger.pec");
	}

	@Test
	public void testInterpretedSubIntegerEnum() throws Exception {
		checkInterpretedOutput("sub/subIntegerEnum.pec");
	}

	@Test
	public void testCompiledSubIntegerEnum() throws Exception {
		checkCompiledOutput("sub/subIntegerEnum.pec");
	}

	@Test
	public void testTranspiledSubIntegerEnum() throws Exception {
		checkTranspiledOutput("sub/subIntegerEnum.pec");
	}

	@Test
	public void testInterpretedSubPeriod() throws Exception {
		checkInterpretedOutput("sub/subPeriod.pec");
	}

	@Test
	public void testCompiledSubPeriod() throws Exception {
		checkCompiledOutput("sub/subPeriod.pec");
	}

	@Test
	public void testTranspiledSubPeriod() throws Exception {
		checkTranspiledOutput("sub/subPeriod.pec");
	}

	@Test
	public void testInterpretedSubTime() throws Exception {
		checkInterpretedOutput("sub/subTime.pec");
	}

	@Test
	public void testCompiledSubTime() throws Exception {
		checkCompiledOutput("sub/subTime.pec");
	}

	@Test
	public void testTranspiledSubTime() throws Exception {
		checkTranspiledOutput("sub/subTime.pec");
	}

}

