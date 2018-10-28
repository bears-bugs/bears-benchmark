package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestItem extends BaseEParserTest {

	@Test
	public void testItemDict() throws Exception {
		compareResourceEOE("item/itemDict.pec");
	}

	@Test
	public void testItemList() throws Exception {
		compareResourceEOE("item/itemList.pec");
	}

	@Test
	public void testItemRange() throws Exception {
		compareResourceEOE("item/itemRange.pec");
	}

	@Test
	public void testItemSet() throws Exception {
		compareResourceEOE("item/itemSet.pec");
	}

	@Test
	public void testItemText() throws Exception {
		compareResourceEOE("item/itemText.pec");
	}

}

