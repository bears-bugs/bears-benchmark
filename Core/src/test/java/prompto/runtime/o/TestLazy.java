package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestLazy extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedCyclic() throws Exception {
		checkInterpretedOutput("lazy/cyclic.poc");
	}

	@Test
	public void testCompiledCyclic() throws Exception {
		checkCompiledOutput("lazy/cyclic.poc");
	}

	@Test
	public void testTranspiledCyclic() throws Exception {
		checkTranspiledOutput("lazy/cyclic.poc");
	}

	@Test
	public void testInterpretedDict() throws Exception {
		checkInterpretedOutput("lazy/dict.poc");
	}

	@Test
	public void testCompiledDict() throws Exception {
		checkCompiledOutput("lazy/dict.poc");
	}

	@Test
	public void testTranspiledDict() throws Exception {
		checkTranspiledOutput("lazy/dict.poc");
	}

	@Test
	public void testInterpretedList() throws Exception {
		checkInterpretedOutput("lazy/list.poc");
	}

	@Test
	public void testCompiledList() throws Exception {
		checkCompiledOutput("lazy/list.poc");
	}

	@Test
	public void testTranspiledList() throws Exception {
		checkTranspiledOutput("lazy/list.poc");
	}

	@Test
	public void testInterpretedSet() throws Exception {
		checkInterpretedOutput("lazy/set.poc");
	}

	@Test
	public void testCompiledSet() throws Exception {
		checkCompiledOutput("lazy/set.poc");
	}

	@Test
	public void testTranspiledSet() throws Exception {
		checkTranspiledOutput("lazy/set.poc");
	}

	@Test
	public void testInterpretedTransient() throws Exception {
		checkInterpretedOutput("lazy/transient.poc");
	}

	@Test
	public void testCompiledTransient() throws Exception {
		checkCompiledOutput("lazy/transient.poc");
	}

	@Test
	public void testTranspiledTransient() throws Exception {
		checkTranspiledOutput("lazy/transient.poc");
	}

}

