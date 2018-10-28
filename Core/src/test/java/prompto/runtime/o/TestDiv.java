package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestDiv extends BaseOParserTest {

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
		checkInterpretedOutput("div/divDecimal.poc");
	}

	@Test
	public void testCompiledDivDecimal() throws Exception {
		checkCompiledOutput("div/divDecimal.poc");
	}

	@Test
	public void testTranspiledDivDecimal() throws Exception {
		checkTranspiledOutput("div/divDecimal.poc");
	}

	@Test
	public void testInterpretedDivInteger() throws Exception {
		checkInterpretedOutput("div/divInteger.poc");
	}

	@Test
	public void testCompiledDivInteger() throws Exception {
		checkCompiledOutput("div/divInteger.poc");
	}

	@Test
	public void testTranspiledDivInteger() throws Exception {
		checkTranspiledOutput("div/divInteger.poc");
	}

	@Test
	public void testInterpretedIdivInteger() throws Exception {
		checkInterpretedOutput("div/idivInteger.poc");
	}

	@Test
	public void testCompiledIdivInteger() throws Exception {
		checkCompiledOutput("div/idivInteger.poc");
	}

	@Test
	public void testTranspiledIdivInteger() throws Exception {
		checkTranspiledOutput("div/idivInteger.poc");
	}

	@Test
	public void testInterpretedModInteger() throws Exception {
		checkInterpretedOutput("div/modInteger.poc");
	}

	@Test
	public void testCompiledModInteger() throws Exception {
		checkCompiledOutput("div/modInteger.poc");
	}

	@Test
	public void testTranspiledModInteger() throws Exception {
		checkTranspiledOutput("div/modInteger.poc");
	}

}

