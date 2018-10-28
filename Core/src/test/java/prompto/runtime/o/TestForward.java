package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestForward extends BaseOParserTest {

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
		checkInterpretedOutput("forward/forward.poc");
	}

	@Test
	public void testCompiledForward() throws Exception {
		checkCompiledOutput("forward/forward.poc");
	}

	@Test
	public void testTranspiledForward() throws Exception {
		checkTranspiledOutput("forward/forward.poc");
	}

}

