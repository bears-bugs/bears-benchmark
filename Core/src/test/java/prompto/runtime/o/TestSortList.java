package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestSortList extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedSortBooleans() throws Exception {
		checkInterpretedOutput("sortList/sortBooleans.poc");
	}

	@Test
	public void testCompiledSortBooleans() throws Exception {
		checkCompiledOutput("sortList/sortBooleans.poc");
	}

	@Test
	public void testTranspiledSortBooleans() throws Exception {
		checkTranspiledOutput("sortList/sortBooleans.poc");
	}

	@Test
	public void testInterpretedSortDateTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDateTimes.poc");
	}

	@Test
	public void testCompiledSortDateTimes() throws Exception {
		checkCompiledOutput("sortList/sortDateTimes.poc");
	}

	@Test
	public void testTranspiledSortDateTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDateTimes.poc");
	}

	@Test
	public void testInterpretedSortDates() throws Exception {
		checkInterpretedOutput("sortList/sortDates.poc");
	}

	@Test
	public void testCompiledSortDates() throws Exception {
		checkCompiledOutput("sortList/sortDates.poc");
	}

	@Test
	public void testTranspiledSortDates() throws Exception {
		checkTranspiledOutput("sortList/sortDates.poc");
	}

	@Test
	public void testInterpretedSortDecimals() throws Exception {
		checkInterpretedOutput("sortList/sortDecimals.poc");
	}

	@Test
	public void testCompiledSortDecimals() throws Exception {
		checkCompiledOutput("sortList/sortDecimals.poc");
	}

	@Test
	public void testTranspiledSortDecimals() throws Exception {
		checkTranspiledOutput("sortList/sortDecimals.poc");
	}

	@Test
	public void testInterpretedSortDescBooleans() throws Exception {
		checkInterpretedOutput("sortList/sortDescBooleans.poc");
	}

	@Test
	public void testCompiledSortDescBooleans() throws Exception {
		checkCompiledOutput("sortList/sortDescBooleans.poc");
	}

	@Test
	public void testTranspiledSortDescBooleans() throws Exception {
		checkTranspiledOutput("sortList/sortDescBooleans.poc");
	}

	@Test
	public void testInterpretedSortDescDateTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDescDateTimes.poc");
	}

	@Test
	public void testCompiledSortDescDateTimes() throws Exception {
		checkCompiledOutput("sortList/sortDescDateTimes.poc");
	}

	@Test
	public void testTranspiledSortDescDateTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDescDateTimes.poc");
	}

	@Test
	public void testInterpretedSortDescDates() throws Exception {
		checkInterpretedOutput("sortList/sortDescDates.poc");
	}

	@Test
	public void testCompiledSortDescDates() throws Exception {
		checkCompiledOutput("sortList/sortDescDates.poc");
	}

	@Test
	public void testTranspiledSortDescDates() throws Exception {
		checkTranspiledOutput("sortList/sortDescDates.poc");
	}

	@Test
	public void testInterpretedSortDescDecimals() throws Exception {
		checkInterpretedOutput("sortList/sortDescDecimals.poc");
	}

	@Test
	public void testCompiledSortDescDecimals() throws Exception {
		checkCompiledOutput("sortList/sortDescDecimals.poc");
	}

	@Test
	public void testTranspiledSortDescDecimals() throws Exception {
		checkTranspiledOutput("sortList/sortDescDecimals.poc");
	}

	@Test
	public void testInterpretedSortDescExpressions() throws Exception {
		checkInterpretedOutput("sortList/sortDescExpressions.poc");
	}

	@Test
	public void testCompiledSortDescExpressions() throws Exception {
		checkCompiledOutput("sortList/sortDescExpressions.poc");
	}

	@Test
	public void testTranspiledSortDescExpressions() throws Exception {
		checkTranspiledOutput("sortList/sortDescExpressions.poc");
	}

	@Test
	public void testInterpretedSortDescIntegers() throws Exception {
		checkInterpretedOutput("sortList/sortDescIntegers.poc");
	}

	@Test
	public void testCompiledSortDescIntegers() throws Exception {
		checkCompiledOutput("sortList/sortDescIntegers.poc");
	}

	@Test
	public void testTranspiledSortDescIntegers() throws Exception {
		checkTranspiledOutput("sortList/sortDescIntegers.poc");
	}

	@Test
	public void testInterpretedSortDescKeys() throws Exception {
		checkInterpretedOutput("sortList/sortDescKeys.poc");
	}

	@Test
	public void testCompiledSortDescKeys() throws Exception {
		checkCompiledOutput("sortList/sortDescKeys.poc");
	}

	@Test
	public void testTranspiledSortDescKeys() throws Exception {
		checkTranspiledOutput("sortList/sortDescKeys.poc");
	}

	@Test
	public void testInterpretedSortDescMethods() throws Exception {
		checkInterpretedOutput("sortList/sortDescMethods.poc");
	}

	@Test
	public void testCompiledSortDescMethods() throws Exception {
		checkCompiledOutput("sortList/sortDescMethods.poc");
	}

	@Test
	public void testTranspiledSortDescMethods() throws Exception {
		checkTranspiledOutput("sortList/sortDescMethods.poc");
	}

	@Test
	public void testInterpretedSortDescNames() throws Exception {
		checkInterpretedOutput("sortList/sortDescNames.poc");
	}

	@Test
	public void testCompiledSortDescNames() throws Exception {
		checkCompiledOutput("sortList/sortDescNames.poc");
	}

	@Test
	public void testTranspiledSortDescNames() throws Exception {
		checkTranspiledOutput("sortList/sortDescNames.poc");
	}

	@Test
	public void testInterpretedSortDescTexts() throws Exception {
		checkInterpretedOutput("sortList/sortDescTexts.poc");
	}

	@Test
	public void testCompiledSortDescTexts() throws Exception {
		checkCompiledOutput("sortList/sortDescTexts.poc");
	}

	@Test
	public void testTranspiledSortDescTexts() throws Exception {
		checkTranspiledOutput("sortList/sortDescTexts.poc");
	}

	@Test
	public void testInterpretedSortDescTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDescTimes.poc");
	}

	@Test
	public void testCompiledSortDescTimes() throws Exception {
		checkCompiledOutput("sortList/sortDescTimes.poc");
	}

	@Test
	public void testTranspiledSortDescTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDescTimes.poc");
	}

	@Test
	public void testInterpretedSortExpressions() throws Exception {
		checkInterpretedOutput("sortList/sortExpressions.poc");
	}

	@Test
	public void testCompiledSortExpressions() throws Exception {
		checkCompiledOutput("sortList/sortExpressions.poc");
	}

	@Test
	public void testTranspiledSortExpressions() throws Exception {
		checkTranspiledOutput("sortList/sortExpressions.poc");
	}

	@Test
	public void testInterpretedSortIntegers() throws Exception {
		checkInterpretedOutput("sortList/sortIntegers.poc");
	}

	@Test
	public void testCompiledSortIntegers() throws Exception {
		checkCompiledOutput("sortList/sortIntegers.poc");
	}

	@Test
	public void testTranspiledSortIntegers() throws Exception {
		checkTranspiledOutput("sortList/sortIntegers.poc");
	}

	@Test
	public void testInterpretedSortKeys() throws Exception {
		checkInterpretedOutput("sortList/sortKeys.poc");
	}

	@Test
	public void testCompiledSortKeys() throws Exception {
		checkCompiledOutput("sortList/sortKeys.poc");
	}

	@Test
	public void testTranspiledSortKeys() throws Exception {
		checkTranspiledOutput("sortList/sortKeys.poc");
	}

	@Test
	public void testInterpretedSortMethods() throws Exception {
		checkInterpretedOutput("sortList/sortMethods.poc");
	}

	@Test
	public void testCompiledSortMethods() throws Exception {
		checkCompiledOutput("sortList/sortMethods.poc");
	}

	@Test
	public void testTranspiledSortMethods() throws Exception {
		checkTranspiledOutput("sortList/sortMethods.poc");
	}

	@Test
	public void testInterpretedSortNames() throws Exception {
		checkInterpretedOutput("sortList/sortNames.poc");
	}

	@Test
	public void testCompiledSortNames() throws Exception {
		checkCompiledOutput("sortList/sortNames.poc");
	}

	@Test
	public void testTranspiledSortNames() throws Exception {
		checkTranspiledOutput("sortList/sortNames.poc");
	}

	@Test
	public void testInterpretedSortTexts() throws Exception {
		checkInterpretedOutput("sortList/sortTexts.poc");
	}

	@Test
	public void testCompiledSortTexts() throws Exception {
		checkCompiledOutput("sortList/sortTexts.poc");
	}

	@Test
	public void testTranspiledSortTexts() throws Exception {
		checkTranspiledOutput("sortList/sortTexts.poc");
	}

	@Test
	public void testInterpretedSortTimes() throws Exception {
		checkInterpretedOutput("sortList/sortTimes.poc");
	}

	@Test
	public void testCompiledSortTimes() throws Exception {
		checkCompiledOutput("sortList/sortTimes.poc");
	}

	@Test
	public void testTranspiledSortTimes() throws Exception {
		checkTranspiledOutput("sortList/sortTimes.poc");
	}

}

