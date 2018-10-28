package prompto.parser.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.error.DivideByZeroError;
import prompto.runtime.utils.Out;

public class TestUnexpected extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}

	@Test(expected = DivideByZeroError.class)
	public void testReturn() throws Exception {
		interpretResource("errors/unexpected.pec", true);
	}
	
}
