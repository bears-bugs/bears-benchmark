package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestSortSet extends BaseOParserTest {

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
		checkInterpretedOutput("sortSet/sortBooleans.poc");
	}

	@Test
	public void testCompiledSortBooleans() throws Exception {
		checkCompiledOutput("sortSet/sortBooleans.poc");
	}

	@Test
	public void testTranspiledSortBooleans() throws Exception {
		checkTranspiledOutput("sortSet/sortBooleans.poc");
	}

	@Test
	public void testInterpretedSortDateTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDateTimes.poc");
	}

	@Test
	public void testCompiledSortDateTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDateTimes.poc");
	}

	@Test
	public void testTranspiledSortDateTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDateTimes.poc");
	}

	@Test
	public void testInterpretedSortDates() throws Exception {
		checkInterpretedOutput("sortSet/sortDates.poc");
	}

	@Test
	public void testCompiledSortDates() throws Exception {
		checkCompiledOutput("sortSet/sortDates.poc");
	}

	@Test
	public void testTranspiledSortDates() throws Exception {
		checkTranspiledOutput("sortSet/sortDates.poc");
	}

	@Test
	public void testInterpretedSortDecimals() throws Exception {
		checkInterpretedOutput("sortSet/sortDecimals.poc");
	}

	@Test
	public void testCompiledSortDecimals() throws Exception {
		checkCompiledOutput("sortSet/sortDecimals.poc");
	}

	@Test
	public void testTranspiledSortDecimals() throws Exception {
		checkTranspiledOutput("sortSet/sortDecimals.poc");
	}

	@Test
	public void testInterpretedSortDescBooleans() throws Exception {
		checkInterpretedOutput("sortSet/sortDescBooleans.poc");
	}

	@Test
	public void testCompiledSortDescBooleans() throws Exception {
		checkCompiledOutput("sortSet/sortDescBooleans.poc");
	}

	@Test
	public void testTranspiledSortDescBooleans() throws Exception {
		checkTranspiledOutput("sortSet/sortDescBooleans.poc");
	}

	@Test
	public void testInterpretedSortDescDateTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDateTimes.poc");
	}

	@Test
	public void testCompiledSortDescDateTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDescDateTimes.poc");
	}

	@Test
	public void testTranspiledSortDescDateTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDateTimes.poc");
	}

	@Test
	public void testInterpretedSortDescDates() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDates.poc");
	}

	@Test
	public void testCompiledSortDescDates() throws Exception {
		checkCompiledOutput("sortSet/sortDescDates.poc");
	}

	@Test
	public void testTranspiledSortDescDates() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDates.poc");
	}

	@Test
	public void testInterpretedSortDescDecimals() throws Exception {
		checkInterpretedOutput("sortSet/sortDescDecimals.poc");
	}

	@Test
	public void testCompiledSortDescDecimals() throws Exception {
		checkCompiledOutput("sortSet/sortDescDecimals.poc");
	}

	@Test
	public void testTranspiledSortDescDecimals() throws Exception {
		checkTranspiledOutput("sortSet/sortDescDecimals.poc");
	}

	@Test
	public void testInterpretedSortDescExpressions() throws Exception {
		checkInterpretedOutput("sortSet/sortDescExpressions.poc");
	}

	@Test
	public void testCompiledSortDescExpressions() throws Exception {
		checkCompiledOutput("sortSet/sortDescExpressions.poc");
	}

	@Test
	public void testTranspiledSortDescExpressions() throws Exception {
		checkTranspiledOutput("sortSet/sortDescExpressions.poc");
	}

	@Test
	public void testInterpretedSortDescIntegers() throws Exception {
		checkInterpretedOutput("sortSet/sortDescIntegers.poc");
	}

	@Test
	public void testCompiledSortDescIntegers() throws Exception {
		checkCompiledOutput("sortSet/sortDescIntegers.poc");
	}

	@Test
	public void testTranspiledSortDescIntegers() throws Exception {
		checkTranspiledOutput("sortSet/sortDescIntegers.poc");
	}

	@Test
	public void testInterpretedSortDescKeys() throws Exception {
		checkInterpretedOutput("sortSet/sortDescKeys.poc");
	}

	@Test
	public void testCompiledSortDescKeys() throws Exception {
		checkCompiledOutput("sortSet/sortDescKeys.poc");
	}

	@Test
	public void testTranspiledSortDescKeys() throws Exception {
		checkTranspiledOutput("sortSet/sortDescKeys.poc");
	}

	@Test
	public void testInterpretedSortDescMethods() throws Exception {
		checkInterpretedOutput("sortSet/sortDescMethods.poc");
	}

	@Test
	public void testCompiledSortDescMethods() throws Exception {
		checkCompiledOutput("sortSet/sortDescMethods.poc");
	}

	@Test
	public void testTranspiledSortDescMethods() throws Exception {
		checkTranspiledOutput("sortSet/sortDescMethods.poc");
	}

	@Test
	public void testInterpretedSortDescNames() throws Exception {
		checkInterpretedOutput("sortSet/sortDescNames.poc");
	}

	@Test
	public void testCompiledSortDescNames() throws Exception {
		checkCompiledOutput("sortSet/sortDescNames.poc");
	}

	@Test
	public void testTranspiledSortDescNames() throws Exception {
		checkTranspiledOutput("sortSet/sortDescNames.poc");
	}

	@Test
	public void testInterpretedSortDescTexts() throws Exception {
		checkInterpretedOutput("sortSet/sortDescTexts.poc");
	}

	@Test
	public void testCompiledSortDescTexts() throws Exception {
		checkCompiledOutput("sortSet/sortDescTexts.poc");
	}

	@Test
	public void testTranspiledSortDescTexts() throws Exception {
		checkTranspiledOutput("sortSet/sortDescTexts.poc");
	}

	@Test
	public void testInterpretedSortDescTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortDescTimes.poc");
	}

	@Test
	public void testCompiledSortDescTimes() throws Exception {
		checkCompiledOutput("sortSet/sortDescTimes.poc");
	}

	@Test
	public void testTranspiledSortDescTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortDescTimes.poc");
	}

	@Test
	public void testInterpretedSortExpressions() throws Exception {
		checkInterpretedOutput("sortSet/sortExpressions.poc");
	}

	@Test
	public void testCompiledSortExpressions() throws Exception {
		checkCompiledOutput("sortSet/sortExpressions.poc");
	}

	@Test
	public void testTranspiledSortExpressions() throws Exception {
		checkTranspiledOutput("sortSet/sortExpressions.poc");
	}

	@Test
	public void testInterpretedSortIntegers() throws Exception {
		checkInterpretedOutput("sortSet/sortIntegers.poc");
	}

	@Test
	public void testCompiledSortIntegers() throws Exception {
		checkCompiledOutput("sortSet/sortIntegers.poc");
	}

	@Test
	public void testTranspiledSortIntegers() throws Exception {
		checkTranspiledOutput("sortSet/sortIntegers.poc");
	}

	@Test
	public void testInterpretedSortKeys() throws Exception {
		checkInterpretedOutput("sortSet/sortKeys.poc");
	}

	@Test
	public void testCompiledSortKeys() throws Exception {
		checkCompiledOutput("sortSet/sortKeys.poc");
	}

	@Test
	public void testTranspiledSortKeys() throws Exception {
		checkTranspiledOutput("sortSet/sortKeys.poc");
	}

	@Test
	public void testInterpretedSortMethods() throws Exception {
		checkInterpretedOutput("sortSet/sortMethods.poc");
	}

	@Test
	public void testCompiledSortMethods() throws Exception {
		checkCompiledOutput("sortSet/sortMethods.poc");
	}

	@Test
	public void testTranspiledSortMethods() throws Exception {
		checkTranspiledOutput("sortSet/sortMethods.poc");
	}

	@Test
	public void testInterpretedSortNames() throws Exception {
		checkInterpretedOutput("sortSet/sortNames.poc");
	}

	@Test
	public void testCompiledSortNames() throws Exception {
		checkCompiledOutput("sortSet/sortNames.poc");
	}

	@Test
	public void testTranspiledSortNames() throws Exception {
		checkTranspiledOutput("sortSet/sortNames.poc");
	}

	@Test
	public void testInterpretedSortTexts() throws Exception {
		checkInterpretedOutput("sortSet/sortTexts.poc");
	}

	@Test
	public void testCompiledSortTexts() throws Exception {
		checkCompiledOutput("sortSet/sortTexts.poc");
	}

	@Test
	public void testTranspiledSortTexts() throws Exception {
		checkTranspiledOutput("sortSet/sortTexts.poc");
	}

	@Test
	public void testInterpretedSortTimes() throws Exception {
		checkInterpretedOutput("sortSet/sortTimes.poc");
	}

	@Test
	public void testCompiledSortTimes() throws Exception {
		checkCompiledOutput("sortSet/sortTimes.poc");
	}

	@Test
	public void testTranspiledSortTimes() throws Exception {
		checkTranspiledOutput("sortSet/sortTimes.poc");
	}

}

