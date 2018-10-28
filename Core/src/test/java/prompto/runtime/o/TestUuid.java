package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestUuid extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedUuid() throws Exception {
		checkInterpretedOutput("uuid/uuid.poc");
	}

	@Test
	public void testCompiledUuid() throws Exception {
		checkCompiledOutput("uuid/uuid.poc");
	}

	@Test
	public void testTranspiledUuid() throws Exception {
		checkTranspiledOutput("uuid/uuid.poc");
	}

}

