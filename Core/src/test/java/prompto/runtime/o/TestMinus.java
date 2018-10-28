package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestMinus extends BaseOParserTest {

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
		checkInterpretedOutput("minus/minusDecimal.poc");
	}

	@Test
	public void testCompiledMinusDecimal() throws Exception {
		checkCompiledOutput("minus/minusDecimal.poc");
	}

	@Test
	public void testTranspiledMinusDecimal() throws Exception {
		checkTranspiledOutput("minus/minusDecimal.poc");
	}

	@Test
	public void testInterpretedMinusInteger() throws Exception {
		checkInterpretedOutput("minus/minusInteger.poc");
	}

	@Test
	public void testCompiledMinusInteger() throws Exception {
		checkCompiledOutput("minus/minusInteger.poc");
	}

	@Test
	public void testTranspiledMinusInteger() throws Exception {
		checkTranspiledOutput("minus/minusInteger.poc");
	}

	@Test
	public void testInterpretedMinusPeriod() throws Exception {
		checkInterpretedOutput("minus/minusPeriod.poc");
	}

	@Test
	public void testCompiledMinusPeriod() throws Exception {
		checkCompiledOutput("minus/minusPeriod.poc");
	}

	@Test
	public void testTranspiledMinusPeriod() throws Exception {
		checkTranspiledOutput("minus/minusPeriod.poc");
	}

}

