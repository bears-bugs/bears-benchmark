package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestAdd extends BaseOParserTest {

	@Test
	public void testAddCharacter() throws Exception {
		compareResourceOEO("add/addCharacter.poc");
	}

	@Test
	public void testAddDate() throws Exception {
		compareResourceOEO("add/addDate.poc");
	}

	@Test
	public void testAddDateTime() throws Exception {
		compareResourceOEO("add/addDateTime.poc");
	}

	@Test
	public void testAddDecimal() throws Exception {
		compareResourceOEO("add/addDecimal.poc");
	}

	@Test
	public void testAddDict() throws Exception {
		compareResourceOEO("add/addDict.poc");
	}

	@Test
	public void testAddInteger() throws Exception {
		compareResourceOEO("add/addInteger.poc");
	}

	@Test
	public void testAddList() throws Exception {
		compareResourceOEO("add/addList.poc");
	}

	@Test
	public void testAddPeriod() throws Exception {
		compareResourceOEO("add/addPeriod.poc");
	}

	@Test
	public void testAddSet() throws Exception {
		compareResourceOEO("add/addSet.poc");
	}

	@Test
	public void testAddTextCharacter() throws Exception {
		compareResourceOEO("add/addTextCharacter.poc");
	}

	@Test
	public void testAddTextDecimal() throws Exception {
		compareResourceOEO("add/addTextDecimal.poc");
	}

	@Test
	public void testAddTextInteger() throws Exception {
		compareResourceOEO("add/addTextInteger.poc");
	}

	@Test
	public void testAddTextText() throws Exception {
		compareResourceOEO("add/addTextText.poc");
	}

	@Test
	public void testAddTime() throws Exception {
		compareResourceOEO("add/addTime.poc");
	}

	@Test
	public void testAddTuple() throws Exception {
		compareResourceOEO("add/addTuple.poc");
	}

}

