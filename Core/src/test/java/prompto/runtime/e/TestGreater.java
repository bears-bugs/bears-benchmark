package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestGreater extends BaseEParserTest {

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
		checkInterpretedOutput("greater/gtCharacter.pec");
	}

	@Test
	public void testCompiledGtCharacter() throws Exception {
		checkCompiledOutput("greater/gtCharacter.pec");
	}

	@Test
	public void testTranspiledGtCharacter() throws Exception {
		checkTranspiledOutput("greater/gtCharacter.pec");
	}

	@Test
	public void testInterpretedGtDate() throws Exception {
		checkInterpretedOutput("greater/gtDate.pec");
	}

	@Test
	public void testCompiledGtDate() throws Exception {
		checkCompiledOutput("greater/gtDate.pec");
	}

	@Test
	public void testTranspiledGtDate() throws Exception {
		checkTranspiledOutput("greater/gtDate.pec");
	}

	@Test
	public void testInterpretedGtDateTime() throws Exception {
		checkInterpretedOutput("greater/gtDateTime.pec");
	}

	@Test
	public void testCompiledGtDateTime() throws Exception {
		checkCompiledOutput("greater/gtDateTime.pec");
	}

	@Test
	public void testTranspiledGtDateTime() throws Exception {
		checkTranspiledOutput("greater/gtDateTime.pec");
	}

	@Test
	public void testInterpretedGtDecimal() throws Exception {
		checkInterpretedOutput("greater/gtDecimal.pec");
	}

	@Test
	public void testCompiledGtDecimal() throws Exception {
		checkCompiledOutput("greater/gtDecimal.pec");
	}

	@Test
	public void testTranspiledGtDecimal() throws Exception {
		checkTranspiledOutput("greater/gtDecimal.pec");
	}

	@Test
	public void testInterpretedGtInteger() throws Exception {
		checkInterpretedOutput("greater/gtInteger.pec");
	}

	@Test
	public void testCompiledGtInteger() throws Exception {
		checkCompiledOutput("greater/gtInteger.pec");
	}

	@Test
	public void testTranspiledGtInteger() throws Exception {
		checkTranspiledOutput("greater/gtInteger.pec");
	}

	@Test
	public void testInterpretedGtText() throws Exception {
		checkInterpretedOutput("greater/gtText.pec");
	}

	@Test
	public void testCompiledGtText() throws Exception {
		checkCompiledOutput("greater/gtText.pec");
	}

	@Test
	public void testTranspiledGtText() throws Exception {
		checkTranspiledOutput("greater/gtText.pec");
	}

	@Test
	public void testInterpretedGtTime() throws Exception {
		checkInterpretedOutput("greater/gtTime.pec");
	}

	@Test
	public void testCompiledGtTime() throws Exception {
		checkCompiledOutput("greater/gtTime.pec");
	}

	@Test
	public void testTranspiledGtTime() throws Exception {
		checkTranspiledOutput("greater/gtTime.pec");
	}

	@Test
	public void testInterpretedGtVersion() throws Exception {
		checkInterpretedOutput("greater/gtVersion.pec");
	}

	@Test
	public void testCompiledGtVersion() throws Exception {
		checkCompiledOutput("greater/gtVersion.pec");
	}

	@Test
	public void testTranspiledGtVersion() throws Exception {
		checkTranspiledOutput("greater/gtVersion.pec");
	}

	@Test
	public void testInterpretedGteCharacter() throws Exception {
		checkInterpretedOutput("greater/gteCharacter.pec");
	}

	@Test
	public void testCompiledGteCharacter() throws Exception {
		checkCompiledOutput("greater/gteCharacter.pec");
	}

	@Test
	public void testTranspiledGteCharacter() throws Exception {
		checkTranspiledOutput("greater/gteCharacter.pec");
	}

	@Test
	public void testInterpretedGteDate() throws Exception {
		checkInterpretedOutput("greater/gteDate.pec");
	}

	@Test
	public void testCompiledGteDate() throws Exception {
		checkCompiledOutput("greater/gteDate.pec");
	}

	@Test
	public void testTranspiledGteDate() throws Exception {
		checkTranspiledOutput("greater/gteDate.pec");
	}

	@Test
	public void testInterpretedGteDateTime() throws Exception {
		checkInterpretedOutput("greater/gteDateTime.pec");
	}

	@Test
	public void testCompiledGteDateTime() throws Exception {
		checkCompiledOutput("greater/gteDateTime.pec");
	}

	@Test
	public void testTranspiledGteDateTime() throws Exception {
		checkTranspiledOutput("greater/gteDateTime.pec");
	}

	@Test
	public void testInterpretedGteDecimal() throws Exception {
		checkInterpretedOutput("greater/gteDecimal.pec");
	}

	@Test
	public void testCompiledGteDecimal() throws Exception {
		checkCompiledOutput("greater/gteDecimal.pec");
	}

	@Test
	public void testTranspiledGteDecimal() throws Exception {
		checkTranspiledOutput("greater/gteDecimal.pec");
	}

	@Test
	public void testInterpretedGteInteger() throws Exception {
		checkInterpretedOutput("greater/gteInteger.pec");
	}

	@Test
	public void testCompiledGteInteger() throws Exception {
		checkCompiledOutput("greater/gteInteger.pec");
	}

	@Test
	public void testTranspiledGteInteger() throws Exception {
		checkTranspiledOutput("greater/gteInteger.pec");
	}

	@Test
	public void testInterpretedGteText() throws Exception {
		checkInterpretedOutput("greater/gteText.pec");
	}

	@Test
	public void testCompiledGteText() throws Exception {
		checkCompiledOutput("greater/gteText.pec");
	}

	@Test
	public void testTranspiledGteText() throws Exception {
		checkTranspiledOutput("greater/gteText.pec");
	}

	@Test
	public void testInterpretedGteTime() throws Exception {
		checkInterpretedOutput("greater/gteTime.pec");
	}

	@Test
	public void testCompiledGteTime() throws Exception {
		checkCompiledOutput("greater/gteTime.pec");
	}

	@Test
	public void testTranspiledGteTime() throws Exception {
		checkTranspiledOutput("greater/gteTime.pec");
	}

}

