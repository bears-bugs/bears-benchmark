package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestMinus extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedMinusDecimal() throws Exception {
		checkInterpretedOutput("minus/minusDecimal.pec");
	}

	@Test
	public void testCompiledMinusDecimal() throws Exception {
		checkCompiledOutput("minus/minusDecimal.pec");
	}

	@Test
	public void testTranspiledMinusDecimal() throws Exception {
		checkTranspiledOutput("minus/minusDecimal.pec");
	}

	@Test
	public void testInterpretedMinusInteger() throws Exception {
		checkInterpretedOutput("minus/minusInteger.pec");
	}

	@Test
	public void testCompiledMinusInteger() throws Exception {
		checkCompiledOutput("minus/minusInteger.pec");
	}

	@Test
	public void testTranspiledMinusInteger() throws Exception {
		checkTranspiledOutput("minus/minusInteger.pec");
	}

	@Test
	public void testInterpretedMinusPeriod() throws Exception {
		checkInterpretedOutput("minus/minusPeriod.pec");
	}

	@Test
	public void testCompiledMinusPeriod() throws Exception {
		checkCompiledOutput("minus/minusPeriod.pec");
	}

	@Test
	public void testTranspiledMinusPeriod() throws Exception {
		checkTranspiledOutput("minus/minusPeriod.pec");
	}

}

