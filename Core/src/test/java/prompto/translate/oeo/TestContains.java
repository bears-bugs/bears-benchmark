package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestContains extends BaseOParserTest {

	@Test
	public void testContainsAllList() throws Exception {
		compareResourceOEO("contains/containsAllList.poc");
	}

	@Test
	public void testContainsAllSet() throws Exception {
		compareResourceOEO("contains/containsAllSet.poc");
	}

	@Test
	public void testContainsAllText() throws Exception {
		compareResourceOEO("contains/containsAllText.poc");
	}

	@Test
	public void testContainsAllTuple() throws Exception {
		compareResourceOEO("contains/containsAllTuple.poc");
	}

	@Test
	public void testContainsAnyList() throws Exception {
		compareResourceOEO("contains/containsAnyList.poc");
	}

	@Test
	public void testContainsAnySet() throws Exception {
		compareResourceOEO("contains/containsAnySet.poc");
	}

	@Test
	public void testContainsAnyText() throws Exception {
		compareResourceOEO("contains/containsAnyText.poc");
	}

	@Test
	public void testContainsAnyTuple() throws Exception {
		compareResourceOEO("contains/containsAnyTuple.poc");
	}

	@Test
	public void testInCharacterRange() throws Exception {
		compareResourceOEO("contains/inCharacterRange.poc");
	}

	@Test
	public void testInDateRange() throws Exception {
		compareResourceOEO("contains/inDateRange.poc");
	}

	@Test
	public void testInDict() throws Exception {
		compareResourceOEO("contains/inDict.poc");
	}

	@Test
	public void testInIntegerRange() throws Exception {
		compareResourceOEO("contains/inIntegerRange.poc");
	}

	@Test
	public void testInList() throws Exception {
		compareResourceOEO("contains/inList.poc");
	}

	@Test
	public void testInSet() throws Exception {
		compareResourceOEO("contains/inSet.poc");
	}

	@Test
	public void testInText() throws Exception {
		compareResourceOEO("contains/inText.poc");
	}

	@Test
	public void testInTimeRange() throws Exception {
		compareResourceOEO("contains/inTimeRange.poc");
	}

	@Test
	public void testInTuple() throws Exception {
		compareResourceOEO("contains/inTuple.poc");
	}

	@Test
	public void testNinCharacterRange() throws Exception {
		compareResourceOEO("contains/ninCharacterRange.poc");
	}

	@Test
	public void testNinDateRange() throws Exception {
		compareResourceOEO("contains/ninDateRange.poc");
	}

	@Test
	public void testNinDict() throws Exception {
		compareResourceOEO("contains/ninDict.poc");
	}

	@Test
	public void testNinIntegerRange() throws Exception {
		compareResourceOEO("contains/ninIntegerRange.poc");
	}

	@Test
	public void testNinList() throws Exception {
		compareResourceOEO("contains/ninList.poc");
	}

	@Test
	public void testNinSet() throws Exception {
		compareResourceOEO("contains/ninSet.poc");
	}

	@Test
	public void testNinText() throws Exception {
		compareResourceOEO("contains/ninText.poc");
	}

	@Test
	public void testNinTimeRange() throws Exception {
		compareResourceOEO("contains/ninTimeRange.poc");
	}

}

