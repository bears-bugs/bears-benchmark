package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLoops extends BaseOParserTest {

	@Test
	public void testDoWhile() throws Exception {
		compareResourceOEO("loops/doWhile.poc");
	}

	@Test
	public void testDoWhileBreak() throws Exception {
		compareResourceOEO("loops/doWhileBreak.poc");
	}

	@Test
	public void testEmbeddedForEach() throws Exception {
		compareResourceOEO("loops/embeddedForEach.poc");
	}

	@Test
	public void testForEachBreak() throws Exception {
		compareResourceOEO("loops/forEachBreak.poc");
	}

	@Test
	public void testForEachCharacterRange() throws Exception {
		compareResourceOEO("loops/forEachCharacterRange.poc");
	}

	@Test
	public void testForEachCharacterRangeWithIndex() throws Exception {
		compareResourceOEO("loops/forEachCharacterRangeWithIndex.poc");
	}

	@Test
	public void testForEachDateRange() throws Exception {
		compareResourceOEO("loops/forEachDateRange.poc");
	}

	@Test
	public void testForEachDateRangeWithIndex() throws Exception {
		compareResourceOEO("loops/forEachDateRangeWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryItem() throws Exception {
		compareResourceOEO("loops/forEachDictionaryItem.poc");
	}

	@Test
	public void testForEachDictionaryItemWithIndex() throws Exception {
		compareResourceOEO("loops/forEachDictionaryItemWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryKey() throws Exception {
		compareResourceOEO("loops/forEachDictionaryKey.poc");
	}

	@Test
	public void testForEachDictionaryKeyWithIndex() throws Exception {
		compareResourceOEO("loops/forEachDictionaryKeyWithIndex.poc");
	}

	@Test
	public void testForEachDictionaryValue() throws Exception {
		compareResourceOEO("loops/forEachDictionaryValue.poc");
	}

	@Test
	public void testForEachDictionaryValueWithIndex() throws Exception {
		compareResourceOEO("loops/forEachDictionaryValueWithIndex.poc");
	}

	@Test
	public void testForEachInstanceList() throws Exception {
		compareResourceOEO("loops/forEachInstanceList.poc");
	}

	@Test
	public void testForEachInstanceListWithIndex() throws Exception {
		compareResourceOEO("loops/forEachInstanceListWithIndex.poc");
	}

	@Test
	public void testForEachInstanceSet() throws Exception {
		compareResourceOEO("loops/forEachInstanceSet.poc");
	}

	@Test
	public void testForEachInstanceSetWithIndex() throws Exception {
		compareResourceOEO("loops/forEachInstanceSetWithIndex.poc");
	}

	@Test
	public void testForEachIntegerList() throws Exception {
		compareResourceOEO("loops/forEachIntegerList.poc");
	}

	@Test
	public void testForEachIntegerListWithIndex() throws Exception {
		compareResourceOEO("loops/forEachIntegerListWithIndex.poc");
	}

	@Test
	public void testForEachIntegerRange() throws Exception {
		compareResourceOEO("loops/forEachIntegerRange.poc");
	}

	@Test
	public void testForEachIntegerRangeWithIndex() throws Exception {
		compareResourceOEO("loops/forEachIntegerRangeWithIndex.poc");
	}

	@Test
	public void testForEachIntegerSet() throws Exception {
		compareResourceOEO("loops/forEachIntegerSet.poc");
	}

	@Test
	public void testForEachIntegerSetWithIndex() throws Exception {
		compareResourceOEO("loops/forEachIntegerSetWithIndex.poc");
	}

	@Test
	public void testForEachTimeRange() throws Exception {
		compareResourceOEO("loops/forEachTimeRange.poc");
	}

	@Test
	public void testForEachTimeRangeWithIndex() throws Exception {
		compareResourceOEO("loops/forEachTimeRangeWithIndex.poc");
	}

	@Test
	public void testForEachTupleList() throws Exception {
		compareResourceOEO("loops/forEachTupleList.poc");
	}

	@Test
	public void testForEachTupleListWithIndex() throws Exception {
		compareResourceOEO("loops/forEachTupleListWithIndex.poc");
	}

	@Test
	public void testForEachTupleSet() throws Exception {
		compareResourceOEO("loops/forEachTupleSet.poc");
	}

	@Test
	public void testForEachTupleSetWithIndex() throws Exception {
		compareResourceOEO("loops/forEachTupleSetWithIndex.poc");
	}

	@Test
	public void testWhile() throws Exception {
		compareResourceOEO("loops/while.poc");
	}

	@Test
	public void testWhileBreak() throws Exception {
		compareResourceOEO("loops/whileBreak.poc");
	}

}

