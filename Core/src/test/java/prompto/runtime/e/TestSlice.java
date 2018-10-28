package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestSlice extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedSliceList() throws Exception {
		checkInterpretedOutput("slice/sliceList.pec");
	}

	@Test
	public void testCompiledSliceList() throws Exception {
		checkCompiledOutput("slice/sliceList.pec");
	}

	@Test
	public void testTranspiledSliceList() throws Exception {
		checkTranspiledOutput("slice/sliceList.pec");
	}

	@Test
	public void testInterpretedSliceRange() throws Exception {
		checkInterpretedOutput("slice/sliceRange.pec");
	}

	@Test
	public void testCompiledSliceRange() throws Exception {
		checkCompiledOutput("slice/sliceRange.pec");
	}

	@Test
	public void testTranspiledSliceRange() throws Exception {
		checkTranspiledOutput("slice/sliceRange.pec");
	}

	@Test
	public void testInterpretedSliceText() throws Exception {
		checkInterpretedOutput("slice/sliceText.pec");
	}

	@Test
	public void testCompiledSliceText() throws Exception {
		checkCompiledOutput("slice/sliceText.pec");
	}

	@Test
	public void testTranspiledSliceText() throws Exception {
		checkTranspiledOutput("slice/sliceText.pec");
	}

}

