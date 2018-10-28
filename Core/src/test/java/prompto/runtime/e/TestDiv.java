package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestDiv extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedDivDecimal() throws Exception {
		checkInterpretedOutput("div/divDecimal.pec");
	}

	@Test
	public void testCompiledDivDecimal() throws Exception {
		checkCompiledOutput("div/divDecimal.pec");
	}

	@Test
	public void testTranspiledDivDecimal() throws Exception {
		checkTranspiledOutput("div/divDecimal.pec");
	}

	@Test
	public void testInterpretedDivInteger() throws Exception {
		checkInterpretedOutput("div/divInteger.pec");
	}

	@Test
	public void testCompiledDivInteger() throws Exception {
		checkCompiledOutput("div/divInteger.pec");
	}

	@Test
	public void testTranspiledDivInteger() throws Exception {
		checkTranspiledOutput("div/divInteger.pec");
	}

	@Test
	public void testInterpretedIdivInteger() throws Exception {
		checkInterpretedOutput("div/idivInteger.pec");
	}

	@Test
	public void testCompiledIdivInteger() throws Exception {
		checkCompiledOutput("div/idivInteger.pec");
	}

	@Test
	public void testTranspiledIdivInteger() throws Exception {
		checkTranspiledOutput("div/idivInteger.pec");
	}

	@Test
	public void testInterpretedModInteger() throws Exception {
		checkInterpretedOutput("div/modInteger.pec");
	}

	@Test
	public void testCompiledModInteger() throws Exception {
		checkCompiledOutput("div/modInteger.pec");
	}

	@Test
	public void testTranspiledModInteger() throws Exception {
		checkTranspiledOutput("div/modInteger.pec");
	}

}

