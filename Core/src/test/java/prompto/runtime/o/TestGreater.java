package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestGreater extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedGtCharacter() throws Exception {
		checkInterpretedOutput("greater/gtCharacter.poc");
	}

	@Test
	public void testCompiledGtCharacter() throws Exception {
		checkCompiledOutput("greater/gtCharacter.poc");
	}

	@Test
	public void testTranspiledGtCharacter() throws Exception {
		checkTranspiledOutput("greater/gtCharacter.poc");
	}

	@Test
	public void testInterpretedGtDate() throws Exception {
		checkInterpretedOutput("greater/gtDate.poc");
	}

	@Test
	public void testCompiledGtDate() throws Exception {
		checkCompiledOutput("greater/gtDate.poc");
	}

	@Test
	public void testTranspiledGtDate() throws Exception {
		checkTranspiledOutput("greater/gtDate.poc");
	}

	@Test
	public void testInterpretedGtDateTime() throws Exception {
		checkInterpretedOutput("greater/gtDateTime.poc");
	}

	@Test
	public void testCompiledGtDateTime() throws Exception {
		checkCompiledOutput("greater/gtDateTime.poc");
	}

	@Test
	public void testTranspiledGtDateTime() throws Exception {
		checkTranspiledOutput("greater/gtDateTime.poc");
	}

	@Test
	public void testInterpretedGtDecimal() throws Exception {
		checkInterpretedOutput("greater/gtDecimal.poc");
	}

	@Test
	public void testCompiledGtDecimal() throws Exception {
		checkCompiledOutput("greater/gtDecimal.poc");
	}

	@Test
	public void testTranspiledGtDecimal() throws Exception {
		checkTranspiledOutput("greater/gtDecimal.poc");
	}

	@Test
	public void testInterpretedGtInteger() throws Exception {
		checkInterpretedOutput("greater/gtInteger.poc");
	}

	@Test
	public void testCompiledGtInteger() throws Exception {
		checkCompiledOutput("greater/gtInteger.poc");
	}

	@Test
	public void testTranspiledGtInteger() throws Exception {
		checkTranspiledOutput("greater/gtInteger.poc");
	}

	@Test
	public void testInterpretedGtText() throws Exception {
		checkInterpretedOutput("greater/gtText.poc");
	}

	@Test
	public void testCompiledGtText() throws Exception {
		checkCompiledOutput("greater/gtText.poc");
	}

	@Test
	public void testTranspiledGtText() throws Exception {
		checkTranspiledOutput("greater/gtText.poc");
	}

	@Test
	public void testInterpretedGtTime() throws Exception {
		checkInterpretedOutput("greater/gtTime.poc");
	}

	@Test
	public void testCompiledGtTime() throws Exception {
		checkCompiledOutput("greater/gtTime.poc");
	}

	@Test
	public void testTranspiledGtTime() throws Exception {
		checkTranspiledOutput("greater/gtTime.poc");
	}

	@Test
	public void testInterpretedGtVersion() throws Exception {
		checkInterpretedOutput("greater/gtVersion.poc");
	}

	@Test
	public void testCompiledGtVersion() throws Exception {
		checkCompiledOutput("greater/gtVersion.poc");
	}

	@Test
	public void testTranspiledGtVersion() throws Exception {
		checkTranspiledOutput("greater/gtVersion.poc");
	}

	@Test
	public void testInterpretedGteCharacter() throws Exception {
		checkInterpretedOutput("greater/gteCharacter.poc");
	}

	@Test
	public void testCompiledGteCharacter() throws Exception {
		checkCompiledOutput("greater/gteCharacter.poc");
	}

	@Test
	public void testTranspiledGteCharacter() throws Exception {
		checkTranspiledOutput("greater/gteCharacter.poc");
	}

	@Test
	public void testInterpretedGteDate() throws Exception {
		checkInterpretedOutput("greater/gteDate.poc");
	}

	@Test
	public void testCompiledGteDate() throws Exception {
		checkCompiledOutput("greater/gteDate.poc");
	}

	@Test
	public void testTranspiledGteDate() throws Exception {
		checkTranspiledOutput("greater/gteDate.poc");
	}

	@Test
	public void testInterpretedGteDateTime() throws Exception {
		checkInterpretedOutput("greater/gteDateTime.poc");
	}

	@Test
	public void testCompiledGteDateTime() throws Exception {
		checkCompiledOutput("greater/gteDateTime.poc");
	}

	@Test
	public void testTranspiledGteDateTime() throws Exception {
		checkTranspiledOutput("greater/gteDateTime.poc");
	}

	@Test
	public void testInterpretedGteDecimal() throws Exception {
		checkInterpretedOutput("greater/gteDecimal.poc");
	}

	@Test
	public void testCompiledGteDecimal() throws Exception {
		checkCompiledOutput("greater/gteDecimal.poc");
	}

	@Test
	public void testTranspiledGteDecimal() throws Exception {
		checkTranspiledOutput("greater/gteDecimal.poc");
	}

	@Test
	public void testInterpretedGteInteger() throws Exception {
		checkInterpretedOutput("greater/gteInteger.poc");
	}

	@Test
	public void testCompiledGteInteger() throws Exception {
		checkCompiledOutput("greater/gteInteger.poc");
	}

	@Test
	public void testTranspiledGteInteger() throws Exception {
		checkTranspiledOutput("greater/gteInteger.poc");
	}

	@Test
	public void testInterpretedGteText() throws Exception {
		checkInterpretedOutput("greater/gteText.poc");
	}

	@Test
	public void testCompiledGteText() throws Exception {
		checkCompiledOutput("greater/gteText.poc");
	}

	@Test
	public void testTranspiledGteText() throws Exception {
		checkTranspiledOutput("greater/gteText.poc");
	}

	@Test
	public void testInterpretedGteTime() throws Exception {
		checkInterpretedOutput("greater/gteTime.poc");
	}

	@Test
	public void testCompiledGteTime() throws Exception {
		checkCompiledOutput("greater/gteTime.poc");
	}

	@Test
	public void testTranspiledGteTime() throws Exception {
		checkTranspiledOutput("greater/gteTime.poc");
	}

}

