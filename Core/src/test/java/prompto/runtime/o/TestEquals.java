package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestEquals extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedEqBoolean() throws Exception {
		checkInterpretedOutput("equals/eqBoolean.poc");
	}

	@Test
	public void testCompiledEqBoolean() throws Exception {
		checkCompiledOutput("equals/eqBoolean.poc");
	}

	@Test
	public void testTranspiledEqBoolean() throws Exception {
		checkTranspiledOutput("equals/eqBoolean.poc");
	}

	@Test
	public void testInterpretedEqCharacter() throws Exception {
		checkInterpretedOutput("equals/eqCharacter.poc");
	}

	@Test
	public void testCompiledEqCharacter() throws Exception {
		checkCompiledOutput("equals/eqCharacter.poc");
	}

	@Test
	public void testTranspiledEqCharacter() throws Exception {
		checkTranspiledOutput("equals/eqCharacter.poc");
	}

	@Test
	public void testInterpretedEqDate() throws Exception {
		checkInterpretedOutput("equals/eqDate.poc");
	}

	@Test
	public void testCompiledEqDate() throws Exception {
		checkCompiledOutput("equals/eqDate.poc");
	}

	@Test
	public void testTranspiledEqDate() throws Exception {
		checkTranspiledOutput("equals/eqDate.poc");
	}

	@Test
	public void testInterpretedEqDateTime() throws Exception {
		checkInterpretedOutput("equals/eqDateTime.poc");
	}

	@Test
	public void testCompiledEqDateTime() throws Exception {
		checkCompiledOutput("equals/eqDateTime.poc");
	}

	@Test
	public void testTranspiledEqDateTime() throws Exception {
		checkTranspiledOutput("equals/eqDateTime.poc");
	}

	@Test
	public void testInterpretedEqDecimal() throws Exception {
		checkInterpretedOutput("equals/eqDecimal.poc");
	}

	@Test
	public void testCompiledEqDecimal() throws Exception {
		checkCompiledOutput("equals/eqDecimal.poc");
	}

	@Test
	public void testTranspiledEqDecimal() throws Exception {
		checkTranspiledOutput("equals/eqDecimal.poc");
	}

	@Test
	public void testInterpretedEqDict() throws Exception {
		checkInterpretedOutput("equals/eqDict.poc");
	}

	@Test
	public void testCompiledEqDict() throws Exception {
		checkCompiledOutput("equals/eqDict.poc");
	}

	@Test
	public void testTranspiledEqDict() throws Exception {
		checkTranspiledOutput("equals/eqDict.poc");
	}

	@Test
	public void testInterpretedEqInteger() throws Exception {
		checkInterpretedOutput("equals/eqInteger.poc");
	}

	@Test
	public void testCompiledEqInteger() throws Exception {
		checkCompiledOutput("equals/eqInteger.poc");
	}

	@Test
	public void testTranspiledEqInteger() throws Exception {
		checkTranspiledOutput("equals/eqInteger.poc");
	}

	@Test
	public void testInterpretedEqList() throws Exception {
		checkInterpretedOutput("equals/eqList.poc");
	}

	@Test
	public void testCompiledEqList() throws Exception {
		checkCompiledOutput("equals/eqList.poc");
	}

	@Test
	public void testTranspiledEqList() throws Exception {
		checkTranspiledOutput("equals/eqList.poc");
	}

	@Test
	public void testInterpretedEqPeriod() throws Exception {
		checkInterpretedOutput("equals/eqPeriod.poc");
	}

	@Test
	public void testCompiledEqPeriod() throws Exception {
		checkCompiledOutput("equals/eqPeriod.poc");
	}

	@Test
	public void testTranspiledEqPeriod() throws Exception {
		checkTranspiledOutput("equals/eqPeriod.poc");
	}

	@Test
	public void testInterpretedEqRange() throws Exception {
		checkInterpretedOutput("equals/eqRange.poc");
	}

	@Test
	public void testCompiledEqRange() throws Exception {
		checkCompiledOutput("equals/eqRange.poc");
	}

	@Test
	public void testTranspiledEqRange() throws Exception {
		checkTranspiledOutput("equals/eqRange.poc");
	}

	@Test
	public void testInterpretedEqSet() throws Exception {
		checkInterpretedOutput("equals/eqSet.poc");
	}

	@Test
	public void testCompiledEqSet() throws Exception {
		checkCompiledOutput("equals/eqSet.poc");
	}

	@Test
	public void testTranspiledEqSet() throws Exception {
		checkTranspiledOutput("equals/eqSet.poc");
	}

	@Test
	public void testInterpretedEqText() throws Exception {
		checkInterpretedOutput("equals/eqText.poc");
	}

	@Test
	public void testCompiledEqText() throws Exception {
		checkCompiledOutput("equals/eqText.poc");
	}

	@Test
	public void testTranspiledEqText() throws Exception {
		checkTranspiledOutput("equals/eqText.poc");
	}

	@Test
	public void testInterpretedEqTime() throws Exception {
		checkInterpretedOutput("equals/eqTime.poc");
	}

	@Test
	public void testCompiledEqTime() throws Exception {
		checkCompiledOutput("equals/eqTime.poc");
	}

	@Test
	public void testTranspiledEqTime() throws Exception {
		checkTranspiledOutput("equals/eqTime.poc");
	}

	@Test
	public void testInterpretedEqVersion() throws Exception {
		checkInterpretedOutput("equals/eqVersion.poc");
	}

	@Test
	public void testCompiledEqVersion() throws Exception {
		checkCompiledOutput("equals/eqVersion.poc");
	}

	@Test
	public void testTranspiledEqVersion() throws Exception {
		checkTranspiledOutput("equals/eqVersion.poc");
	}

	@Test
	public void testInterpretedIsBoolean() throws Exception {
		checkInterpretedOutput("equals/isBoolean.poc");
	}

	@Test
	public void testCompiledIsBoolean() throws Exception {
		checkCompiledOutput("equals/isBoolean.poc");
	}

	@Test
	public void testTranspiledIsBoolean() throws Exception {
		checkTranspiledOutput("equals/isBoolean.poc");
	}

	@Test
	public void testInterpretedIsInstance() throws Exception {
		checkInterpretedOutput("equals/isInstance.poc");
	}

	@Test
	public void testCompiledIsInstance() throws Exception {
		checkCompiledOutput("equals/isInstance.poc");
	}

	@Test
	public void testTranspiledIsInstance() throws Exception {
		checkTranspiledOutput("equals/isInstance.poc");
	}

	@Test
	public void testInterpretedIsNotBoolean() throws Exception {
		checkInterpretedOutput("equals/isNotBoolean.poc");
	}

	@Test
	public void testCompiledIsNotBoolean() throws Exception {
		checkCompiledOutput("equals/isNotBoolean.poc");
	}

	@Test
	public void testTranspiledIsNotBoolean() throws Exception {
		checkTranspiledOutput("equals/isNotBoolean.poc");
	}

	@Test
	public void testInterpretedIsNotInstance() throws Exception {
		checkInterpretedOutput("equals/isNotInstance.poc");
	}

	@Test
	public void testCompiledIsNotInstance() throws Exception {
		checkCompiledOutput("equals/isNotInstance.poc");
	}

	@Test
	public void testTranspiledIsNotInstance() throws Exception {
		checkTranspiledOutput("equals/isNotInstance.poc");
	}

	@Test
	public void testInterpretedNeqBoolean() throws Exception {
		checkInterpretedOutput("equals/neqBoolean.poc");
	}

	@Test
	public void testCompiledNeqBoolean() throws Exception {
		checkCompiledOutput("equals/neqBoolean.poc");
	}

	@Test
	public void testTranspiledNeqBoolean() throws Exception {
		checkTranspiledOutput("equals/neqBoolean.poc");
	}

	@Test
	public void testInterpretedNeqCharacter() throws Exception {
		checkInterpretedOutput("equals/neqCharacter.poc");
	}

	@Test
	public void testCompiledNeqCharacter() throws Exception {
		checkCompiledOutput("equals/neqCharacter.poc");
	}

	@Test
	public void testTranspiledNeqCharacter() throws Exception {
		checkTranspiledOutput("equals/neqCharacter.poc");
	}

	@Test
	public void testInterpretedNeqDate() throws Exception {
		checkInterpretedOutput("equals/neqDate.poc");
	}

	@Test
	public void testCompiledNeqDate() throws Exception {
		checkCompiledOutput("equals/neqDate.poc");
	}

	@Test
	public void testTranspiledNeqDate() throws Exception {
		checkTranspiledOutput("equals/neqDate.poc");
	}

	@Test
	public void testInterpretedNeqDateTime() throws Exception {
		checkInterpretedOutput("equals/neqDateTime.poc");
	}

	@Test
	public void testCompiledNeqDateTime() throws Exception {
		checkCompiledOutput("equals/neqDateTime.poc");
	}

	@Test
	public void testTranspiledNeqDateTime() throws Exception {
		checkTranspiledOutput("equals/neqDateTime.poc");
	}

	@Test
	public void testInterpretedNeqDecimal() throws Exception {
		checkInterpretedOutput("equals/neqDecimal.poc");
	}

	@Test
	public void testCompiledNeqDecimal() throws Exception {
		checkCompiledOutput("equals/neqDecimal.poc");
	}

	@Test
	public void testTranspiledNeqDecimal() throws Exception {
		checkTranspiledOutput("equals/neqDecimal.poc");
	}

	@Test
	public void testInterpretedNeqDict() throws Exception {
		checkInterpretedOutput("equals/neqDict.poc");
	}

	@Test
	public void testCompiledNeqDict() throws Exception {
		checkCompiledOutput("equals/neqDict.poc");
	}

	@Test
	public void testTranspiledNeqDict() throws Exception {
		checkTranspiledOutput("equals/neqDict.poc");
	}

	@Test
	public void testInterpretedNeqInteger() throws Exception {
		checkInterpretedOutput("equals/neqInteger.poc");
	}

	@Test
	public void testCompiledNeqInteger() throws Exception {
		checkCompiledOutput("equals/neqInteger.poc");
	}

	@Test
	public void testTranspiledNeqInteger() throws Exception {
		checkTranspiledOutput("equals/neqInteger.poc");
	}

	@Test
	public void testInterpretedNeqList() throws Exception {
		checkInterpretedOutput("equals/neqList.poc");
	}

	@Test
	public void testCompiledNeqList() throws Exception {
		checkCompiledOutput("equals/neqList.poc");
	}

	@Test
	public void testTranspiledNeqList() throws Exception {
		checkTranspiledOutput("equals/neqList.poc");
	}

	@Test
	public void testInterpretedNeqPeriod() throws Exception {
		checkInterpretedOutput("equals/neqPeriod.poc");
	}

	@Test
	public void testCompiledNeqPeriod() throws Exception {
		checkCompiledOutput("equals/neqPeriod.poc");
	}

	@Test
	public void testTranspiledNeqPeriod() throws Exception {
		checkTranspiledOutput("equals/neqPeriod.poc");
	}

	@Test
	public void testInterpretedNeqRange() throws Exception {
		checkInterpretedOutput("equals/neqRange.poc");
	}

	@Test
	public void testCompiledNeqRange() throws Exception {
		checkCompiledOutput("equals/neqRange.poc");
	}

	@Test
	public void testTranspiledNeqRange() throws Exception {
		checkTranspiledOutput("equals/neqRange.poc");
	}

	@Test
	public void testInterpretedNeqSet() throws Exception {
		checkInterpretedOutput("equals/neqSet.poc");
	}

	@Test
	public void testCompiledNeqSet() throws Exception {
		checkCompiledOutput("equals/neqSet.poc");
	}

	@Test
	public void testTranspiledNeqSet() throws Exception {
		checkTranspiledOutput("equals/neqSet.poc");
	}

	@Test
	public void testInterpretedNeqText() throws Exception {
		checkInterpretedOutput("equals/neqText.poc");
	}

	@Test
	public void testCompiledNeqText() throws Exception {
		checkCompiledOutput("equals/neqText.poc");
	}

	@Test
	public void testTranspiledNeqText() throws Exception {
		checkTranspiledOutput("equals/neqText.poc");
	}

	@Test
	public void testInterpretedNeqTime() throws Exception {
		checkInterpretedOutput("equals/neqTime.poc");
	}

	@Test
	public void testCompiledNeqTime() throws Exception {
		checkCompiledOutput("equals/neqTime.poc");
	}

	@Test
	public void testTranspiledNeqTime() throws Exception {
		checkTranspiledOutput("equals/neqTime.poc");
	}

	@Test
	public void testInterpretedReqText() throws Exception {
		checkInterpretedOutput("equals/reqText.poc");
	}

	@Test
	public void testCompiledReqText() throws Exception {
		checkCompiledOutput("equals/reqText.poc");
	}

	@Test
	public void testTranspiledReqText() throws Exception {
		checkTranspiledOutput("equals/reqText.poc");
	}

}

