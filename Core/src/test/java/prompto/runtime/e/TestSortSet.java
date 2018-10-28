package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestSortSet extends BaseEParserTest {

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
		checkInterpretedOutput("sortSet/sortBooleans.pec");
	}

	@Test
	public void testCompiledSortBooleans() throws Exception {
		checkCompiledOutput("sortSet/sortBooleans.pec");
	}

	@Test
	public void testTranspiledSortBooleans() throws Exception {
		checkTranspiledOutput("sortSet/sortBooleans.pec");
	}

	@Test
	public void testInterpretedSortDateTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDateTimes.pec");
	}

	@Test
	public void testCompiledSortDateTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDateTimes.pec");
	}

	@Test
	public void testTranspiledSortDateTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDateTimes.pec");
	}

	@Test
	public void testInterpretedSortDates() throws Exception {
		checkInterpretedOutput("sortSet/sortDates.pec");
	}

	@Test
	public void testCompiledSortDates() throws Exception {
		checkCompiledOutput("sortSet/sortDates.pec");
	}

	@Test
	public void testTranspiledSortDates() throws Exception {
		checkTranspiledOutput("sortSet/sortDates.pec");
	}

	@Test
	public void testInterpretedSortDecimals() throws Exception {
		checkInterpretedOutput("sortSet/sortDecimals.pec");
	}

	@Test
	public void testCompiledSortDecimals() throws Exception {
		checkCompiledOutput("sortSet/sortDecimals.pec");
	}

	@Test
	public void testTranspiledSortDecimals() throws Exception {
		checkTranspiledOutput("sortSet/sortDecimals.pec");
	}

	@Test
	public void testInterpretedSortDescBooleans() throws Exception {
		checkInterpretedOutput("sortSet/sortDescBooleans.pec");
	}

	@Test
	public void testCompiledSortDescBooleans() throws Exception {
		checkCompiledOutput("sortSet/sortDescBooleans.pec");
	}

	@Test
	public void testTranspiledSortDescBooleans() throws Exception {
		checkTranspiledOutput("sortSet/sortDescBooleans.pec");
	}

	@Test
	public void testInterpretedSortDescDateTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDateTimes.pec");
	}

	@Test
	public void testCompiledSortDescDateTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDescDateTimes.pec");
	}

	@Test
	public void testTranspiledSortDescDateTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDateTimes.pec");
	}

	@Test
	public void testInterpretedSortDescDates() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDates.pec");
	}

	@Test
	public void testCompiledSortDescDates() throws Exception {
		checkCompiledOutput("sortSet/sortDescDates.pec");
	}

	@Test
	public void testTranspiledSortDescDates() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDates.pec");
	}

	@Test
	public void testInterpretedSortDescDecimals() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDecimals.pec");
	}

	@Test
	public void testCompiledSortDescDecimals() throws Exception {
		checkCompiledOutput("sortSet/sortDescDecimals.pec");
	}

	@Test
	public void testTranspiledSortDescDecimals() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDecimals.pec");
	}

	@Test
	public void testInterpretedSortDescExpressions() throws Exception {
		checkInterpretedOutput("sortSet/sortDescExpressions.pec");
	}

	@Test
	public void testCompiledSortDescExpressions() throws Exception {
		checkCompiledOutput("sortSet/sortDescExpressions.pec");
	}

	@Test
	public void testTranspiledSortDescExpressions() throws Exception {
		checkTranspiledOutput("sortSet/sortDescExpressions.pec");
	}

	@Test
	public void testInterpretedSortDescIntegers() throws Exception {
		checkInterpretedOutput("sortSet/sortDescIntegers.pec");
	}

	@Test
	public void testCompiledSortDescIntegers() throws Exception {
		checkCompiledOutput("sortSet/sortDescIntegers.pec");
	}

	@Test
	public void testTranspiledSortDescIntegers() throws Exception {
		checkTranspiledOutput("sortSet/sortDescIntegers.pec");
	}

	@Test
	public void testInterpretedSortDescKeys() throws Exception {
		checkInterpretedOutput("sortSet/sortDescKeys.pec");
	}

	@Test
	public void testCompiledSortDescKeys() throws Exception {
		checkCompiledOutput("sortSet/sortDescKeys.pec");
	}

	@Test
	public void testTranspiledSortDescKeys() throws Exception {
		checkTranspiledOutput("sortSet/sortDescKeys.pec");
	}

	@Test
	public void testInterpretedSortDescMethods() throws Exception {
		checkInterpretedOutput("sortSet/sortDescMethods.pec");
	}

	@Test
	public void testCompiledSortDescMethods() throws Exception {
		checkCompiledOutput("sortSet/sortDescMethods.pec");
	}

	@Test
	public void testTranspiledSortDescMethods() throws Exception {
		checkTranspiledOutput("sortSet/sortDescMethods.pec");
	}

	@Test
	public void testInterpretedSortDescNames() throws Exception {
		checkInterpretedOutput("sortSet/sortDescNames.pec");
	}

	@Test
	public void testCompiledSortDescNames() throws Exception {
		checkCompiledOutput("sortSet/sortDescNames.pec");
	}

	@Test
	public void testTranspiledSortDescNames() throws Exception {
		checkTranspiledOutput("sortSet/sortDescNames.pec");
	}

	@Test
	public void testInterpretedSortDescTexts() throws Exception {
		checkInterpretedOutput("sortSet/sortDescTexts.pec");
	}

	@Test
	public void testCompiledSortDescTexts() throws Exception {
		checkCompiledOutput("sortSet/sortDescTexts.pec");
	}

	@Test
	public void testTranspiledSortDescTexts() throws Exception {
		checkTranspiledOutput("sortSet/sortDescTexts.pec");
	}

	@Test
	public void testInterpretedSortDescTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDescTimes.pec");
	}

	@Test
	public void testCompiledSortDescTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDescTimes.pec");
	}

	@Test
	public void testTranspiledSortDescTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDescTimes.pec");
	}

	@Test
	public void testInterpretedSortExpressions() throws Exception {
		checkInterpretedOutput("sortSet/sortExpressions.pec");
	}

	@Test
	public void testCompiledSortExpressions() throws Exception {
		checkCompiledOutput("sortSet/sortExpressions.pec");
	}

	@Test
	public void testTranspiledSortExpressions() throws Exception {
		checkTranspiledOutput("sortSet/sortExpressions.pec");
	}

	@Test
	public void testInterpretedSortIntegers() throws Exception {
		checkInterpretedOutput("sortSet/sortIntegers.pec");
	}

	@Test
	public void testCompiledSortIntegers() throws Exception {
		checkCompiledOutput("sortSet/sortIntegers.pec");
	}

	@Test
	public void testTranspiledSortIntegers() throws Exception {
		checkTranspiledOutput("sortSet/sortIntegers.pec");
	}

	@Test
	public void testInterpretedSortKeys() throws Exception {
		checkInterpretedOutput("sortSet/sortKeys.pec");
	}

	@Test
	public void testCompiledSortKeys() throws Exception {
		checkCompiledOutput("sortSet/sortKeys.pec");
	}

	@Test
	public void testTranspiledSortKeys() throws Exception {
		checkTranspiledOutput("sortSet/sortKeys.pec");
	}

	@Test
	public void testInterpretedSortMethods() throws Exception {
		checkInterpretedOutput("sortSet/sortMethods.pec");
	}

	@Test
	public void testCompiledSortMethods() throws Exception {
		checkCompiledOutput("sortSet/sortMethods.pec");
	}

	@Test
	public void testTranspiledSortMethods() throws Exception {
		checkTranspiledOutput("sortSet/sortMethods.pec");
	}

	@Test
	public void testInterpretedSortNames() throws Exception {
		checkInterpretedOutput("sortSet/sortNames.pec");
	}

	@Test
	public void testCompiledSortNames() throws Exception {
		checkCompiledOutput("sortSet/sortNames.pec");
	}

	@Test
	public void testTranspiledSortNames() throws Exception {
		checkTranspiledOutput("sortSet/sortNames.pec");
	}

	@Test
	public void testInterpretedSortTexts() throws Exception {
		checkInterpretedOutput("sortSet/sortTexts.pec");
	}

	@Test
	public void testCompiledSortTexts() throws Exception {
		checkCompiledOutput("sortSet/sortTexts.pec");
	}

	@Test
	public void testTranspiledSortTexts() throws Exception {
		checkTranspiledOutput("sortSet/sortTexts.pec");
	}

	@Test
	public void testInterpretedSortTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortTimes.pec");
	}

	@Test
	public void testCompiledSortTimes() throws Exception {
		checkCompiledOutput("sortSet/sortTimes.pec");
	}

	@Test
	public void testTranspiledSortTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortTimes.pec");
	}

}

