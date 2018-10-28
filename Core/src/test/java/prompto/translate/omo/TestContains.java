package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestContains extends BaseOParserTest {

	@Test
	public void testContainsAllList() throws Exception {
		compareResourceOMO("contains/containsAllList.poc");
	}

	@Test
	public void testContainsAllSet() throws Exception {
		compareResourceOMO("contains/containsAllSet.poc");
	}

	@Test
	public void testContainsAllText() throws Exception {
		compareResourceOMO("contains/containsAllText.poc");
	}

	@Test
	public void testContainsAllTuple() throws Exception {
		compareResourceOMO("contains/containsAllTuple.poc");
	}

	@Test
	public void testContainsAnyList() throws Exception {
		compareResourceOMO("contains/containsAnyList.poc");
	}

	@Test
	public void testContainsAnySet() throws Exception {
		compareResourceOMO("contains/containsAnySet.poc");
	}

	@Test
	public void testContainsAnyText() throws Exception {
		compareResourceOMO("contains/containsAnyText.poc");
	}

	@Test
	public void testContainsAnyTuple() throws Exception {
		compareResourceOMO("contains/containsAnyTuple.poc");
	}

	@Test
	public void testInCharacterRange() throws Exception {
		compareResourceOMO("contains/inCharacterRange.poc");
	}

	@Test
	public void testInDateRange() throws Exception {
		compareResourceOMO("contains/inDateRange.poc");
	}

	@Test
	public void testInDict() throws Exception {
		compareResourceOMO("contains/inDict.poc");
	}

	@Test
	public void testInIntegerRange() throws Exception {
		compareResourceOMO("contains/inIntegerRange.poc");
	}

	@Test
	public void testInList() throws Exception {
		compareResourceOMO("contains/inList.poc");
	}

	@Test
	public void testInSet() throws Exception {
		compareResourceOMO("contains/inSet.poc");
	}

	@Test
	public void testInText() throws Exception {
		compareResourceOMO("contains/inText.poc");
	}

	@Test
	public void testInTimeRange() throws Exception {
		compareResourceOMO("contains/inTimeRange.poc");
	}

	@Test
	public void testInTuple() throws Exception {
		compareResourceOMO("contains/inTuple.poc");
	}

	@Test
	public void testNinCharacterRange() throws Exception {
		compareResourceOMO("contains/ninCharacterRange.poc");
	}

	@Test
	public void testNinDateRange() throws Exception {
		compareResourceOMO("contains/ninDateRange.poc");
	}

	@Test
	public void testNinDict() throws Exception {
		compareResourceOMO("contains/ninDict.poc");
	}

	@Test
	public void testNinIntegerRange() throws Exception {
		compareResourceOMO("contains/ninIntegerRange.poc");
	}

	@Test
	public void testNinList() throws Exception {
		compareResourceOMO("contains/ninList.poc");
	}

	@Test
	public void testNinSet() throws Exception {
		compareResourceOMO("contains/ninSet.poc");
	}

	@Test
	public void testNinText() throws Exception {
		compareResourceOMO("contains/ninText.poc");
	}

	@Test
	public void testNinTimeRange() throws Exception {
		compareResourceOMO("contains/ninTimeRange.poc");
	}

}

