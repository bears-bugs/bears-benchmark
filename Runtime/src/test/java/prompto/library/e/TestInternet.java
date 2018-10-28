package prompto.library.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestInternet extends BaseEParserTest {

	@Before
	public void before() throws Exception {
		Out.init();
		loadDependency("internet");
		loadDependency("console");
		loadDependency("core");
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testHtml() throws Exception {
		runTests("internet/html.pec");
	}

	@Test
	public void testUrl() throws Exception {
		runTests("internet/url.pec");
	}

}

