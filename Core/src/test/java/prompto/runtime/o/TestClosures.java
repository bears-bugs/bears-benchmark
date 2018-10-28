package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestClosures extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedGlobalClosureNoArg() throws Exception {
		checkInterpretedOutput("closures/globalClosureNoArg.poc");
	}

	@Test
	public void testCompiledGlobalClosureNoArg() throws Exception {
		checkCompiledOutput("closures/globalClosureNoArg.poc");
	}

	@Test
	public void testTranspiledGlobalClosureNoArg() throws Exception {
		checkTranspiledOutput("closures/globalClosureNoArg.poc");
	}

	@Test
	public void testInterpretedGlobalClosureWithArg() throws Exception {
		checkInterpretedOutput("closures/globalClosureWithArg.poc");
	}

	@Test
	public void testCompiledGlobalClosureWithArg() throws Exception {
		checkCompiledOutput("closures/globalClosureWithArg.poc");
	}

	@Test
	public void testTranspiledGlobalClosureWithArg() throws Exception {
		checkTranspiledOutput("closures/globalClosureWithArg.poc");
	}

	@Test
	public void testInterpretedInstanceClosureNoArg() throws Exception {
		checkInterpretedOutput("closures/instanceClosureNoArg.poc");
	}

	@Test
	public void testCompiledInstanceClosureNoArg() throws Exception {
		checkCompiledOutput("closures/instanceClosureNoArg.poc");
	}

	@Test
	public void testTranspiledInstanceClosureNoArg() throws Exception {
		checkTranspiledOutput("closures/instanceClosureNoArg.poc");
	}

}

