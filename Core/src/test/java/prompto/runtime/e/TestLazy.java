package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestLazy extends BaseEParserTest {

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
		checkInterpretedOutput("lazy/cyclic.pec");
	}

	@Test
	public void testCompiledCyclic() throws Exception {
		checkCompiledOutput("lazy/cyclic.pec");
	}

	@Test
	public void testTranspiledCyclic() throws Exception {
		checkTranspiledOutput("lazy/cyclic.pec");
	}

	@Test
	public void testInterpretedDict() throws Exception {
		checkInterpretedOutput("lazy/dict.pec");
	}

	@Test
	public void testCompiledDict() throws Exception {
		checkCompiledOutput("lazy/dict.pec");
	}

	@Test
	public void testTranspiledDict() throws Exception {
		checkTranspiledOutput("lazy/dict.pec");
	}

	@Test
	public void testInterpretedList() throws Exception {
		checkInterpretedOutput("lazy/list.pec");
	}

	@Test
	public void testCompiledList() throws Exception {
		checkCompiledOutput("lazy/list.pec");
	}

	@Test
	public void testTranspiledList() throws Exception {
		checkTranspiledOutput("lazy/list.pec");
	}

	@Test
	public void testInterpretedSet() throws Exception {
		checkInterpretedOutput("lazy/set.pec");
	}

	@Test
	public void testCompiledSet() throws Exception {
		checkCompiledOutput("lazy/set.pec");
	}

	@Test
	public void testTranspiledSet() throws Exception {
		checkTranspiledOutput("lazy/set.pec");
	}

	@Test
	public void testInterpretedTransient() throws Exception {
		checkInterpretedOutput("lazy/transient.pec");
	}

	@Test
	public void testCompiledTransient() throws Exception {
		checkCompiledOutput("lazy/transient.pec");
	}

	@Test
	public void testTranspiledTransient() throws Exception {
		checkTranspiledOutput("lazy/transient.pec");
	}

}

