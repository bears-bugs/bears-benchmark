package prompto.library.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestWeb extends BaseEParserTest {

	@Before
	public void before() throws Exception {
		Out.init();
		loadDependency("web");
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testEvents() throws Exception {
		runTests("web/events.pec");
	}

	@Test
	public void testReact() throws Exception {
		runTests("web/react.pec");
	}

	@Test
	public void testUtils() throws Exception {
		runTests("web/utils.pec");
	}

}

