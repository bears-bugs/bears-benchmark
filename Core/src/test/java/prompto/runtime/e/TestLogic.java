package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestLogic extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAndBoolean() throws Exception {
		checkInterpretedOutput("logic/andBoolean.pec");
	}

	@Test
	public void testCompiledAndBoolean() throws Exception {
		checkCompiledOutput("logic/andBoolean.pec");
	}

	@Test
	public void testTranspiledAndBoolean() throws Exception {
		checkTranspiledOutput("logic/andBoolean.pec");
	}

	@Test
	public void testInterpretedNotBoolean() throws Exception {
		checkInterpretedOutput("logic/notBoolean.pec");
	}

	@Test
	public void testCompiledNotBoolean() throws Exception {
		checkCompiledOutput("logic/notBoolean.pec");
	}

	@Test
	public void testTranspiledNotBoolean() throws Exception {
		checkTranspiledOutput("logic/notBoolean.pec");
	}

	@Test
	public void testInterpretedOrBoolean() throws Exception {
		checkInterpretedOutput("logic/orBoolean.pec");
	}

	@Test
	public void testCompiledOrBoolean() throws Exception {
		checkCompiledOutput("logic/orBoolean.pec");
	}

	@Test
	public void testTranspiledOrBoolean() throws Exception {
		checkTranspiledOutput("logic/orBoolean.pec");
	}

}

