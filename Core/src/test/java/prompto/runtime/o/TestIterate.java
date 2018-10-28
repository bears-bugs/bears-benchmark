package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestIterate extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedForEachIntegerList() throws Exception {
		checkInterpretedOutput("iterate/forEachIntegerList.poc");
	}

	@Test
	public void testCompiledForEachIntegerList() throws Exception {
		checkCompiledOutput("iterate/forEachIntegerList.poc");
	}

	@Test
	public void testTranspiledForEachIntegerList() throws Exception {
		checkTranspiledOutput("iterate/forEachIntegerList.poc");
	}

}

