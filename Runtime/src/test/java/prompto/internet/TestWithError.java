package prompto.internet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;


public class TestWithError extends BaseEParserTest {
	
	@Before
	public void before() throws Exception {
		Out.init();
		loadDependency("internet");
		loadDependency("core");
	}
	
	
	@After
	public void after() {
		Out.restore();
	}
	
	
	@Test
	public void testWithReadWriteError() throws Exception {
		runTests("testWithError.pec", true);
	}
}
