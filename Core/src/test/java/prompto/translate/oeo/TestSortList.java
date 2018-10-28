package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSortList extends BaseOParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceOEO("sortList/sortBooleans.poc");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceOEO("sortList/sortDateTimes.poc");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceOEO("sortList/sortDates.poc");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceOEO("sortList/sortDecimals.poc");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceOEO("sortList/sortDescBooleans.poc");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceOEO("sortList/sortDescDateTimes.poc");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceOEO("sortList/sortDescDates.poc");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceOEO("sortList/sortDescDecimals.poc");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceOEO("sortList/sortDescExpressions.poc");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceOEO("sortList/sortDescIntegers.poc");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceOEO("sortList/sortDescKeys.poc");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceOEO("sortList/sortDescMethods.poc");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceOEO("sortList/sortDescNames.poc");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceOEO("sortList/sortDescTexts.poc");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceOEO("sortList/sortDescTimes.poc");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceOEO("sortList/sortExpressions.poc");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceOEO("sortList/sortIntegers.poc");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceOEO("sortList/sortKeys.poc");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceOEO("sortList/sortMethods.poc");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceOEO("sortList/sortNames.poc");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceOEO("sortList/sortTexts.poc");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceOEO("sortList/sortTimes.poc");
	}

}

