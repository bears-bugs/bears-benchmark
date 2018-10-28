package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestMult extends BaseOParserTest {

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
		checkInterpretedOutput("mult/multCharacter.poc");
	}

	@Test
	public void testCompiledMultCharacter() throws Exception {
		checkCompiledOutput("mult/multCharacter.poc");
	}

	@Test
	public void testTranspiledMultCharacter() throws Exception {
		checkTranspiledOutput("mult/multCharacter.poc");
	}

	@Test
	public void testInterpretedMultDecimal() throws Exception {
		checkInterpretedOutput("mult/multDecimal.poc");
	}

	@Test
	public void testCompiledMultDecimal() throws Exception {
		checkCompiledOutput("mult/multDecimal.poc");
	}

	@Test
	public void testTranspiledMultDecimal() throws Exception {
		checkTranspiledOutput("mult/multDecimal.poc");
	}

	@Test
	public void testInterpretedMultInteger() throws Exception {
		checkInterpretedOutput("mult/multInteger.poc");
	}

	@Test
	public void testCompiledMultInteger() throws Exception {
		checkCompiledOutput("mult/multInteger.poc");
	}

	@Test
	public void testTranspiledMultInteger() throws Exception {
		checkTranspiledOutput("mult/multInteger.poc");
	}

	@Test
	public void testInterpretedMultList() throws Exception {
		checkInterpretedOutput("mult/multList.poc");
	}

	@Test
	public void testCompiledMultList() throws Exception {
		checkCompiledOutput("mult/multList.poc");
	}

	@Test
	public void testTranspiledMultList() throws Exception {
		checkTranspiledOutput("mult/multList.poc");
	}

	@Test
	public void testInterpretedMultPeriod() throws Exception {
		checkInterpretedOutput("mult/multPeriod.poc");
	}

	@Test
	public void testCompiledMultPeriod() throws Exception {
		checkCompiledOutput("mult/multPeriod.poc");
	}

	@Test
	public void testTranspiledMultPeriod() throws Exception {
		checkTranspiledOutput("mult/multPeriod.poc");
	}

	@Test
	public void testInterpretedMultText() throws Exception {
		checkInterpretedOutput("mult/multText.poc");
	}

	@Test
	public void testCompiledMultText() throws Exception {
		checkCompiledOutput("mult/multText.poc");
	}

	@Test
	public void testTranspiledMultText() throws Exception {
		checkTranspiledOutput("mult/multText.poc");
	}

}

