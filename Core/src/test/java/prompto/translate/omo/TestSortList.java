package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSortList extends BaseOParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceOMO("sortList/sortBooleans.poc");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceOMO("sortList/sortDateTimes.poc");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceOMO("sortList/sortDates.poc");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceOMO("sortList/sortDecimals.poc");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceOMO("sortList/sortDescBooleans.poc");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceOMO("sortList/sortDescDateTimes.poc");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceOMO("sortList/sortDescDates.poc");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceOMO("sortList/sortDescDecimals.poc");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceOMO("sortList/sortDescExpressions.poc");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceOMO("sortList/sortDescIntegers.poc");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceOMO("sortList/sortDescKeys.poc");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceOMO("sortList/sortDescMethods.poc");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceOMO("sortList/sortDescNames.poc");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceOMO("sortList/sortDescTexts.poc");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceOMO("sortList/sortDescTimes.poc");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceOMO("sortList/sortExpressions.poc");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceOMO("sortList/sortIntegers.poc");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceOMO("sortList/sortKeys.poc");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceOMO("sortList/sortMethods.poc");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceOMO("sortList/sortNames.poc");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceOMO("sortList/sortTexts.poc");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceOMO("sortList/sortTimes.poc");
	}

}

