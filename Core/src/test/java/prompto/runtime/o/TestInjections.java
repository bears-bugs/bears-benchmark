package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestInjections extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedExpressionInjection() throws Exception {
		checkInterpretedOutput("injections/expressionInjection.poc");
	}

	@Test
	public void testCompiledExpressionInjection() throws Exception {
		checkCompiledOutput("injections/expressionInjection.poc");
	}

	@Test
	public void testTranspiledExpressionInjection() throws Exception {
		checkTranspiledOutput("injections/expressionInjection.poc");
	}

}

