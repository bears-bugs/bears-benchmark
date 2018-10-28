package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestFilter extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedFilterFromCursor() throws Exception {
		checkInterpretedOutput("filter/filterFromCursor.pec");
	}

	@Test
	public void testCompiledFilterFromCursor() throws Exception {
		checkCompiledOutput("filter/filterFromCursor.pec");
	}

	@Test
	public void testTranspiledFilterFromCursor() throws Exception {
		checkTranspiledOutput("filter/filterFromCursor.pec");
	}

	@Test
	public void testInterpretedFilterFromList() throws Exception {
		checkInterpretedOutput("filter/filterFromList.pec");
	}

	@Test
	public void testCompiledFilterFromList() throws Exception {
		checkCompiledOutput("filter/filterFromList.pec");
	}

	@Test
	public void testTranspiledFilterFromList() throws Exception {
		checkTranspiledOutput("filter/filterFromList.pec");
	}

	@Test
	public void testInterpretedFilterFromSet() throws Exception {
		checkInterpretedOutput("filter/filterFromSet.pec");
	}

	@Test
	public void testCompiledFilterFromSet() throws Exception {
		checkCompiledOutput("filter/filterFromSet.pec");
	}

	@Test
	public void testTranspiledFilterFromSet() throws Exception {
		checkTranspiledOutput("filter/filterFromSet.pec");
	}

}

