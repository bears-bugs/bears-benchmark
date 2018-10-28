package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLoops extends BaseOParserTest {

	@Test
	public void testDoWhile() throws Exception {
		compareResourceOMO("loops/doWhile.poc");
	}

	@Test
	public void testDoWhileBreak() throws Exception {
		compareResourceOMO("loops/doWhileBreak.poc");
	}

	@Test
	public void testEmbeddedForEach() throws Exception {
		compareResourceOMO("loops/embeddedForEach.poc");
	}

	@Test
	public void testForEachBreak() throws Exception {
		compareResourceOMO("loops/forEachBreak.poc");
	}

	@Test
	public void testForEachCharacterRange() throws Exception {
		compareResourceOMO("loops/forEachCharacterRange.poc");
	}

	@Test
	public void testForEachCharacterRangeWithIndex() throws Exception {
		compareResourceOMO("loops/forEachCharacterRangeWithIndex.poc");
	}

	@Test
	public void testForEachDateRange() throws Exception {
		compareResourceOMO("loops/forEachDateRange.poc");
	}

	@Test
	public void testForEachDateRangeWithIndex() throws Exception {
		compareResourceOMO("loops/forEachDateRangeWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryItem() throws Exception {
		compareResourceOMO("loops/forEachDictionaryItem.poc");
	}

	@Test
	public void testForEachDictionaryItemWithIndex() throws Exception {
		compareResourceOMO("loops/forEachDictionaryItemWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryKey() throws Exception {
		compareResourceOMO("loops/forEachDictionaryKey.poc");
	}

	@Test
	public void testForEachDictionaryKeyWithIndex() throws Exception {
		compareResourceOMO("loops/forEachDictionaryKeyWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryValue() throws Exception {
		compareResourceOMO("loops/forEachDictionaryValue.poc");
	}

	@Test
	public void testForEachDictionaryValueWithIndex() throws Exception {
		compareResourceOMO("loops/forEachDictionaryValueWithIndex.poc");
	}

	@Test
	public void testForEachInstanceList() throws Exception {
		compareResourceOMO("loops/forEachInstanceList.poc");
	}

	@Test
	public void testForEachInstanceListWithIndex() throws Exception {
		compareResourceOMO("loops/forEachInstanceListWithIndex.poc");
	}

	@Test
	public void testForEachInstanceSet() throws Exception {
		compareResourceOMO("loops/forEachInstanceSet.poc");
	}

	@Test
	public void testForEachInstanceSetWithIndex() throws Exception {
		compareResourceOMO("loops/forEachInstanceSetWithIndex.poc");
	}

	@Test
	public void testForEachIntegerList() throws Exception {
		compareResourceOMO("loops/forEachIntegerList.poc");
	}

	@Test
	public void testForEachIntegerListWithIndex() throws Exception {
		compareResourceOMO("loops/forEachIntegerListWithIndex.poc");
	}

	@Test
	public void testForEachIntegerRange() throws Exception {
		compareResourceOMO("loops/forEachIntegerRange.poc");
	}

	@Test
	public void testForEachIntegerRangeWithIndex() throws Exception {
		compareResourceOMO("loops/forEachIntegerRangeWithIndex.poc");
	}

	@Test
	public void testForEachIntegerSet() throws Exception {
		compareResourceOMO("loops/forEachIntegerSet.poc");
	}

	@Test
	public void testForEachIntegerSetWithIndex() throws Exception {
		compareResourceOMO("loops/forEachIntegerSetWithIndex.poc");
	}

	@Test
	public void testForEachTimeRange() throws Exception {
		compareResourceOMO("loops/forEachTimeRange.poc");
	}

	@Test
	public void testForEachTimeRangeWithIndex() throws Exception {
		compareResourceOMO("loops/forEachTimeRangeWithIndex.poc");
	}

	@Test
	public void testForEachTupleList() throws Exception {
		compareResourceOMO("loops/forEachTupleList.poc");
	}

	@Test
	public void testForEachTupleListWithIndex() throws Exception {
		compareResourceOMO("loops/forEachTupleListWithIndex.poc");
	}

	@Test
	public void testForEachTupleSet() throws Exception {
		compareResourceOMO("loops/forEachTupleSet.poc");
	}

	@Test
	public void testForEachTupleSetWithIndex() throws Exception {
		compareResourceOMO("loops/forEachTupleSetWithIndex.poc");
	}

	@Test
	public void testWhile() throws Exception {
		compareResourceOMO("loops/while.poc");
	}

	@Test
	public void testWhileBreak() throws Exception {
		compareResourceOMO("loops/whileBreak.poc");
	}

}

