package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestAdd extends BaseOParserTest {

	@Test
	public void testAddCharacter() throws Exception {
		compareResourceOMO("add/addCharacter.poc");
	}

	@Test
	public void testAddDate() throws Exception {
		compareResourceOMO("add/addDate.poc");
	}

	@Test
	public void testAddDateTime() throws Exception {
		compareResourceOMO("add/addDateTime.poc");
	}

	@Test
	public void testAddDecimal() throws Exception {
		compareResourceOMO("add/addDecimal.poc");
	}

	@Test
	public void testAddDict() throws Exception {
		compareResourceOMO("add/addDict.poc");
	}

	@Test
	public void testAddInteger() throws Exception {
		compareResourceOMO("add/addInteger.poc");
	}

	@Test
	public void testAddList() throws Exception {
		compareResourceOMO("add/addList.poc");
	}

	@Test
	public void testAddPeriod() throws Exception {
		compareResourceOMO("add/addPeriod.poc");
	}

	@Test
	public void testAddSet() throws Exception {
		compareResourceOMO("add/addSet.poc");
	}

	@Test
	public void testAddTextCharacter() throws Exception {
		compareResourceOMO("add/addTextCharacter.poc");
	}

	@Test
	public void testAddTextDecimal() throws Exception {
		compareResourceOMO("add/addTextDecimal.poc");
	}

	@Test
	public void testAddTextInteger() throws Exception {
		compareResourceOMO("add/addTextInteger.poc");
	}

	@Test
	public void testAddTextText() throws Exception {
		compareResourceOMO("add/addTextText.poc");
	}

	@Test
	public void testAddTime() throws Exception {
		compareResourceOMO("add/addTime.poc");
	}

	@Test
	public void testAddTuple() throws Exception {
		compareResourceOMO("add/addTuple.poc");
	}

}

