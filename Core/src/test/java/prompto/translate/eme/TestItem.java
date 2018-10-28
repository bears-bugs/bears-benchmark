package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestItem extends BaseEParserTest {

	@Test
	public void testItemDict() throws Exception {
		compareResourceEME("item/itemDict.pec");
	}

	@Test
	public void testItemList() throws Exception {
		compareResourceEME("item/itemList.pec");
	}

	@Test
	public void testItemRange() throws Exception {
		compareResourceEME("item/itemRange.pec");
	}

	@Test
	public void testItemSet() throws Exception {
		compareResourceEME("item/itemSet.pec");
	}

	@Test
	public void testItemText() throws Exception {
		compareResourceEME("item/itemText.pec");
	}

}

