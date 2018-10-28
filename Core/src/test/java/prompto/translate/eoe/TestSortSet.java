package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSortSet extends BaseEParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceEOE("sortSet/sortBooleans.pec");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceEOE("sortSet/sortDateTimes.pec");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceEOE("sortSet/sortDates.pec");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceEOE("sortSet/sortDecimals.pec");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceEOE("sortSet/sortDescBooleans.pec");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceEOE("sortSet/sortDescDateTimes.pec");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceEOE("sortSet/sortDescDates.pec");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceEOE("sortSet/sortDescDecimals.pec");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceEOE("sortSet/sortDescExpressions.pec");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceEOE("sortSet/sortDescIntegers.pec");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceEOE("sortSet/sortDescKeys.pec");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceEOE("sortSet/sortDescMethods.pec");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceEOE("sortSet/sortDescNames.pec");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceEOE("sortSet/sortDescTexts.pec");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceEOE("sortSet/sortDescTimes.pec");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceEOE("sortSet/sortExpressions.pec");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceEOE("sortSet/sortIntegers.pec");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceEOE("sortSet/sortKeys.pec");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceEOE("sortSet/sortMethods.pec");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceEOE("sortSet/sortNames.pec");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceEOE("sortSet/sortTexts.pec");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceEOE("sortSet/sortTimes.pec");
	}

}

