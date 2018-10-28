package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestWidget extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedMinimal() throws Exception {
		checkInterpretedOutput("widget/minimal.pec");
	}

	@Test
	public void testTranspiledMinimal() throws Exception {
		checkTranspiledOutput("widget/minimal.pec");
	}

	@Test
	public void testInterpretedNative() throws Exception {
		checkInterpretedOutput("widget/native.pec");
	}

	@Test
	public void testTranspiledNative() throws Exception {
		checkTranspiledOutput("widget/native.pec");
	}

}

