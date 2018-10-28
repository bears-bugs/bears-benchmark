package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestItem extends BaseOParserTest {

	@Test
	public void testItemDict() throws Exception {
		compareResourceOEO("item/itemDict.poc");
	}

	@Test
	public void testItemList() throws Exception {
		compareResourceOEO("item/itemList.poc");
	}

	@Test
	public void testItemRange() throws Exception {
		compareResourceOEO("item/itemRange.poc");
	}

	@Test
	public void testItemSet() throws Exception {
		compareResourceOEO("item/itemSet.poc");
	}

	@Test
	public void testItemText() throws Exception {
		compareResourceOEO("item/itemText.poc");
	}

}

