package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestLoops extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedDoWhile() throws Exception {
		checkInterpretedOutput("loops/doWhile.pec");
	}

	@Test
	public void testCompiledDoWhile() throws Exception {
		checkCompiledOutput("loops/doWhile.pec");
	}

	@Test
	public void testTranspiledDoWhile() throws Exception {
		checkTranspiledOutput("loops/doWhile.pec");
	}

	@Test
	public void testInterpretedDoWhileBreak() throws Exception {
		checkInterpretedOutput("loops/doWhileBreak.pec");
	}

	@Test
	public void testCompiledDoWhileBreak() throws Exception {
		checkCompiledOutput("loops/doWhileBreak.pec");
	}

	@Test
	public void testTranspiledDoWhileBreak() throws Exception {
		checkTranspiledOutput("loops/doWhileBreak.pec");
	}

	@Test
	public void testInterpretedEmbeddedForEach() throws Exception {
		checkInterpretedOutput("loops/embeddedForEach.pec");
	}

	@Test
	public void testCompiledEmbeddedForEach() throws Exception {
		checkCompiledOutput("loops/embeddedForEach.pec");
	}

	@Test
	public void testTranspiledEmbeddedForEach() throws Exception {
		checkTranspiledOutput("loops/embeddedForEach.pec");
	}

	@Test
	public void testInterpretedForEachBreak() throws Exception {
		checkInterpretedOutput("loops/forEachBreak.pec");
	}

	@Test
	public void testCompiledForEachBreak() throws Exception {
		checkCompiledOutput("loops/forEachBreak.pec");
	}

	@Test
	public void testTranspiledForEachBreak() throws Exception {
		checkTranspiledOutput("loops/forEachBreak.pec");
	}

	@Test
	public void testInterpretedForEachCharacterRange() throws Exception {
		checkInterpretedOutput("loops/forEachCharacterRange.pec");
	}

	@Test
	public void testCompiledForEachCharacterRange() throws Exception {
		checkCompiledOutput("loops/forEachCharacterRange.pec");
	}

	@Test
	public void testTranspiledForEachCharacterRange() throws Exception {
		checkTranspiledOutput("loops/forEachCharacterRange.pec");
	}

	@Test
	public void testInterpretedForEachCharacterRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachCharacterRangeWithIndex.pec");
	}

	@Test
	public void testCompiledForEachCharacterRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachCharacterRangeWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachCharacterRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachCharacterRangeWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachDateRange() throws Exception {
		checkInterpretedOutput("loops/forEachDateRange.pec");
	}

	@Test
	public void testCompiledForEachDateRange() throws Exception {
		checkCompiledOutput("loops/forEachDateRange.pec");
	}

	@Test
	public void testTranspiledForEachDateRange() throws Exception {
		checkTranspiledOutput("loops/forEachDateRange.pec");
	}

	@Test
	public void testInterpretedForEachDateRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDateRangeWithIndex.pec");
	}

	@Test
	public void testCompiledForEachDateRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDateRangeWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachDateRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDateRangeWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryItem() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryItem.pec");
	}

	@Test
	public void testCompiledForEachDictionaryItem() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryItem.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryItem() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryItem.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryItemWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryItemWithIndex.pec");
	}

	@Test
	public void testCompiledForEachDictionaryItemWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryItemWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryItemWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryItemWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryKey() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryKey.pec");
	}

	@Test
	public void testCompiledForEachDictionaryKey() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryKey.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryKey() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryKey.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryKeyWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryKeyWithIndex.pec");
	}

	@Test
	public void testCompiledForEachDictionaryKeyWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryKeyWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryKeyWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryKeyWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryValue() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryValue.pec");
	}

	@Test
	public void testCompiledForEachDictionaryValue() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryValue.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryValue() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryValue.pec");
	}

	@Test
	public void testInterpretedForEachDictionaryValueWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryValueWithIndex.pec");
	}

	@Test
	public void testCompiledForEachDictionaryValueWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryValueWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachDictionaryValueWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryValueWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachInstanceList() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceList.pec");
	}

	@Test
	public void testCompiledForEachInstanceList() throws Exception {
		checkCompiledOutput("loops/forEachInstanceList.pec");
	}

	@Test
	public void testTranspiledForEachInstanceList() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceList.pec");
	}

	@Test
	public void testInterpretedForEachInstanceListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceListWithIndex.pec");
	}

	@Test
	public void testCompiledForEachInstanceListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachInstanceListWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachInstanceListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceListWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachInstanceSet() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceSet.pec");
	}

	@Test
	public void testCompiledForEachInstanceSet() throws Exception {
		checkCompiledOutput("loops/forEachInstanceSet.pec");
	}

	@Test
	public void testTranspiledForEachInstanceSet() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceSet.pec");
	}

	@Test
	public void testInterpretedForEachInstanceSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceSetWithIndex.pec");
	}

	@Test
	public void testCompiledForEachInstanceSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachInstanceSetWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachInstanceSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceSetWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachIntegerList() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerList.pec");
	}

	@Test
	public void testCompiledForEachIntegerList() throws Exception {
		checkCompiledOutput("loops/forEachIntegerList.pec");
	}

	@Test
	public void testTranspiledForEachIntegerList() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerList.pec");
	}

	@Test
	public void testInterpretedForEachIntegerListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerListWithIndex.pec");
	}

	@Test
	public void testCompiledForEachIntegerListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerListWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachIntegerListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerListWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachIntegerRange() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerRange.pec");
	}

	@Test
	public void testCompiledForEachIntegerRange() throws Exception {
		checkCompiledOutput("loops/forEachIntegerRange.pec");
	}

	@Test
	public void testTranspiledForEachIntegerRange() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerRange.pec");
	}

	@Test
	public void testInterpretedForEachIntegerRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerRangeWithIndex.pec");
	}

	@Test
	public void testCompiledForEachIntegerRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerRangeWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachIntegerRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerRangeWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachIntegerSet() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerSet.pec");
	}

	@Test
	public void testCompiledForEachIntegerSet() throws Exception {
		checkCompiledOutput("loops/forEachIntegerSet.pec");
	}

	@Test
	public void testTranspiledForEachIntegerSet() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerSet.pec");
	}

	@Test
	public void testInterpretedForEachIntegerSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerSetWithIndex.pec");
	}

	@Test
	public void testCompiledForEachIntegerSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerSetWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachIntegerSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerSetWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachTimeRange() throws Exception {
		checkInterpretedOutput("loops/forEachTimeRange.pec");
	}

	@Test
	public void testCompiledForEachTimeRange() throws Exception {
		checkCompiledOutput("loops/forEachTimeRange.pec");
	}

	@Test
	public void testTranspiledForEachTimeRange() throws Exception {
		checkTranspiledOutput("loops/forEachTimeRange.pec");
	}

	@Test
	public void testInterpretedForEachTimeRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTimeRangeWithIndex.pec");
	}

	@Test
	public void testCompiledForEachTimeRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTimeRangeWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachTimeRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTimeRangeWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachTupleList() throws Exception {
		checkInterpretedOutput("loops/forEachTupleList.pec");
	}

	@Test
	public void testCompiledForEachTupleList() throws Exception {
		checkCompiledOutput("loops/forEachTupleList.pec");
	}

	@Test
	public void testTranspiledForEachTupleList() throws Exception {
		checkTranspiledOutput("loops/forEachTupleList.pec");
	}

	@Test
	public void testInterpretedForEachTupleListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTupleListWithIndex.pec");
	}

	@Test
	public void testCompiledForEachTupleListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTupleListWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachTupleListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTupleListWithIndex.pec");
	}

	@Test
	public void testInterpretedForEachTupleSet() throws Exception {
		checkInterpretedOutput("loops/forEachTupleSet.pec");
	}

	@Test
	public void testCompiledForEachTupleSet() throws Exception {
		checkCompiledOutput("loops/forEachTupleSet.pec");
	}

	@Test
	public void testTranspiledForEachTupleSet() throws Exception {
		checkTranspiledOutput("loops/forEachTupleSet.pec");
	}

	@Test
	public void testInterpretedForEachTupleSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTupleSetWithIndex.pec");
	}

	@Test
	public void testCompiledForEachTupleSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTupleSetWithIndex.pec");
	}

	@Test
	public void testTranspiledForEachTupleSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTupleSetWithIndex.pec");
	}

	@Test
	public void testInterpretedWhile() throws Exception {
		checkInterpretedOutput("loops/while.pec");
	}

	@Test
	public void testCompiledWhile() throws Exception {
		checkCompiledOutput("loops/while.pec");
	}

	@Test
	public void testTranspiledWhile() throws Exception {
		checkTranspiledOutput("loops/while.pec");
	}

	@Test
	public void testInterpretedWhileBreak() throws Exception {
		checkInterpretedOutput("loops/whileBreak.pec");
	}

	@Test
	public void testCompiledWhileBreak() throws Exception {
		checkCompiledOutput("loops/whileBreak.pec");
	}

	@Test
	public void testTranspiledWhileBreak() throws Exception {
		checkTranspiledOutput("loops/whileBreak.pec");
	}

}

