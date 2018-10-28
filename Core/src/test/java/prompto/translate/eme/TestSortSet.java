package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSortSet extends BaseEParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceEME("sortSet/sortBooleans.pec");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceEME("sortSet/sortDateTimes.pec");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceEME("sortSet/sortDates.pec");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceEME("sortSet/sortDecimals.pec");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceEME("sortSet/sortDescBooleans.pec");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceEME("sortSet/sortDescDateTimes.pec");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceEME("sortSet/sortDescDates.pec");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceEME("sortSet/sortDescDecimals.pec");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceEME("sortSet/sortDescExpressions.pec");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceEME("sortSet/sortDescIntegers.pec");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceEME("sortSet/sortDescKeys.pec");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceEME("sortSet/sortDescMethods.pec");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceEME("sortSet/sortDescNames.pec");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceEME("sortSet/sortDescTexts.pec");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceEME("sortSet/sortDescTimes.pec");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceEME("sortSet/sortExpressions.pec");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceEME("sortSet/sortIntegers.pec");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceEME("sortSet/sortKeys.pec");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceEME("sortSet/sortMethods.pec");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceEME("sortSet/sortNames.pec");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceEME("sortSet/sortTexts.pec");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceEME("sortSet/sortTimes.pec");
	}

}

