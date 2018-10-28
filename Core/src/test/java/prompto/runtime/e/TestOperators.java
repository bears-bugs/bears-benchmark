package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestOperators extends BaseEParserTest {

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
		checkInterpretedOutput("operators/addAmount.pec");
	}

	@Test
	public void testCompiledAddAmount() throws Exception {
		checkCompiledOutput("operators/addAmount.pec");
	}

	@Test
	public void testTranspiledAddAmount() throws Exception {
		checkTranspiledOutput("operators/addAmount.pec");
	}

	@Test
	public void testInterpretedDivAmount() throws Exception {
		checkInterpretedOutput("operators/divAmount.pec");
	}

	@Test
	public void testCompiledDivAmount() throws Exception {
		checkCompiledOutput("operators/divAmount.pec");
	}

	@Test
	public void testTranspiledDivAmount() throws Exception {
		checkTranspiledOutput("operators/divAmount.pec");
	}

	@Test
	public void testInterpretedIdivAmount() throws Exception {
		checkInterpretedOutput("operators/idivAmount.pec");
	}

	@Test
	public void testCompiledIdivAmount() throws Exception {
		checkCompiledOutput("operators/idivAmount.pec");
	}

	@Test
	public void testTranspiledIdivAmount() throws Exception {
		checkTranspiledOutput("operators/idivAmount.pec");
	}

	@Test
	public void testInterpretedModAmount() throws Exception {
		checkInterpretedOutput("operators/modAmount.pec");
	}

	@Test
	public void testCompiledModAmount() throws Exception {
		checkCompiledOutput("operators/modAmount.pec");
	}

	@Test
	public void testTranspiledModAmount() throws Exception {
		checkTranspiledOutput("operators/modAmount.pec");
	}

	@Test
	public void testInterpretedMultAmount() throws Exception {
		checkInterpretedOutput("operators/multAmount.pec");
	}

	@Test
	public void testCompiledMultAmount() throws Exception {
		checkCompiledOutput("operators/multAmount.pec");
	}

	@Test
	public void testTranspiledMultAmount() throws Exception {
		checkTranspiledOutput("operators/multAmount.pec");
	}

	@Test
	public void testInterpretedSubAmount() throws Exception {
		checkInterpretedOutput("operators/subAmount.pec");
	}

	@Test
	public void testCompiledSubAmount() throws Exception {
		checkCompiledOutput("operators/subAmount.pec");
	}

	@Test
	public void testTranspiledSubAmount() throws Exception {
		checkTranspiledOutput("operators/subAmount.pec");
	}

}

