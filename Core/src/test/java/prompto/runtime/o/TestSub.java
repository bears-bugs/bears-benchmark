package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestSub extends BaseOParserTest {

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
		checkInterpretedOutput("sub/subDate.poc");
	}

	@Test
	public void testCompiledSubDate() throws Exception {
		checkCompiledOutput("sub/subDate.poc");
	}

	@Test
	public void testTranspiledSubDate() throws Exception {
		checkTranspiledOutput("sub/subDate.poc");
	}

	@Test
	public void testInterpretedSubDateTime() throws Exception {
		checkInterpretedOutput("sub/subDateTime.poc");
	}

	@Test
	public void testCompiledSubDateTime() throws Exception {
		checkCompiledOutput("sub/subDateTime.poc");
	}

	@Test
	public void testTranspiledSubDateTime() throws Exception {
		checkTranspiledOutput("sub/subDateTime.poc");
	}

	@Test
	public void testInterpretedSubDecimal() throws Exception {
		checkInterpretedOutput("sub/subDecimal.poc");
	}

	@Test
	public void testCompiledSubDecimal() throws Exception {
		checkCompiledOutput("sub/subDecimal.poc");
	}

	@Test
	public void testTranspiledSubDecimal() throws Exception {
		checkTranspiledOutput("sub/subDecimal.poc");
	}

	@Test
	public void testInterpretedSubInteger() throws Exception {
		checkInterpretedOutput("sub/subInteger.poc");
	}

	@Test
	public void testCompiledSubInteger() throws Exception {
		checkCompiledOutput("sub/subInteger.poc");
	}

	@Test
	public void testTranspiledSubInteger() throws Exception {
		checkTranspiledOutput("sub/subInteger.poc");
	}

	@Test
	public void testInterpretedSubPeriod() throws Exception {
		checkInterpretedOutput("sub/subPeriod.poc");
	}

	@Test
	public void testCompiledSubPeriod() throws Exception {
		checkCompiledOutput("sub/subPeriod.poc");
	}

	@Test
	public void testTranspiledSubPeriod() throws Exception {
		checkTranspiledOutput("sub/subPeriod.poc");
	}

	@Test
	public void testInterpretedSubTime() throws Exception {
		checkInterpretedOutput("sub/subTime.poc");
	}

	@Test
	public void testCompiledSubTime() throws Exception {
		checkCompiledOutput("sub/subTime.poc");
	}

	@Test
	public void testTranspiledSubTime() throws Exception {
		checkTranspiledOutput("sub/subTime.poc");
	}

}

