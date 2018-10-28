package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestCast extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAutoDowncast() throws Exception {
		checkInterpretedOutput("cast/autoDowncast.poc");
	}

	@Test
	public void testCompiledAutoDowncast() throws Exception {
		checkCompiledOutput("cast/autoDowncast.poc");
	}

	@Test
	public void testTranspiledAutoDowncast() throws Exception {
		checkTranspiledOutput("cast/autoDowncast.poc");
	}

	@Test
	public void testInterpretedCastChild() throws Exception {
		checkInterpretedOutput("cast/castChild.poc");
	}

	@Test
	public void testCompiledCastChild() throws Exception {
		checkCompiledOutput("cast/castChild.poc");
	}

	@Test
	public void testTranspiledCastChild() throws Exception {
		checkTranspiledOutput("cast/castChild.poc");
	}

	@Test
	public void testInterpretedCastMethod() throws Exception {
		checkInterpretedOutput("cast/castMethod.poc");
	}

	@Test
	public void testCompiledCastMethod() throws Exception {
		checkCompiledOutput("cast/castMethod.poc");
	}

	@Test
	public void testTranspiledCastMethod() throws Exception {
		checkTranspiledOutput("cast/castMethod.poc");
	}

	@Test
	public void testInterpretedCastMissing() throws Exception {
		checkInterpretedOutput("cast/castMissing.poc");
	}

	@Test
	public void testCompiledCastMissing() throws Exception {
		checkCompiledOutput("cast/castMissing.poc");
	}

	@Test
	public void testTranspiledCastMissing() throws Exception {
		checkTranspiledOutput("cast/castMissing.poc");
	}

	@Test
	public void testInterpretedCastNull() throws Exception {
		checkInterpretedOutput("cast/castNull.poc");
	}

	@Test
	public void testCompiledCastNull() throws Exception {
		checkCompiledOutput("cast/castNull.poc");
	}

	@Test
	public void testTranspiledCastNull() throws Exception {
		checkTranspiledOutput("cast/castNull.poc");
	}

	@Test
	public void testInterpretedIsAChild() throws Exception {
		checkInterpretedOutput("cast/isAChild.poc");
	}

	@Test
	public void testCompiledIsAChild() throws Exception {
		checkCompiledOutput("cast/isAChild.poc");
	}

	@Test
	public void testTranspiledIsAChild() throws Exception {
		checkTranspiledOutput("cast/isAChild.poc");
	}

	@Test
	public void testInterpretedIsAText() throws Exception {
		checkInterpretedOutput("cast/isAText.poc");
	}

	@Test
	public void testCompiledIsAText() throws Exception {
		checkCompiledOutput("cast/isAText.poc");
	}

	@Test
	public void testTranspiledIsAText() throws Exception {
		checkTranspiledOutput("cast/isAText.poc");
	}

}

