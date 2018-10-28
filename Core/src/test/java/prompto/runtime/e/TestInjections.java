package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestInjections extends BaseEParserTest {

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
		checkInterpretedOutput("injections/expressionInjection.pec");
	}

	@Test
	public void testCompiledExpressionInjection() throws Exception {
		checkCompiledOutput("injections/expressionInjection.pec");
	}

	@Test
	public void testTranspiledExpressionInjection() throws Exception {
		checkTranspiledOutput("injections/expressionInjection.pec");
	}

}

