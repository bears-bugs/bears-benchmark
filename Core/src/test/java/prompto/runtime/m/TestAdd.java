package prompto.runtime.m;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.m.BaseMParserTest;
import prompto.runtime.utils.Out;

public class TestAdd extends BaseMParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAddInteger() throws Exception {
		checkInterpretedOutput("add/addInteger.pmc");
	}

	@Test
	public void testCompiledAddInteger() throws Exception {
		checkCompiledOutput("add/addInteger.pmc");
	}

	@Test
	public void testTranspiledAddInteger() throws Exception {
		checkTranspiledOutput("add/addInteger.pmc");
	}

}

