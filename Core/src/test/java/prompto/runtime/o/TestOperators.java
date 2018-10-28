package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestOperators extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAddAmount() throws Exception {
		checkInterpretedOutput("operators/addAmount.poc");
	}

	@Test
	public void testCompiledAddAmount() throws Exception {
		checkCompiledOutput("operators/addAmount.poc");
	}

	@Test
	public void testTranspiledAddAmount() throws Exception {
		checkTranspiledOutput("operators/addAmount.poc");
	}

	@Test
	public void testInterpretedDivAmount() throws Exception {
		checkInterpretedOutput("operators/divAmount.poc");
	}

	@Test
	public void testCompiledDivAmount() throws Exception {
		checkCompiledOutput("operators/divAmount.poc");
	}

	@Test
	public void testTranspiledDivAmount() throws Exception {
		checkTranspiledOutput("operators/divAmount.poc");
	}

	@Test
	public void testInterpretedIdivAmount() throws Exception {
		checkInterpretedOutput("operators/idivAmount.poc");
	}

	@Test
	public void testCompiledIdivAmount() throws Exception {
		checkCompiledOutput("operators/idivAmount.poc");
	}

	@Test
	public void testTranspiledIdivAmount() throws Exception {
		checkTranspiledOutput("operators/idivAmount.poc");
	}

	@Test
	public void testInterpretedModAmount() throws Exception {
		checkInterpretedOutput("operators/modAmount.poc");
	}

	@Test
	public void testCompiledModAmount() throws Exception {
		checkCompiledOutput("operators/modAmount.poc");
	}

	@Test
	public void testTranspiledModAmount() throws Exception {
		checkTranspiledOutput("operators/modAmount.poc");
	}

	@Test
	public void testInterpretedMultAmount() throws Exception {
		checkInterpretedOutput("operators/multAmount.poc");
	}

	@Test
	public void testCompiledMultAmount() throws Exception {
		checkCompiledOutput("operators/multAmount.poc");
	}

	@Test
	public void testTranspiledMultAmount() throws Exception {
		checkTranspiledOutput("operators/multAmount.poc");
	}

	@Test
	public void testInterpretedSubAmount() throws Exception {
		checkInterpretedOutput("operators/subAmount.poc");
	}

	@Test
	public void testCompiledSubAmount() throws Exception {
		checkCompiledOutput("operators/subAmount.poc");
	}

	@Test
	public void testTranspiledSubAmount() throws Exception {
		checkTranspiledOutput("operators/subAmount.poc");
	}

}

