package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSortList extends BaseEParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceEOE("sortList/sortBooleans.pec");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceEOE("sortList/sortDateTimes.pec");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceEOE("sortList/sortDates.pec");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceEOE("sortList/sortDecimals.pec");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceEOE("sortList/sortDescBooleans.pec");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceEOE("sortList/sortDescDateTimes.pec");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceEOE("sortList/sortDescDates.pec");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceEOE("sortList/sortDescDecimals.pec");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceEOE("sortList/sortDescExpressions.pec");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceEOE("sortList/sortDescIntegers.pec");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceEOE("sortList/sortDescKeys.pec");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceEOE("sortList/sortDescMethods.pec");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceEOE("sortList/sortDescNames.pec");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceEOE("sortList/sortDescTexts.pec");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceEOE("sortList/sortDescTimes.pec");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceEOE("sortList/sortExpressions.pec");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceEOE("sortList/sortIntegers.pec");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceEOE("sortList/sortKeys.pec");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceEOE("sortList/sortMethods.pec");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceEOE("sortList/sortNames.pec");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceEOE("sortList/sortTexts.pec");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceEOE("sortList/sortTimes.pec");
	}

}

