package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestInfer extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedInferDict() throws Exception {
		checkInterpretedOutput("infer/inferDict.pec");
	}

	@Test
	public void testCompiledInferDict() throws Exception {
		checkCompiledOutput("infer/inferDict.pec");
	}

	@Test
	public void testTranspiledInferDict() throws Exception {
		checkTranspiledOutput("infer/inferDict.pec");
	}

	@Test
	public void testInterpretedInferList() throws Exception {
		checkInterpretedOutput("infer/inferList.pec");
	}

	@Test
	public void testCompiledInferList() throws Exception {
		checkCompiledOutput("infer/inferList.pec");
	}

	@Test
	public void testTranspiledInferList() throws Exception {
		checkTranspiledOutput("infer/inferList.pec");
	}

	@Test
	public void testInterpretedInferSet() throws Exception {
		checkInterpretedOutput("infer/inferSet.pec");
	}

	@Test
	public void testCompiledInferSet() throws Exception {
		checkCompiledOutput("infer/inferSet.pec");
	}

	@Test
	public void testTranspiledInferSet() throws Exception {
		checkTranspiledOutput("infer/inferSet.pec");
	}

}

