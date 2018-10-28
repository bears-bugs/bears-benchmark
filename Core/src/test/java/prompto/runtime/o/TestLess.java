package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestLess extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedLtCharacter() throws Exception {
		checkInterpretedOutput("less/ltCharacter.poc");
	}

	@Test
	public void testCompiledLtCharacter() throws Exception {
		checkCompiledOutput("less/ltCharacter.poc");
	}

	@Test
	public void testTranspiledLtCharacter() throws Exception {
		checkTranspiledOutput("less/ltCharacter.poc");
	}

	@Test
	public void testInterpretedLtDate() throws Exception {
		checkInterpretedOutput("less/ltDate.poc");
	}

	@Test
	public void testCompiledLtDate() throws Exception {
		checkCompiledOutput("less/ltDate.poc");
	}

	@Test
	public void testTranspiledLtDate() throws Exception {
		checkTranspiledOutput("less/ltDate.poc");
	}

	@Test
	public void testInterpretedLtDateTime() throws Exception {
		checkInterpretedOutput("less/ltDateTime.poc");
	}

	@Test
	public void testCompiledLtDateTime() throws Exception {
		checkCompiledOutput("less/ltDateTime.poc");
	}

	@Test
	public void testTranspiledLtDateTime() throws Exception {
		checkTranspiledOutput("less/ltDateTime.poc");
	}

	@Test
	public void testInterpretedLtDecimal() throws Exception {
		checkInterpretedOutput("less/ltDecimal.poc");
	}

	@Test
	public void testCompiledLtDecimal() throws Exception {
		checkCompiledOutput("less/ltDecimal.poc");
	}

	@Test
	public void testTranspiledLtDecimal() throws Exception {
		checkTranspiledOutput("less/ltDecimal.poc");
	}

	@Test
	public void testInterpretedLtInteger() throws Exception {
		checkInterpretedOutput("less/ltInteger.poc");
	}

	@Test
	public void testCompiledLtInteger() throws Exception {
		checkCompiledOutput("less/ltInteger.poc");
	}

	@Test
	public void testTranspiledLtInteger() throws Exception {
		checkTranspiledOutput("less/ltInteger.poc");
	}

	@Test
	public void testInterpretedLtText() throws Exception {
		checkInterpretedOutput("less/ltText.poc");
	}

	@Test
	public void testCompiledLtText() throws Exception {
		checkCompiledOutput("less/ltText.poc");
	}

	@Test
	public void testTranspiledLtText() throws Exception {
		checkTranspiledOutput("less/ltText.poc");
	}

	@Test
	public void testInterpretedLtTime() throws Exception {
		checkInterpretedOutput("less/ltTime.poc");
	}

	@Test
	public void testCompiledLtTime() throws Exception {
		checkCompiledOutput("less/ltTime.poc");
	}

	@Test
	public void testTranspiledLtTime() throws Exception {
		checkTranspiledOutput("less/ltTime.poc");
	}

	@Test
	public void testInterpretedLtVersion() throws Exception {
		checkInterpretedOutput("less/ltVersion.poc");
	}

	@Test
	public void testCompiledLtVersion() throws Exception {
		checkCompiledOutput("less/ltVersion.poc");
	}

	@Test
	public void testTranspiledLtVersion() throws Exception {
		checkTranspiledOutput("less/ltVersion.poc");
	}

	@Test
	public void testInterpretedLteCharacter() throws Exception {
		checkInterpretedOutput("less/lteCharacter.poc");
	}

	@Test
	public void testCompiledLteCharacter() throws Exception {
		checkCompiledOutput("less/lteCharacter.poc");
	}

	@Test
	public void testTranspiledLteCharacter() throws Exception {
		checkTranspiledOutput("less/lteCharacter.poc");
	}

	@Test
	public void testInterpretedLteDate() throws Exception {
		checkInterpretedOutput("less/lteDate.poc");
	}

	@Test
	public void testCompiledLteDate() throws Exception {
		checkCompiledOutput("less/lteDate.poc");
	}

	@Test
	public void testTranspiledLteDate() throws Exception {
		checkTranspiledOutput("less/lteDate.poc");
	}

	@Test
	public void testInterpretedLteDateTime() throws Exception {
		checkInterpretedOutput("less/lteDateTime.poc");
	}

	@Test
	public void testCompiledLteDateTime() throws Exception {
		checkCompiledOutput("less/lteDateTime.poc");
	}

	@Test
	public void testTranspiledLteDateTime() throws Exception {
		checkTranspiledOutput("less/lteDateTime.poc");
	}

	@Test
	public void testInterpretedLteDecimal() throws Exception {
		checkInterpretedOutput("less/lteDecimal.poc");
	}

	@Test
	public void testCompiledLteDecimal() throws Exception {
		checkCompiledOutput("less/lteDecimal.poc");
	}

	@Test
	public void testTranspiledLteDecimal() throws Exception {
		checkTranspiledOutput("less/lteDecimal.poc");
	}

	@Test
	public void testInterpretedLteInteger() throws Exception {
		checkInterpretedOutput("less/lteInteger.poc");
	}

	@Test
	public void testCompiledLteInteger() throws Exception {
		checkCompiledOutput("less/lteInteger.poc");
	}

	@Test
	public void testTranspiledLteInteger() throws Exception {
		checkTranspiledOutput("less/lteInteger.poc");
	}

	@Test
	public void testInterpretedLteText() throws Exception {
		checkInterpretedOutput("less/lteText.poc");
	}

	@Test
	public void testCompiledLteText() throws Exception {
		checkCompiledOutput("less/lteText.poc");
	}

	@Test
	public void testTranspiledLteText() throws Exception {
		checkTranspiledOutput("less/lteText.poc");
	}

	@Test
	public void testInterpretedLteTime() throws Exception {
		checkInterpretedOutput("less/lteTime.poc");
	}

	@Test
	public void testCompiledLteTime() throws Exception {
		checkCompiledOutput("less/lteTime.poc");
	}

	@Test
	public void testTranspiledLteTime() throws Exception {
		checkTranspiledOutput("less/lteTime.poc");
	}

}

