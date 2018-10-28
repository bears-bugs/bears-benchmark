package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestSortList extends BaseEParserTest {

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
		checkInterpretedOutput("sortList/sortBooleans.pec");
	}

	@Test
	public void testCompiledSortBooleans() throws Exception {
		checkCompiledOutput("sortList/sortBooleans.pec");
	}

	@Test
	public void testTranspiledSortBooleans() throws Exception {
		checkTranspiledOutput("sortList/sortBooleans.pec");
	}

	@Test
	public void testInterpretedSortDateTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDateTimes.pec");
	}

	@Test
	public void testCompiledSortDateTimes() throws Exception {
		checkCompiledOutput("sortList/sortDateTimes.pec");
	}

	@Test
	public void testTranspiledSortDateTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDateTimes.pec");
	}

	@Test
	public void testInterpretedSortDates() throws Exception {
		checkInterpretedOutput("sortList/sortDates.pec");
	}

	@Test
	public void testCompiledSortDates() throws Exception {
		checkCompiledOutput("sortList/sortDates.pec");
	}

	@Test
	public void testTranspiledSortDates() throws Exception {
		checkTranspiledOutput("sortList/sortDates.pec");
	}

	@Test
	public void testInterpretedSortDecimals() throws Exception {
		checkInterpretedOutput("sortList/sortDecimals.pec");
	}

	@Test
	public void testCompiledSortDecimals() throws Exception {
		checkCompiledOutput("sortList/sortDecimals.pec");
	}

	@Test
	public void testTranspiledSortDecimals() throws Exception {
		checkTranspiledOutput("sortList/sortDecimals.pec");
	}

	@Test
	public void testInterpretedSortDescBooleans() throws Exception {
		checkInterpretedOutput("sortList/sortDescBooleans.pec");
	}

	@Test
	public void testCompiledSortDescBooleans() throws Exception {
		checkCompiledOutput("sortList/sortDescBooleans.pec");
	}

	@Test
	public void testTranspiledSortDescBooleans() throws Exception {
		checkTranspiledOutput("sortList/sortDescBooleans.pec");
	}

	@Test
	public void testInterpretedSortDescDateTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDescDateTimes.pec");
	}

	@Test
	public void testCompiledSortDescDateTimes() throws Exception {
		checkCompiledOutput("sortList/sortDescDateTimes.pec");
	}

	@Test
	public void testTranspiledSortDescDateTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDescDateTimes.pec");
	}

	@Test
	public void testInterpretedSortDescDates() throws Exception {
		checkInterpretedOutput("sortList/sortDescDates.pec");
	}

	@Test
	public void testCompiledSortDescDates() throws Exception {
		checkCompiledOutput("sortList/sortDescDates.pec");
	}

	@Test
	public void testTranspiledSortDescDates() throws Exception {
		checkTranspiledOutput("sortList/sortDescDates.pec");
	}

	@Test
	public void testInterpretedSortDescDecimals() throws Exception {
		checkInterpretedOutput("sortList/sortDescDecimals.pec");
	}

	@Test
	public void testCompiledSortDescDecimals() throws Exception {
		checkCompiledOutput("sortList/sortDescDecimals.pec");
	}

	@Test
	public void testTranspiledSortDescDecimals() throws Exception {
		checkTranspiledOutput("sortList/sortDescDecimals.pec");
	}

	@Test
	public void testInterpretedSortDescExpressions() throws Exception {
		checkInterpretedOutput("sortList/sortDescExpressions.pec");
	}

	@Test
	public void testCompiledSortDescExpressions() throws Exception {
		checkCompiledOutput("sortList/sortDescExpressions.pec");
	}

	@Test
	public void testTranspiledSortDescExpressions() throws Exception {
		checkTranspiledOutput("sortList/sortDescExpressions.pec");
	}

	@Test
	public void testInterpretedSortDescIntegers() throws Exception {
		checkInterpretedOutput("sortList/sortDescIntegers.pec");
	}

	@Test
	public void testCompiledSortDescIntegers() throws Exception {
		checkCompiledOutput("sortList/sortDescIntegers.pec");
	}

	@Test
	public void testTranspiledSortDescIntegers() throws Exception {
		checkTranspiledOutput("sortList/sortDescIntegers.pec");
	}

	@Test
	public void testInterpretedSortDescKeys() throws Exception {
		checkInterpretedOutput("sortList/sortDescKeys.pec");
	}

	@Test
	public void testCompiledSortDescKeys() throws Exception {
		checkCompiledOutput("sortList/sortDescKeys.pec");
	}

	@Test
	public void testTranspiledSortDescKeys() throws Exception {
		checkTranspiledOutput("sortList/sortDescKeys.pec");
	}

	@Test
	public void testInterpretedSortDescMethods() throws Exception {
		checkInterpretedOutput("sortList/sortDescMethods.pec");
	}

	@Test
	public void testCompiledSortDescMethods() throws Exception {
		checkCompiledOutput("sortList/sortDescMethods.pec");
	}

	@Test
	public void testTranspiledSortDescMethods() throws Exception {
		checkTranspiledOutput("sortList/sortDescMethods.pec");
	}

	@Test
	public void testInterpretedSortDescNames() throws Exception {
		checkInterpretedOutput("sortList/sortDescNames.pec");
	}

	@Test
	public void testCompiledSortDescNames() throws Exception {
		checkCompiledOutput("sortList/sortDescNames.pec");
	}

	@Test
	public void testTranspiledSortDescNames() throws Exception {
		checkTranspiledOutput("sortList/sortDescNames.pec");
	}

	@Test
	public void testInterpretedSortDescTexts() throws Exception {
		checkInterpretedOutput("sortList/sortDescTexts.pec");
	}

	@Test
	public void testCompiledSortDescTexts() throws Exception {
		checkCompiledOutput("sortList/sortDescTexts.pec");
	}

	@Test
	public void testTranspiledSortDescTexts() throws Exception {
		checkTranspiledOutput("sortList/sortDescTexts.pec");
	}

	@Test
	public void testInterpretedSortDescTimes() throws Exception {
		checkInterpretedOutput("sortList/sortDescTimes.pec");
	}

	@Test
	public void testCompiledSortDescTimes() throws Exception {
		checkCompiledOutput("sortList/sortDescTimes.pec");
	}

	@Test
	public void testTranspiledSortDescTimes() throws Exception {
		checkTranspiledOutput("sortList/sortDescTimes.pec");
	}

	@Test
	public void testInterpretedSortExpressions() throws Exception {
		checkInterpretedOutput("sortList/sortExpressions.pec");
	}

	@Test
	public void testCompiledSortExpressions() throws Exception {
		checkCompiledOutput("sortList/sortExpressions.pec");
	}

	@Test
	public void testTranspiledSortExpressions() throws Exception {
		checkTranspiledOutput("sortList/sortExpressions.pec");
	}

	@Test
	public void testInterpretedSortIntegers() throws Exception {
		checkInterpretedOutput("sortList/sortIntegers.pec");
	}

	@Test
	public void testCompiledSortIntegers() throws Exception {
		checkCompiledOutput("sortList/sortIntegers.pec");
	}

	@Test
	public void testTranspiledSortIntegers() throws Exception {
		checkTranspiledOutput("sortList/sortIntegers.pec");
	}

	@Test
	public void testInterpretedSortKeys() throws Exception {
		checkInterpretedOutput("sortList/sortKeys.pec");
	}

	@Test
	public void testCompiledSortKeys() throws Exception {
		checkCompiledOutput("sortList/sortKeys.pec");
	}

	@Test
	public void testTranspiledSortKeys() throws Exception {
		checkTranspiledOutput("sortList/sortKeys.pec");
	}

	@Test
	public void testInterpretedSortMethods() throws Exception {
		checkInterpretedOutput("sortList/sortMethods.pec");
	}

	@Test
	public void testCompiledSortMethods() throws Exception {
		checkCompiledOutput("sortList/sortMethods.pec");
	}

	@Test
	public void testTranspiledSortMethods() throws Exception {
		checkTranspiledOutput("sortList/sortMethods.pec");
	}

	@Test
	public void testInterpretedSortNames() throws Exception {
		checkInterpretedOutput("sortList/sortNames.pec");
	}

	@Test
	public void testCompiledSortNames() throws Exception {
		checkCompiledOutput("sortList/sortNames.pec");
	}

	@Test
	public void testTranspiledSortNames() throws Exception {
		checkTranspiledOutput("sortList/sortNames.pec");
	}

	@Test
	public void testInterpretedSortTexts() throws Exception {
		checkInterpretedOutput("sortList/sortTexts.pec");
	}

	@Test
	public void testCompiledSortTexts() throws Exception {
		checkCompiledOutput("sortList/sortTexts.pec");
	}

	@Test
	public void testTranspiledSortTexts() throws Exception {
		checkTranspiledOutput("sortList/sortTexts.pec");
	}

	@Test
	public void testInterpretedSortTimes() throws Exception {
		checkInterpretedOutput("sortList/sortTimes.pec");
	}

	@Test
	public void testCompiledSortTimes() throws Exception {
		checkCompiledOutput("sortList/sortTimes.pec");
	}

	@Test
	public void testTranspiledSortTimes() throws Exception {
		checkTranspiledOutput("sortList/sortTimes.pec");
	}

}

