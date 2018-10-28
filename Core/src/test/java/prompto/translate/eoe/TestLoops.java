package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLoops extends BaseEParserTest {

	@Test
	public void testDoWhile() throws Exception {
		compareResourceEOE("loops/doWhile.pec");
	}

	@Test
	public void testDoWhileBreak() throws Exception {
		compareResourceEOE("loops/doWhileBreak.pec");
	}

	@Test
	public void testEmbeddedForEach() throws Exception {
		compareResourceEOE("loops/embeddedForEach.pec");
	}

	@Test
	public void testForEachBreak() throws Exception {
		compareResourceEOE("loops/forEachBreak.pec");
	}

	@Test
	public void testForEachCharacterRange() throws Exception {
		compareResourceEOE("loops/forEachCharacterRange.pec");
	}

	@Test
	public void testForEachCharacterRangeWithIndex() throws Exception {
		compareResourceEOE("loops/forEachCharacterRangeWithIndex.pec");
	}

	@Test
	public void testForEachDateRange() throws Exception {
		compareResourceEOE("loops/forEachDateRange.pec");
	}

	@Test
	public void testForEachDateRangeWithIndex() throws Exception {
		compareResourceEOE("loops/forEachDateRangeWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryItem() throws Exception {
		compareResourceEOE("loops/forEachDictionaryItem.pec");
	}

	@Test
	public void testForEachDictionaryItemWithIndex() throws Exception {
		compareResourceEOE("loops/forEachDictionaryItemWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryKey() throws Exception {
		compareResourceEOE("loops/forEachDictionaryKey.pec");
	}

	@Test
	public void testForEachDictionaryKeyWithIndex() throws Exception {
		compareResourceEOE("loops/forEachDictionaryKeyWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryValue() throws Exception {
		compareResourceEOE("loops/forEachDictionaryValue.pec");
	}

	@Test
	public void testForEachDictionaryValueWithIndex() throws Exception {
		compareResourceEOE("loops/forEachDictionaryValueWithIndex.pec");
	}

	@Test
	public void testForEachInstanceList() throws Exception {
		compareResourceEOE("loops/forEachInstanceList.pec");
	}

	@Test
	public void testForEachInstanceListWithIndex() throws Exception {
		compareResourceEOE("loops/forEachInstanceListWithIndex.pec");
	}

	@Test
	public void testForEachInstanceSet() throws Exception {
		compareResourceEOE("loops/forEachInstanceSet.pec");
	}

	@Test
	public void testForEachInstanceSetWithIndex() throws Exception {
		compareResourceEOE("loops/forEachInstanceSetWithIndex.pec");
	}

	@Test
	public void testForEachIntegerList() throws Exception {
		compareResourceEOE("loops/forEachIntegerList.pec");
	}

	@Test
	public void testForEachIntegerListWithIndex() throws Exception {
		compareResourceEOE("loops/forEachIntegerListWithIndex.pec");
	}

	@Test
	public void testForEachIntegerRange() throws Exception {
		compareResourceEOE("loops/forEachIntegerRange.pec");
	}

	@Test
	public void testForEachIntegerRangeWithIndex() throws Exception {
		compareResourceEOE("loops/forEachIntegerRangeWithIndex.pec");
	}

	@Test
	public void testForEachIntegerSet() throws Exception {
		compareResourceEOE("loops/forEachIntegerSet.pec");
	}

	@Test
	public void testForEachIntegerSetWithIndex() throws Exception {
		compareResourceEOE("loops/forEachIntegerSetWithIndex.pec");
	}

	@Test
	public void testForEachTimeRange() throws Exception {
		compareResourceEOE("loops/forEachTimeRange.pec");
	}

	@Test
	public void testForEachTimeRangeWithIndex() throws Exception {
		compareResourceEOE("loops/forEachTimeRangeWithIndex.pec");
	}

	@Test
	public void testForEachTupleList() throws Exception {
		compareResourceEOE("loops/forEachTupleList.pec");
	}

	@Test
	public void testForEachTupleListWithIndex() throws Exception {
		compareResourceEOE("loops/forEachTupleListWithIndex.pec");
	}

	@Test
	public void testForEachTupleSet() throws Exception {
		compareResourceEOE("loops/forEachTupleSet.pec");
	}

	@Test
	public void testForEachTupleSetWithIndex() throws Exception {
		compareResourceEOE("loops/forEachTupleSetWithIndex.pec");
	}

	@Test
	public void testWhile() throws Exception {
		compareResourceEOE("loops/while.pec");
	}

	@Test
	public void testWhileBreak() throws Exception {
		compareResourceEOE("loops/whileBreak.pec");
	}

}

