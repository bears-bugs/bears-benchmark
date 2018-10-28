package prompto.runtime.m;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.m.BaseMParserTest;
import prompto.runtime.utils.Out;

public class TestWidget extends BaseMParserTest {

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
		checkInterpretedOutput("widget/minimal.pmc");
	}

	@Test
	public void testTranspiledMinimal() throws Exception {
		checkTranspiledOutput("widget/minimal.pmc");
	}

	@Test
	public void testInterpretedNative() throws Exception {
		checkInterpretedOutput("widget/native.pmc");
	}

	@Test
	public void testTranspiledNative() throws Exception {
		checkTranspiledOutput("widget/native.pmc");
	}

}

