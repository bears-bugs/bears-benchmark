package prompto.parser.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.error.SyntaxError;
import prompto.runtime.utils.Out;


public class TestResourceError extends BaseEParserTest {

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
		interpretResource("resourceError/badRead.pec", true);
	}

	@Test(expected = SyntaxError.class)
	public void testBadWrite() throws Exception {
		interpretResource("resourceError/badWrite.pec", true);
	}

	@Test(expected = SyntaxError.class)
	public void testBadResource() throws Exception {
		interpretResource("resourceError/badResource.pec", true);
	}
	
}
