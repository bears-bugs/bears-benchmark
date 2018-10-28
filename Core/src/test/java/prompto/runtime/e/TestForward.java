package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestForward extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedForward() throws Exception {
		checkInterpretedOutput("forward/forward.pec");
	}

	@Test
	public void testCompiledForward() throws Exception {
		checkCompiledOutput("forward/forward.pec");
	}

	@Test
	public void testTranspiledForward() throws Exception {
		checkTranspiledOutput("forward/forward.pec");
	}

}

