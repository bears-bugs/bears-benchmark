package prompto.library.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestReader extends BaseEParserTest {

	@Before
	public void before() throws Exception {
		Out.init();
		loadDependency("reader");
		loadDependency("core");
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testJson() throws Exception {
		runTests("reader/json.pec");
	}

	@Test
	public void testReader() throws Exception {
		runTests("reader/reader.pec");
	}

}

