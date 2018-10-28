package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSortSet extends BaseOParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceOEO("sortSet/sortBooleans.poc");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceOEO("sortSet/sortDateTimes.poc");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceOEO("sortSet/sortDates.poc");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceOEO("sortSet/sortDecimals.poc");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceOEO("sortSet/sortDescBooleans.poc");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceOEO("sortSet/sortDescDateTimes.poc");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceOEO("sortSet/sortDescDates.poc");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceOEO("sortSet/sortDescDecimals.poc");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceOEO("sortSet/sortDescExpressions.poc");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceOEO("sortSet/sortDescIntegers.poc");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceOEO("sortSet/sortDescKeys.poc");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceOEO("sortSet/sortDescMethods.poc");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceOEO("sortSet/sortDescNames.poc");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceOEO("sortSet/sortDescTexts.poc");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceOEO("sortSet/sortDescTimes.poc");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceOEO("sortSet/sortExpressions.poc");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceOEO("sortSet/sortIntegers.poc");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceOEO("sortSet/sortKeys.poc");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceOEO("sortSet/sortMethods.poc");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceOEO("sortSet/sortNames.poc");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceOEO("sortSet/sortTexts.poc");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceOEO("sortSet/sortTimes.poc");
	}

}

