package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestAdd extends BaseOParserTest {

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
		checkInterpretedOutput("add/addCharacter.poc");
	}

	@Test
	public void testCompiledAddCharacter() throws Exception {
		checkCompiledOutput("add/addCharacter.poc");
	}

	@Test
	public void testTranspiledAddCharacter() throws Exception {
		checkTranspiledOutput("add/addCharacter.poc");
	}

	@Test
	public void testInterpretedAddDate() throws Exception {
		checkInterpretedOutput("add/addDate.poc");
	}

	@Test
	public void testCompiledAddDate() throws Exception {
		checkCompiledOutput("add/addDate.poc");
	}

	@Test
	public void testTranspiledAddDate() throws Exception {
		checkTranspiledOutput("add/addDate.poc");
	}

	@Test
	public void testInterpretedAddDateTime() throws Exception {
		checkInterpretedOutput("add/addDateTime.poc");
	}

	@Test
	public void testCompiledAddDateTime() throws Exception {
		checkCompiledOutput("add/addDateTime.poc");
	}

	@Test
	public void testTranspiledAddDateTime() throws Exception {
		checkTranspiledOutput("add/addDateTime.poc");
	}

	@Test
	public void testInterpretedAddDecimal() throws Exception {
		checkInterpretedOutput("add/addDecimal.poc");
	}

	@Test
	public void testCompiledAddDecimal() throws Exception {
		checkCompiledOutput("add/addDecimal.poc");
	}

	@Test
	public void testTranspiledAddDecimal() throws Exception {
		checkTranspiledOutput("add/addDecimal.poc");
	}

	@Test
	public void testInterpretedAddDict() throws Exception {
		checkInterpretedOutput("add/addDict.poc");
	}

	@Test
	public void testCompiledAddDict() throws Exception {
		checkCompiledOutput("add/addDict.poc");
	}

	@Test
	public void testTranspiledAddDict() throws Exception {
		checkTranspiledOutput("add/addDict.poc");
	}

	@Test
	public void testInterpretedAddInteger() throws Exception {
		checkInterpretedOutput("add/addInteger.poc");
	}

	@Test
	public void testCompiledAddInteger() throws Exception {
		checkCompiledOutput("add/addInteger.poc");
	}

	@Test
	public void testTranspiledAddInteger() throws Exception {
		checkTranspiledOutput("add/addInteger.poc");
	}

	@Test
	public void testInterpretedAddList() throws Exception {
		checkInterpretedOutput("add/addList.poc");
	}

	@Test
	public void testCompiledAddList() throws Exception {
		checkCompiledOutput("add/addList.poc");
	}

	@Test
	public void testTranspiledAddList() throws Exception {
		checkTranspiledOutput("add/addList.poc");
	}

	@Test
	public void testInterpretedAddPeriod() throws Exception {
		checkInterpretedOutput("add/addPeriod.poc");
	}

	@Test
	public void testCompiledAddPeriod() throws Exception {
		checkCompiledOutput("add/addPeriod.poc");
	}

	@Test
	public void testTranspiledAddPeriod() throws Exception {
		checkTranspiledOutput("add/addPeriod.poc");
	}

	@Test
	public void testInterpretedAddSet() throws Exception {
		checkInterpretedOutput("add/addSet.poc");
	}

	@Test
	public void testCompiledAddSet() throws Exception {
		checkCompiledOutput("add/addSet.poc");
	}

	@Test
	public void testTranspiledAddSet() throws Exception {
		checkTranspiledOutput("add/addSet.poc");
	}

	@Test
	public void testInterpretedAddTextCharacter() throws Exception {
		checkInterpretedOutput("add/addTextCharacter.poc");
	}

	@Test
	public void testCompiledAddTextCharacter() throws Exception {
		checkCompiledOutput("add/addTextCharacter.poc");
	}

	@Test
	public void testTranspiledAddTextCharacter() throws Exception {
		checkTranspiledOutput("add/addTextCharacter.poc");
	}

	@Test
	public void testInterpretedAddTextDecimal() throws Exception {
		checkInterpretedOutput("add/addTextDecimal.poc");
	}

	@Test
	public void testCompiledAddTextDecimal() throws Exception {
		checkCompiledOutput("add/addTextDecimal.poc");
	}

	@Test
	public void testTranspiledAddTextDecimal() throws Exception {
		checkTranspiledOutput("add/addTextDecimal.poc");
	}

	@Test
	public void testInterpretedAddTextInteger() throws Exception {
		checkInterpretedOutput("add/addTextInteger.poc");
	}

	@Test
	public void testCompiledAddTextInteger() throws Exception {
		checkCompiledOutput("add/addTextInteger.poc");
	}

	@Test
	public void testTranspiledAddTextInteger() throws Exception {
		checkTranspiledOutput("add/addTextInteger.poc");
	}

	@Test
	public void testInterpretedAddTextText() throws Exception {
		checkInterpretedOutput("add/addTextText.poc");
	}

	@Test
	public void testCompiledAddTextText() throws Exception {
		checkCompiledOutput("add/addTextText.poc");
	}

	@Test
	public void testTranspiledAddTextText() throws Exception {
		checkTranspiledOutput("add/addTextText.poc");
	}

	@Test
	public void testInterpretedAddTime() throws Exception {
		checkInterpretedOutput("add/addTime.poc");
	}

	@Test
	public void testCompiledAddTime() throws Exception {
		checkCompiledOutput("add/addTime.poc");
	}

	@Test
	public void testTranspiledAddTime() throws Exception {
		checkTranspiledOutput("add/addTime.poc");
	}

	@Test
	public void testInterpretedAddTuple() throws Exception {
		checkInterpretedOutput("add/addTuple.poc");
	}

	@Test
	public void testCompiledAddTuple() throws Exception {
		checkCompiledOutput("add/addTuple.poc");
	}

	@Test
	public void testTranspiledAddTuple() throws Exception {
		checkTranspiledOutput("add/addTuple.poc");
	}

}

