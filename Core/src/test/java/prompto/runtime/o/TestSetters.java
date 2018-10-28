package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestSetters extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedGetter() throws Exception {
		checkInterpretedOutput("setters/getter.poc");
	}

	@Test
	public void testCompiledGetter() throws Exception {
		checkCompiledOutput("setters/getter.poc");
	}

	@Test
	public void testTranspiledGetter() throws Exception {
		checkTranspiledOutput("setters/getter.poc");
	}

	@Test
	public void testInterpretedSetter() throws Exception {
		checkInterpretedOutput("setters/setter.poc");
	}

	@Test
	public void testCompiledSetter() throws Exception {
		checkCompiledOutput("setters/setter.poc");
	}

	@Test
	public void testTranspiledSetter() throws Exception {
		checkTranspiledOutput("setters/setter.poc");
	}

}

