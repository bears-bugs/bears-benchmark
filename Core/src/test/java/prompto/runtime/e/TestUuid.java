package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestUuid extends BaseEParserTest {

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
		checkInterpretedOutput("uuid/uuid.pec");
	}

	@Test
	public void testCompiledUuid() throws Exception {
		checkCompiledOutput("uuid/uuid.pec");
	}

	@Test
	public void testTranspiledUuid() throws Exception {
		checkTranspiledOutput("uuid/uuid.pec");
	}

}

