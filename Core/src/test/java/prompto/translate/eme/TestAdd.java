package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestAdd extends BaseEParserTest {

	@Test
	public void testAddCharacter() throws Exception {
		compareResourceEME("add/addCharacter.pec");
	}

	@Test
	public void testAddDate() throws Exception {
		compareResourceEME("add/addDate.pec");
	}

	@Test
	public void testAddDateTime() throws Exception {
		compareResourceEME("add/addDateTime.pec");
	}

	@Test
	public void testAddDecimal() throws Exception {
		compareResourceEME("add/addDecimal.pec");
	}

	@Test
	public void testAddDecimalEnum() throws Exception {
		compareResourceEME("add/addDecimalEnum.pec");
	}

	@Test
	public void testAddDict() throws Exception {
		compareResourceEME("add/addDict.pec");
	}

	@Test
	public void testAddInteger() throws Exception {
		compareResourceEME("add/addInteger.pec");
	}

	@Test
	public void testAddIntegerEnum() throws Exception {
		compareResourceEME("add/addIntegerEnum.pec");
	}

	@Test
	public void testAddList() throws Exception {
		compareResourceEME("add/addList.pec");
	}

	@Test
	public void testAddPeriod() throws Exception {
		compareResourceEME("add/addPeriod.pec");
	}

	@Test
	public void testAddSet() throws Exception {
		compareResourceEME("add/addSet.pec");
	}

	@Test
	public void testAddTextCharacter() throws Exception {
		compareResourceEME("add/addTextCharacter.pec");
	}

	@Test
	public void testAddTextDecimal() throws Exception {
		compareResourceEME("add/addTextDecimal.pec");
	}

	@Test
	public void testAddTextEnum() throws Exception {
		compareResourceEME("add/addTextEnum.pec");
	}

	@Test
	public void testAddTextInteger() throws Exception {
		compareResourceEME("add/addTextInteger.pec");
	}

	@Test
	public void testAddTextText() throws Exception {
		compareResourceEME("add/addTextText.pec");
	}

	@Test
	public void testAddTime() throws Exception {
		compareResourceEME("add/addTime.pec");
	}

	@Test
	public void testAddTuple() throws Exception {
		compareResourceEME("add/addTuple.pec");
	}

}

