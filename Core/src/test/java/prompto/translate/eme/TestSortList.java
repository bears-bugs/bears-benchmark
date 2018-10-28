package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSortList extends BaseEParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceEME("sortList/sortBooleans.pec");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceEME("sortList/sortDateTimes.pec");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceEME("sortList/sortDates.pec");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceEME("sortList/sortDecimals.pec");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceEME("sortList/sortDescBooleans.pec");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceEME("sortList/sortDescDateTimes.pec");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceEME("sortList/sortDescDates.pec");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceEME("sortList/sortDescDecimals.pec");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceEME("sortList/sortDescExpressions.pec");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceEME("sortList/sortDescIntegers.pec");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceEME("sortList/sortDescKeys.pec");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceEME("sortList/sortDescMethods.pec");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceEME("sortList/sortDescNames.pec");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceEME("sortList/sortDescTexts.pec");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceEME("sortList/sortDescTimes.pec");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceEME("sortList/sortExpressions.pec");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceEME("sortList/sortIntegers.pec");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceEME("sortList/sortKeys.pec");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceEME("sortList/sortMethods.pec");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceEME("sortList/sortNames.pec");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceEME("sortList/sortTexts.pec");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceEME("sortList/sortTimes.pec");
	}

}

