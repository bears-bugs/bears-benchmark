package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestWidget extends BaseOParserTest {

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
		checkInterpretedOutput("widget/minimal.poc");
	}

	@Test
	public void testTranspiledMinimal() throws Exception {
		checkTranspiledOutput("widget/minimal.poc");
	}

	@Test
	public void testInterpretedNative() throws Exception {
		checkInterpretedOutput("widget/native.poc");
	}

	@Test
	public void testTranspiledNative() throws Exception {
		checkTranspiledOutput("widget/native.poc");
	}

	@Test
	public void testInterpretedWithEvent() throws Exception {
		checkInterpretedOutput("widget/withEvent.poc");
	}

	@Test
	public void testTranspiledWithEvent() throws Exception {
		checkTranspiledOutput("widget/withEvent.poc");
	}

}

