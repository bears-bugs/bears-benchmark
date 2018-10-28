package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestItem extends BaseOParserTest {

	@Test
	public void testItemDict() throws Exception {
		compareResourceOMO("item/itemDict.poc");
	}

	@Test
	public void testItemList() throws Exception {
		compareResourceOMO("item/itemList.poc");
	}

	@Test
	public void testItemRange() throws Exception {
		compareResourceOMO("item/itemRange.poc");
	}

	@Test
	public void testItemSet() throws Exception {
		compareResourceOMO("item/itemSet.poc");
	}

	@Test
	public void testItemText() throws Exception {
		compareResourceOMO("item/itemText.poc");
	}

}

