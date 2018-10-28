package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestMult extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedMultCharacter() throws Exception {
		checkInterpretedOutput("mult/multCharacter.pec");
	}

	@Test
	public void testCompiledMultCharacter() throws Exception {
		checkCompiledOutput("mult/multCharacter.pec");
	}

	@Test
	public void testTranspiledMultCharacter() throws Exception {
		checkTranspiledOutput("mult/multCharacter.pec");
	}

	@Test
	public void testInterpretedMultDecimal() throws Exception {
		checkInterpretedOutput("mult/multDecimal.pec");
	}

	@Test
	public void testCompiledMultDecimal() throws Exception {
		checkCompiledOutput("mult/multDecimal.pec");
	}

	@Test
	public void testTranspiledMultDecimal() throws Exception {
		checkTranspiledOutput("mult/multDecimal.pec");
	}

	@Test
	public void testInterpretedMultInteger() throws Exception {
		checkInterpretedOutput("mult/multInteger.pec");
	}

	@Test
	public void testCompiledMultInteger() throws Exception {
		checkCompiledOutput("mult/multInteger.pec");
	}

	@Test
	public void testTranspiledMultInteger() throws Exception {
		checkTranspiledOutput("mult/multInteger.pec");
	}

	@Test
	public void testInterpretedMultList() throws Exception {
		checkInterpretedOutput("mult/multList.pec");
	}

	@Test
	public void testCompiledMultList() throws Exception {
		checkCompiledOutput("mult/multList.pec");
	}

	@Test
	public void testTranspiledMultList() throws Exception {
		checkTranspiledOutput("mult/multList.pec");
	}

	@Test
	public void testInterpretedMultPeriod() throws Exception {
		checkInterpretedOutput("mult/multPeriod.pec");
	}

	@Test
	public void testCompiledMultPeriod() throws Exception {
		checkCompiledOutput("mult/multPeriod.pec");
	}

	@Test
	public void testTranspiledMultPeriod() throws Exception {
		checkTranspiledOutput("mult/multPeriod.pec");
	}

	@Test
	public void testInterpretedMultText() throws Exception {
		checkInterpretedOutput("mult/multText.pec");
	}

	@Test
	public void testCompiledMultText() throws Exception {
		checkCompiledOutput("mult/multText.pec");
	}

	@Test
	public void testTranspiledMultText() throws Exception {
		checkTranspiledOutput("mult/multText.pec");
	}

}

