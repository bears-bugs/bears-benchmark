package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLoops extends BaseEParserTest {

	@Test
	public void testDoWhile() throws Exception {
		compareResourceEME("loops/doWhile.pec");
	}

	@Test
	public void testDoWhileBreak() throws Exception {
		compareResourceEME("loops/doWhileBreak.pec");
	}

	@Test
	public void testEmbeddedForEach() throws Exception {
		compareResourceEME("loops/embeddedForEach.pec");
	}

	@Test
	public void testForEachBreak() throws Exception {
		compareResourceEME("loops/forEachBreak.pec");
	}

	@Test
	public void testForEachCharacterRange() throws Exception {
		compareResourceEME("loops/forEachCharacterRange.pec");
	}

	@Test
	public void testForEachCharacterRangeWithIndex() throws Exception {
		compareResourceEME("loops/forEachCharacterRangeWithIndex.pec");
	}

	@Test
	public void testForEachDateRange() throws Exception {
		compareResourceEME("loops/forEachDateRange.pec");
	}

	@Test
	public void testForEachDateRangeWithIndex() throws Exception {
		compareResourceEME("loops/forEachDateRangeWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryItem() throws Exception {
		compareResourceEME("loops/forEachDictionaryItem.pec");
	}

	@Test
	public void testForEachDictionaryItemWithIndex() throws Exception {
		compareResourceEME("loops/forEachDictionaryItemWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryKey() throws Exception {
		compareResourceEME("loops/forEachDictionaryKey.pec");
	}

	@Test
	public void testForEachDictionaryKeyWithIndex() throws Exception {
		compareResourceEME("loops/forEachDictionaryKeyWithIndex.pec");
	}

	@Test
	public void testForEachDictionaryValue() throws Exception {
		compareResourceEME("loops/forEachDictionaryValue.pec");
	}

	@Test
	public void testForEachDictionaryValueWithIndex() throws Exception {
		compareResourceEME("loops/forEachDictionaryValueWithIndex.pec");
	}

	@Test
	public void testForEachInstanceList() throws Exception {
		compareResourceEME("loops/forEachInstanceList.pec");
	}

	@Test
	public void testForEachInstanceListWithIndex() throws Exception {
		compareResourceEME("loops/forEachInstanceListWithIndex.pec");
	}

	@Test
	public void testForEachInstanceSet() throws Exception {
		compareResourceEME("loops/forEachInstanceSet.pec");
	}

	@Test
	public void testForEachInstanceSetWithIndex() throws Exception {
		compareResourceEME("loops/forEachInstanceSetWithIndex.pec");
	}

	@Test
	public void testForEachIntegerList() throws Exception {
		compareResourceEME("loops/forEachIntegerList.pec");
	}

	@Test
	public void testForEachIntegerListWithIndex() throws Exception {
		compareResourceEME("loops/forEachIntegerListWithIndex.pec");
	}

	@Test
	public void testForEachIntegerRange() throws Exception {
		compareResourceEME("loops/forEachIntegerRange.pec");
	}

	@Test
	public void testForEachIntegerRangeWithIndex() throws Exception {
		compareResourceEME("loops/forEachIntegerRangeWithIndex.pec");
	}

	@Test
	public void testForEachIntegerSet() throws Exception {
		compareResourceEME("loops/forEachIntegerSet.pec");
	}

	@Test
	public void testForEachIntegerSetWithIndex() throws Exception {
		compareResourceEME("loops/forEachIntegerSetWithIndex.pec");
	}

	@Test
	public void testForEachTimeRange() throws Exception {
		compareResourceEME("loops/forEachTimeRange.pec");
	}

	@Test
	public void testForEachTimeRangeWithIndex() throws Exception {
		compareResourceEME("loops/forEachTimeRangeWithIndex.pec");
	}

	@Test
	public void testForEachTupleList() throws Exception {
		compareResourceEME("loops/forEachTupleList.pec");
	}

	@Test
	public void testForEachTupleListWithIndex() throws Exception {
		compareResourceEME("loops/forEachTupleListWithIndex.pec");
	}

	@Test
	public void testForEachTupleSet() throws Exception {
		compareResourceEME("loops/forEachTupleSet.pec");
	}

	@Test
	public void testForEachTupleSetWithIndex() throws Exception {
		compareResourceEME("loops/forEachTupleSetWithIndex.pec");
	}

	@Test
	public void testWhile() throws Exception {
		compareResourceEME("loops/while.pec");
	}

	@Test
	public void testWhileBreak() throws Exception {
		compareResourceEME("loops/whileBreak.pec");
	}

}

