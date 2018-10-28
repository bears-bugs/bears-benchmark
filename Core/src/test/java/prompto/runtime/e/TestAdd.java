package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestAdd extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAddCharacter() throws Exception {
		checkInterpretedOutput("add/addCharacter.pec");
	}

	@Test
	public void testCompiledAddCharacter() throws Exception {
		checkCompiledOutput("add/addCharacter.pec");
	}

	@Test
	public void testTranspiledAddCharacter() throws Exception {
		checkTranspiledOutput("add/addCharacter.pec");
	}

	@Test
	public void testInterpretedAddDate() throws Exception {
		checkInterpretedOutput("add/addDate.pec");
	}

	@Test
	public void testCompiledAddDate() throws Exception {
		checkCompiledOutput("add/addDate.pec");
	}

	@Test
	public void testTranspiledAddDate() throws Exception {
		checkTranspiledOutput("add/addDate.pec");
	}

	@Test
	public void testInterpretedAddDateTime() throws Exception {
		checkInterpretedOutput("add/addDateTime.pec");
	}

	@Test
	public void testCompiledAddDateTime() throws Exception {
		checkCompiledOutput("add/addDateTime.pec");
	}

	@Test
	public void testTranspiledAddDateTime() throws Exception {
		checkTranspiledOutput("add/addDateTime.pec");
	}

	@Test
	public void testInterpretedAddDecimal() throws Exception {
		checkInterpretedOutput("add/addDecimal.pec");
	}

	@Test
	public void testCompiledAddDecimal() throws Exception {
		checkCompiledOutput("add/addDecimal.pec");
	}

	@Test
	public void testTranspiledAddDecimal() throws Exception {
		checkTranspiledOutput("add/addDecimal.pec");
	}

	@Test
	public void testInterpretedAddDecimalEnum() throws Exception {
		checkInterpretedOutput("add/addDecimalEnum.pec");
	}

	@Test
	public void testCompiledAddDecimalEnum() throws Exception {
		checkCompiledOutput("add/addDecimalEnum.pec");
	}

	@Test
	public void testTranspiledAddDecimalEnum() throws Exception {
		checkTranspiledOutput("add/addDecimalEnum.pec");
	}

	@Test
	public void testInterpretedAddDict() throws Exception {
		checkInterpretedOutput("add/addDict.pec");
	}

	@Test
	public void testCompiledAddDict() throws Exception {
		checkCompiledOutput("add/addDict.pec");
	}

	@Test
	public void testTranspiledAddDict() throws Exception {
		checkTranspiledOutput("add/addDict.pec");
	}

	@Test
	public void testInterpretedAddInteger() throws Exception {
		checkInterpretedOutput("add/addInteger.pec");
	}

	@Test
	public void testCompiledAddInteger() throws Exception {
		checkCompiledOutput("add/addInteger.pec");
	}

	@Test
	public void testTranspiledAddInteger() throws Exception {
		checkTranspiledOutput("add/addInteger.pec");
	}

	@Test
	public void testInterpretedAddIntegerEnum() throws Exception {
		checkInterpretedOutput("add/addIntegerEnum.pec");
	}

	@Test
	public void testCompiledAddIntegerEnum() throws Exception {
		checkCompiledOutput("add/addIntegerEnum.pec");
	}

	@Test
	public void testTranspiledAddIntegerEnum() throws Exception {
		checkTranspiledOutput("add/addIntegerEnum.pec");
	}

	@Test
	public void testInterpretedAddList() throws Exception {
		checkInterpretedOutput("add/addList.pec");
	}

	@Test
	public void testCompiledAddList() throws Exception {
		checkCompiledOutput("add/addList.pec");
	}

	@Test
	public void testTranspiledAddList() throws Exception {
		checkTranspiledOutput("add/addList.pec");
	}

	@Test
	public void testInterpretedAddPeriod() throws Exception {
		checkInterpretedOutput("add/addPeriod.pec");
	}

	@Test
	public void testCompiledAddPeriod() throws Exception {
		checkCompiledOutput("add/addPeriod.pec");
	}

	@Test
	public void testTranspiledAddPeriod() throws Exception {
		checkTranspiledOutput("add/addPeriod.pec");
	}

	@Test
	public void testInterpretedAddSet() throws Exception {
		checkInterpretedOutput("add/addSet.pec");
	}

	@Test
	public void testCompiledAddSet() throws Exception {
		checkCompiledOutput("add/addSet.pec");
	}

	@Test
	public void testTranspiledAddSet() throws Exception {
		checkTranspiledOutput("add/addSet.pec");
	}

	@Test
	public void testInterpretedAddTextCharacter() throws Exception {
		checkInterpretedOutput("add/addTextCharacter.pec");
	}

	@Test
	public void testCompiledAddTextCharacter() throws Exception {
		checkCompiledOutput("add/addTextCharacter.pec");
	}

	@Test
	public void testTranspiledAddTextCharacter() throws Exception {
		checkTranspiledOutput("add/addTextCharacter.pec");
	}

	@Test
	public void testInterpretedAddTextDecimal() throws Exception {
		checkInterpretedOutput("add/addTextDecimal.pec");
	}

	@Test
	public void testCompiledAddTextDecimal() throws Exception {
		checkCompiledOutput("add/addTextDecimal.pec");
	}

	@Test
	public void testTranspiledAddTextDecimal() throws Exception {
		checkTranspiledOutput("add/addTextDecimal.pec");
	}

	@Test
	public void testInterpretedAddTextEnum() throws Exception {
		checkInterpretedOutput("add/addTextEnum.pec");
	}

	@Test
	public void testCompiledAddTextEnum() throws Exception {
		checkCompiledOutput("add/addTextEnum.pec");
	}

	@Test
	public void testTranspiledAddTextEnum() throws Exception {
		checkTranspiledOutput("add/addTextEnum.pec");
	}

	@Test
	public void testInterpretedAddTextInteger() throws Exception {
		checkInterpretedOutput("add/addTextInteger.pec");
	}

	@Test
	public void testCompiledAddTextInteger() throws Exception {
		checkCompiledOutput("add/addTextInteger.pec");
	}

	@Test
	public void testTranspiledAddTextInteger() throws Exception {
		checkTranspiledOutput("add/addTextInteger.pec");
	}

	@Test
	public void testInterpretedAddTextText() throws Exception {
		checkInterpretedOutput("add/addTextText.pec");
	}

	@Test
	public void testCompiledAddTextText() throws Exception {
		checkCompiledOutput("add/addTextText.pec");
	}

	@Test
	public void testTranspiledAddTextText() throws Exception {
		checkTranspiledOutput("add/addTextText.pec");
	}

	@Test
	public void testInterpretedAddTime() throws Exception {
		checkInterpretedOutput("add/addTime.pec");
	}

	@Test
	public void testCompiledAddTime() throws Exception {
		checkCompiledOutput("add/addTime.pec");
	}

	@Test
	public void testTranspiledAddTime() throws Exception {
		checkTranspiledOutput("add/addTime.pec");
	}

	@Test
	public void testInterpretedAddTuple() throws Exception {
		checkInterpretedOutput("add/addTuple.pec");
	}

	@Test
	public void testCompiledAddTuple() throws Exception {
		checkCompiledOutput("add/addTuple.pec");
	}

	@Test
	public void testTranspiledAddTuple() throws Exception {
		checkTranspiledOutput("add/addTuple.pec");
	}

}

