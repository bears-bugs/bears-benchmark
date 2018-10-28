package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestFilter extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedFilterFromList() throws Exception {
		checkInterpretedOutput("filter/filterFromList.poc");
	}

	@Test
	public void testCompiledFilterFromList() throws Exception {
		checkCompiledOutput("filter/filterFromList.poc");
	}

	@Test
	public void testTranspiledFilterFromList() throws Exception {
		checkTranspiledOutput("filter/filterFromList.poc");
	}

	@Test
	public void testInterpretedFilterFromSet() throws Exception {
		checkInterpretedOutput("filter/filterFromSet.poc");
	}

	@Test
	public void testCompiledFilterFromSet() throws Exception {
		checkCompiledOutput("filter/filterFromSet.poc");
	}

	@Test
	public void testTranspiledFilterFromSet() throws Exception {
		checkTranspiledOutput("filter/filterFromSet.poc");
	}

}

