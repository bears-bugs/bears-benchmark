package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestSetters extends BaseEParserTest {

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
		checkInterpretedOutput("setters/getter.pec");
	}

	@Test
	public void testCompiledGetter() throws Exception {
		checkCompiledOutput("setters/getter.pec");
	}

	@Test
	public void testTranspiledGetter() throws Exception {
		checkTranspiledOutput("setters/getter.pec");
	}

	@Test
	public void testInterpretedGetterCall() throws Exception {
		checkInterpretedOutput("setters/getterCall.pec");
	}

	@Test
	public void testCompiledGetterCall() throws Exception {
		checkCompiledOutput("setters/getterCall.pec");
	}

	@Test
	public void testTranspiledGetterCall() throws Exception {
		checkTranspiledOutput("setters/getterCall.pec");
	}

	@Test
	public void testInterpretedSetter() throws Exception {
		checkInterpretedOutput("setters/setter.pec");
	}

	@Test
	public void testCompiledSetter() throws Exception {
		checkCompiledOutput("setters/setter.pec");
	}

	@Test
	public void testTranspiledSetter() throws Exception {
		checkTranspiledOutput("setters/setter.pec");
	}

}

