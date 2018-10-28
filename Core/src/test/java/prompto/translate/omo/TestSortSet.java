package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSortSet extends BaseOParserTest {

	@Test
	public void testSortBooleans() throws Exception {
		compareResourceOMO("sortSet/sortBooleans.poc");
	}

	@Test
	public void testSortDateTimes() throws Exception {
		compareResourceOMO("sortSet/sortDateTimes.poc");
	}

	@Test
	public void testSortDates() throws Exception {
		compareResourceOMO("sortSet/sortDates.poc");
	}

	@Test
	public void testSortDecimals() throws Exception {
		compareResourceOMO("sortSet/sortDecimals.poc");
	}

	@Test
	public void testSortDescBooleans() throws Exception {
		compareResourceOMO("sortSet/sortDescBooleans.poc");
	}

	@Test
	public void testSortDescDateTimes() throws Exception {
		compareResourceOMO("sortSet/sortDescDateTimes.poc");
	}

	@Test
	public void testSortDescDates() throws Exception {
		compareResourceOMO("sortSet/sortDescDates.poc");
	}

	@Test
	public void testSortDescDecimals() throws Exception {
		compareResourceOMO("sortSet/sortDescDecimals.poc");
	}

	@Test
	public void testSortDescExpressions() throws Exception {
		compareResourceOMO("sortSet/sortDescExpressions.poc");
	}

	@Test
	public void testSortDescIntegers() throws Exception {
		compareResourceOMO("sortSet/sortDescIntegers.poc");
	}

	@Test
	public void testSortDescKeys() throws Exception {
		compareResourceOMO("sortSet/sortDescKeys.poc");
	}

	@Test
	public void testSortDescMethods() throws Exception {
		compareResourceOMO("sortSet/sortDescMethods.poc");
	}

	@Test
	public void testSortDescNames() throws Exception {
		compareResourceOMO("sortSet/sortDescNames.poc");
	}

	@Test
	public void testSortDescTexts() throws Exception {
		compareResourceOMO("sortSet/sortDescTexts.poc");
	}

	@Test
	public void testSortDescTimes() throws Exception {
		compareResourceOMO("sortSet/sortDescTimes.poc");
	}

	@Test
	public void testSortExpressions() throws Exception {
		compareResourceOMO("sortSet/sortExpressions.poc");
	}

	@Test
	public void testSortIntegers() throws Exception {
		compareResourceOMO("sortSet/sortIntegers.poc");
	}

	@Test
	public void testSortKeys() throws Exception {
		compareResourceOMO("sortSet/sortKeys.poc");
	}

	@Test
	public void testSortMethods() throws Exception {
		compareResourceOMO("sortSet/sortMethods.poc");
	}

	@Test
	public void testSortNames() throws Exception {
		compareResourceOMO("sortSet/sortNames.poc");
	}

	@Test
	public void testSortTexts() throws Exception {
		compareResourceOMO("sortSet/sortTexts.poc");
	}

	@Test
	public void testSortTimes() throws Exception {
		compareResourceOMO("sortSet/sortTimes.poc");
	}

}

