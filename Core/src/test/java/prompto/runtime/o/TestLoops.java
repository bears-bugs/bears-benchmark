package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestLoops extends BaseOParserTest {

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
		checkInterpretedOutput("loops/doWhile.poc");
	}

	@Test
	public void testCompiledDoWhile() throws Exception {
		checkCompiledOutput("loops/doWhile.poc");
	}

	@Test
	public void testTranspiledDoWhile() throws Exception {
		checkTranspiledOutput("loops/doWhile.poc");
	}

	@Test
	public void testInterpretedDoWhileBreak() throws Exception {
		checkInterpretedOutput("loops/doWhileBreak.poc");
	}

	@Test
	public void testCompiledDoWhileBreak() throws Exception {
		checkCompiledOutput("loops/doWhileBreak.poc");
	}

	@Test
	public void testTranspiledDoWhileBreak() throws Exception {
		checkTranspiledOutput("loops/doWhileBreak.poc");
	}

	@Test
	public void testInterpretedEmbeddedForEach() throws Exception {
		checkInterpretedOutput("loops/embeddedForEach.poc");
	}

	@Test
	public void testCompiledEmbeddedForEach() throws Exception {
		checkCompiledOutput("loops/embeddedForEach.poc");
	}

	@Test
	public void testTranspiledEmbeddedForEach() throws Exception {
		checkTranspiledOutput("loops/embeddedForEach.poc");
	}

	@Test
	public void testInterpretedForEachBreak() throws Exception {
		checkInterpretedOutput("loops/forEachBreak.poc");
	}

	@Test
	public void testCompiledForEachBreak() throws Exception {
		checkCompiledOutput("loops/forEachBreak.poc");
	}

	@Test
	public void testTranspiledForEachBreak() throws Exception {
		checkTranspiledOutput("loops/forEachBreak.poc");
	}

	@Test
	public void testInterpretedForEachCharacterRange() throws Exception {
		checkInterpretedOutput("loops/forEachCharacterRange.poc");
	}

	@Test
	public void testCompiledForEachCharacterRange() throws Exception {
		checkCompiledOutput("loops/forEachCharacterRange.poc");
	}

	@Test
	public void testTranspiledForEachCharacterRange() throws Exception {
		checkTranspiledOutput("loops/forEachCharacterRange.poc");
	}

	@Test
	public void testInterpretedForEachCharacterRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachCharacterRangeWithIndex.poc");
	}

	@Test
	public void testCompiledForEachCharacterRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachCharacterRangeWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachCharacterRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachCharacterRangeWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachDateRange() throws Exception {
		checkInterpretedOutput("loops/forEachDateRange.poc");
	}

	@Test
	public void testCompiledForEachDateRange() throws Exception {
		checkCompiledOutput("loops/forEachDateRange.poc");
	}

	@Test
	public void testTranspiledForEachDateRange() throws Exception {
		checkTranspiledOutput("loops/forEachDateRange.poc");
	}

	@Test
	public void testInterpretedForEachDateRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDateRangeWithIndex.poc");
	}

	@Test
	public void testCompiledForEachDateRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDateRangeWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachDateRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDateRangeWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryItem() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryItem.poc");
	}

	@Test
	public void testCompiledForEachDictionaryItem() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryItem.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryItem() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryItem.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryItemWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryItemWithIndex.poc");
	}

	@Test
	public void testCompiledForEachDictionaryItemWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryItemWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryItemWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryItemWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryKey() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryKey.poc");
	}

	@Test
	public void testCompiledForEachDictionaryKey() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryKey.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryKey() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryKey.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryKeyWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryKeyWithIndex.poc");
	}

	@Test
	public void testCompiledForEachDictionaryKeyWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryKeyWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryKeyWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryKeyWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryValue() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryValue.poc");
	}

	@Test
	public void testCompiledForEachDictionaryValue() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryValue.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryValue() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryValue.poc");
	}

	@Test
	public void testInterpretedForEachDictionaryValueWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachDictionaryValueWithIndex.poc");
	}

	@Test
	public void testCompiledForEachDictionaryValueWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachDictionaryValueWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachDictionaryValueWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachDictionaryValueWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachInstanceList() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceList.poc");
	}

	@Test
	public void testCompiledForEachInstanceList() throws Exception {
		checkCompiledOutput("loops/forEachInstanceList.poc");
	}

	@Test
	public void testTranspiledForEachInstanceList() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceList.poc");
	}

	@Test
	public void testInterpretedForEachInstanceListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceListWithIndex.poc");
	}

	@Test
	public void testCompiledForEachInstanceListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachInstanceListWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachInstanceListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceListWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachInstanceSet() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceSet.poc");
	}

	@Test
	public void testCompiledForEachInstanceSet() throws Exception {
		checkCompiledOutput("loops/forEachInstanceSet.poc");
	}

	@Test
	public void testTranspiledForEachInstanceSet() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceSet.poc");
	}

	@Test
	public void testInterpretedForEachInstanceSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachInstanceSetWithIndex.poc");
	}

	@Test
	public void testCompiledForEachInstanceSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachInstanceSetWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachInstanceSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachInstanceSetWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachIntegerList() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerList.poc");
	}

	@Test
	public void testCompiledForEachIntegerList() throws Exception {
		checkCompiledOutput("loops/forEachIntegerList.poc");
	}

	@Test
	public void testTranspiledForEachIntegerList() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerList.poc");
	}

	@Test
	public void testInterpretedForEachIntegerListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerListWithIndex.poc");
	}

	@Test
	public void testCompiledForEachIntegerListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerListWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachIntegerListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerListWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachIntegerRange() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerRange.poc");
	}

	@Test
	public void testCompiledForEachIntegerRange() throws Exception {
		checkCompiledOutput("loops/forEachIntegerRange.poc");
	}

	@Test
	public void testTranspiledForEachIntegerRange() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerRange.poc");
	}

	@Test
	public void testInterpretedForEachIntegerRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerRangeWithIndex.poc");
	}

	@Test
	public void testCompiledForEachIntegerRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerRangeWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachIntegerRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerRangeWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachIntegerSet() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerSet.poc");
	}

	@Test
	public void testCompiledForEachIntegerSet() throws Exception {
		checkCompiledOutput("loops/forEachIntegerSet.poc");
	}

	@Test
	public void testTranspiledForEachIntegerSet() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerSet.poc");
	}

	@Test
	public void testInterpretedForEachIntegerSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachIntegerSetWithIndex.poc");
	}

	@Test
	public void testCompiledForEachIntegerSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachIntegerSetWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachIntegerSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachIntegerSetWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachTimeRange() throws Exception {
		checkInterpretedOutput("loops/forEachTimeRange.poc");
	}

	@Test
	public void testCompiledForEachTimeRange() throws Exception {
		checkCompiledOutput("loops/forEachTimeRange.poc");
	}

	@Test
	public void testTranspiledForEachTimeRange() throws Exception {
		checkTranspiledOutput("loops/forEachTimeRange.poc");
	}

	@Test
	public void testInterpretedForEachTimeRangeWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTimeRangeWithIndex.poc");
	}

	@Test
	public void testCompiledForEachTimeRangeWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTimeRangeWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachTimeRangeWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTimeRangeWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachTupleList() throws Exception {
		checkInterpretedOutput("loops/forEachTupleList.poc");
	}

	@Test
	public void testCompiledForEachTupleList() throws Exception {
		checkCompiledOutput("loops/forEachTupleList.poc");
	}

	@Test
	public void testTranspiledForEachTupleList() throws Exception {
		checkTranspiledOutput("loops/forEachTupleList.poc");
	}

	@Test
	public void testInterpretedForEachTupleListWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTupleListWithIndex.poc");
	}

	@Test
	public void testCompiledForEachTupleListWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTupleListWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachTupleListWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTupleListWithIndex.poc");
	}

	@Test
	public void testInterpretedForEachTupleSet() throws Exception {
		checkInterpretedOutput("loops/forEachTupleSet.poc");
	}

	@Test
	public void testCompiledForEachTupleSet() throws Exception {
		checkCompiledOutput("loops/forEachTupleSet.poc");
	}

	@Test
	public void testTranspiledForEachTupleSet() throws Exception {
		checkTranspiledOutput("loops/forEachTupleSet.poc");
	}

	@Test
	public void testInterpretedForEachTupleSetWithIndex() throws Exception {
		checkInterpretedOutput("loops/forEachTupleSetWithIndex.poc");
	}

	@Test
	public void testCompiledForEachTupleSetWithIndex() throws Exception {
		checkCompiledOutput("loops/forEachTupleSetWithIndex.poc");
	}

	@Test
	public void testTranspiledForEachTupleSetWithIndex() throws Exception {
		checkTranspiledOutput("loops/forEachTupleSetWithIndex.poc");
	}

	@Test
	public void testInterpretedWhile() throws Exception {
		checkInterpretedOutput("loops/while.poc");
	}

	@Test
	public void testCompiledWhile() throws Exception {
		checkCompiledOutput("loops/while.poc");
	}

	@Test
	public void testTranspiledWhile() throws Exception {
		checkTranspiledOutput("loops/while.poc");
	}

	@Test
	public void testInterpretedWhileBreak() throws Exception {
		checkInterpretedOutput("loops/whileBreak.poc");
	}

	@Test
	public void testCompiledWhileBreak() throws Exception {
		checkCompiledOutput("loops/whileBreak.poc");
	}

	@Test
	public void testTranspiledWhileBreak() throws Exception {
		checkTranspiledOutput("loops/whileBreak.poc");
	}

}

