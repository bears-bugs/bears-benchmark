package prompto.parser.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.error.SyntaxError;
import prompto.runtime.utils.Out;


public class TestResourceError extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}
	
	@Test(expected = SyntaxError.class)
	public void testBadRead() throws Exception {
		interpretResource("resourceError/badRead.poc", true);
	}

	@Test(expected = SyntaxError.class)
	public void testBadWrite() throws Exception {
		interpretResource("resourceError/badWrite.poc", true);
	}

	@Test(expected = SyntaxError.class)
	public void testBadResource() throws Exception {
		interpretResource("resourceError/badResource.poc", true);
	}
	
}
