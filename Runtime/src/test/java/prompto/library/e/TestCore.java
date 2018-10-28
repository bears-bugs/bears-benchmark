package prompto.library.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestCore extends BaseEParserTest {

	@Before
	public void before() throws Exception {
		Out.init();
		loadDependency("core");
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testAny() throws Exception {
		runTests("core/any.pec");
	}

	@Test
	public void testAttribute() throws Exception {
		runTests("core/attribute.pec");
	}

	@Test
	public void testAttributes() throws Exception {
		runTests("core/attributes.pec");
	}

	@Test
	public void testError() throws Exception {
		runTests("core/error.pec");
	}

	@Test
	public void testMath() throws Exception {
		runTests("core/math.pec");
	}

	@Test
	public void testParse() throws Exception {
		runTests("core/parse.pec");
	}

	@Test
	public void testTime() throws Exception {
		runTests("core/time.pec");
	}

	@Test
	public void testUtils() throws Exception {
		runTests("core/utils.pec");
	}

}

