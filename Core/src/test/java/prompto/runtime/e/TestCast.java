package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestCast extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAutoDecimalCast() throws Exception {
		checkInterpretedOutput("cast/autoDecimalCast.pec");
	}

	@Test
	public void testCompiledAutoDecimalCast() throws Exception {
		checkCompiledOutput("cast/autoDecimalCast.pec");
	}

	@Test
	public void testTranspiledAutoDecimalCast() throws Exception {
		checkTranspiledOutput("cast/autoDecimalCast.pec");
	}

	@Test
	public void testInterpretedAutoDowncast() throws Exception {
		checkInterpretedOutput("cast/autoDowncast.pec");
	}

	@Test
	public void testCompiledAutoDowncast() throws Exception {
		checkCompiledOutput("cast/autoDowncast.pec");
	}

	@Test
	public void testTranspiledAutoDowncast() throws Exception {
		checkTranspiledOutput("cast/autoDowncast.pec");
	}

	@Test
	public void testInterpretedAutoIntegerCast() throws Exception {
		checkInterpretedOutput("cast/autoIntegerCast.pec");
	}

	@Test
	public void testCompiledAutoIntegerCast() throws Exception {
		checkCompiledOutput("cast/autoIntegerCast.pec");
	}

	@Test
	public void testTranspiledAutoIntegerCast() throws Exception {
		checkTranspiledOutput("cast/autoIntegerCast.pec");
	}

	@Test
	public void testInterpretedCastChild() throws Exception {
		checkInterpretedOutput("cast/castChild.pec");
	}

	@Test
	public void testCompiledCastChild() throws Exception {
		checkCompiledOutput("cast/castChild.pec");
	}

	@Test
	public void testTranspiledCastChild() throws Exception {
		checkTranspiledOutput("cast/castChild.pec");
	}

	@Test
	public void testInterpretedCastDecimal() throws Exception {
		checkInterpretedOutput("cast/castDecimal.pec");
	}

	@Test
	public void testCompiledCastDecimal() throws Exception {
		checkCompiledOutput("cast/castDecimal.pec");
	}

	@Test
	public void testTranspiledCastDecimal() throws Exception {
		checkTranspiledOutput("cast/castDecimal.pec");
	}

	@Test
	public void testInterpretedCastDocument() throws Exception {
		checkInterpretedOutput("cast/castDocument.pec");
	}

	@Test
	public void testCompiledCastDocument() throws Exception {
		checkCompiledOutput("cast/castDocument.pec");
	}

	@Test
	public void testTranspiledCastDocument() throws Exception {
		checkTranspiledOutput("cast/castDocument.pec");
	}

	@Test
	public void testInterpretedCastInteger() throws Exception {
		checkInterpretedOutput("cast/castInteger.pec");
	}

	@Test
	public void testCompiledCastInteger() throws Exception {
		checkCompiledOutput("cast/castInteger.pec");
	}

	@Test
	public void testTranspiledCastInteger() throws Exception {
		checkTranspiledOutput("cast/castInteger.pec");
	}

	@Test
	public void testInterpretedCastMethod() throws Exception {
		checkInterpretedOutput("cast/castMethod.pec");
	}

	@Test
	public void testCompiledCastMethod() throws Exception {
		checkCompiledOutput("cast/castMethod.pec");
	}

	@Test
	public void testTranspiledCastMethod() throws Exception {
		checkTranspiledOutput("cast/castMethod.pec");
	}

	@Test
	public void testInterpretedCastMissing() throws Exception {
		checkInterpretedOutput("cast/castMissing.pec");
	}

	@Test
	public void testCompiledCastMissing() throws Exception {
		checkCompiledOutput("cast/castMissing.pec");
	}

	@Test
	public void testTranspiledCastMissing() throws Exception {
		checkTranspiledOutput("cast/castMissing.pec");
	}

	@Test
	public void testInterpretedCastNull() throws Exception {
		checkInterpretedOutput("cast/castNull.pec");
	}

	@Test
	public void testCompiledCastNull() throws Exception {
		checkCompiledOutput("cast/castNull.pec");
	}

	@Test
	public void testTranspiledCastNull() throws Exception {
		checkTranspiledOutput("cast/castNull.pec");
	}

	@Test
	public void testInterpretedCastRoot() throws Exception {
		checkInterpretedOutput("cast/castRoot.pec");
	}

	@Test
	public void testCompiledCastRoot() throws Exception {
		checkCompiledOutput("cast/castRoot.pec");
	}

	@Test
	public void testTranspiledCastRoot() throws Exception {
		checkTranspiledOutput("cast/castRoot.pec");
	}

	@Test
	public void testInterpretedIsAChild() throws Exception {
		checkInterpretedOutput("cast/isAChild.pec");
	}

	@Test
	public void testCompiledIsAChild() throws Exception {
		checkCompiledOutput("cast/isAChild.pec");
	}

	@Test
	public void testTranspiledIsAChild() throws Exception {
		checkTranspiledOutput("cast/isAChild.pec");
	}

	@Test
	public void testInterpretedIsAText() throws Exception {
		checkInterpretedOutput("cast/isAText.pec");
	}

	@Test
	public void testCompiledIsAText() throws Exception {
		checkCompiledOutput("cast/isAText.pec");
	}

	@Test
	public void testTranspiledIsAText() throws Exception {
		checkTranspiledOutput("cast/isAText.pec");
	}

}

