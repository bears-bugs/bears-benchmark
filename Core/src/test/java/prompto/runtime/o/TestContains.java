package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestContains extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedContainsAllList() throws Exception {
		checkInterpretedOutput("contains/containsAllList.poc");
	}

	@Test
	public void testCompiledContainsAllList() throws Exception {
		checkCompiledOutput("contains/containsAllList.poc");
	}

	@Test
	public void testTranspiledContainsAllList() throws Exception {
		checkTranspiledOutput("contains/containsAllList.poc");
	}

	@Test
	public void testInterpretedContainsAllSet() throws Exception {
		checkInterpretedOutput("contains/containsAllSet.poc");
	}

	@Test
	public void testCompiledContainsAllSet() throws Exception {
		checkCompiledOutput("contains/containsAllSet.poc");
	}

	@Test
	public void testTranspiledContainsAllSet() throws Exception {
		checkTranspiledOutput("contains/containsAllSet.poc");
	}

	@Test
	public void testInterpretedContainsAllText() throws Exception {
		checkInterpretedOutput("contains/containsAllText.poc");
	}

	@Test
	public void testCompiledContainsAllText() throws Exception {
		checkCompiledOutput("contains/containsAllText.poc");
	}

	@Test
	public void testTranspiledContainsAllText() throws Exception {
		checkTranspiledOutput("contains/containsAllText.poc");
	}

	@Test
	public void testInterpretedContainsAllTuple() throws Exception {
		checkInterpretedOutput("contains/containsAllTuple.poc");
	}

	@Test
	public void testCompiledContainsAllTuple() throws Exception {
		checkCompiledOutput("contains/containsAllTuple.poc");
	}

	@Test
	public void testTranspiledContainsAllTuple() throws Exception {
		checkTranspiledOutput("contains/containsAllTuple.poc");
	}

	@Test
	public void testInterpretedContainsAnyList() throws Exception {
		checkInterpretedOutput("contains/containsAnyList.poc");
	}

	@Test
	public void testCompiledContainsAnyList() throws Exception {
		checkCompiledOutput("contains/containsAnyList.poc");
	}

	@Test
	public void testTranspiledContainsAnyList() throws Exception {
		checkTranspiledOutput("contains/containsAnyList.poc");
	}

	@Test
	public void testInterpretedContainsAnySet() throws Exception {
		checkInterpretedOutput("contains/containsAnySet.poc");
	}

	@Test
	public void testCompiledContainsAnySet() throws Exception {
		checkCompiledOutput("contains/containsAnySet.poc");
	}

	@Test
	public void testTranspiledContainsAnySet() throws Exception {
		checkTranspiledOutput("contains/containsAnySet.poc");
	}

	@Test
	public void testInterpretedContainsAnyText() throws Exception {
		checkInterpretedOutput("contains/containsAnyText.poc");
	}

	@Test
	public void testCompiledContainsAnyText() throws Exception {
		checkCompiledOutput("contains/containsAnyText.poc");
	}

	@Test
	public void testTranspiledContainsAnyText() throws Exception {
		checkTranspiledOutput("contains/containsAnyText.poc");
	}

	@Test
	public void testInterpretedContainsAnyTuple() throws Exception {
		checkInterpretedOutput("contains/containsAnyTuple.poc");
	}

	@Test
	public void testCompiledContainsAnyTuple() throws Exception {
		checkCompiledOutput("contains/containsAnyTuple.poc");
	}

	@Test
	public void testTranspiledContainsAnyTuple() throws Exception {
		checkTranspiledOutput("contains/containsAnyTuple.poc");
	}

	@Test
	public void testInterpretedInCharacterRange() throws Exception {
		checkInterpretedOutput("contains/inCharacterRange.poc");
	}

	@Test
	public void testCompiledInCharacterRange() throws Exception {
		checkCompiledOutput("contains/inCharacterRange.poc");
	}

	@Test
	public void testTranspiledInCharacterRange() throws Exception {
		checkTranspiledOutput("contains/inCharacterRange.poc");
	}

	@Test
	public void testInterpretedInDateRange() throws Exception {
		checkInterpretedOutput("contains/inDateRange.poc");
	}

	@Test
	public void testCompiledInDateRange() throws Exception {
		checkCompiledOutput("contains/inDateRange.poc");
	}

	@Test
	public void testTranspiledInDateRange() throws Exception {
		checkTranspiledOutput("contains/inDateRange.poc");
	}

	@Test
	public void testInterpretedInDict() throws Exception {
		checkInterpretedOutput("contains/inDict.poc");
	}

	@Test
	public void testCompiledInDict() throws Exception {
		checkCompiledOutput("contains/inDict.poc");
	}

	@Test
	public void testTranspiledInDict() throws Exception {
		checkTranspiledOutput("contains/inDict.poc");
	}

	@Test
	public void testInterpretedInIntegerRange() throws Exception {
		checkInterpretedOutput("contains/inIntegerRange.poc");
	}

	@Test
	public void testCompiledInIntegerRange() throws Exception {
		checkCompiledOutput("contains/inIntegerRange.poc");
	}

	@Test
	public void testTranspiledInIntegerRange() throws Exception {
		checkTranspiledOutput("contains/inIntegerRange.poc");
	}

	@Test
	public void testInterpretedInList() throws Exception {
		checkInterpretedOutput("contains/inList.poc");
	}

	@Test
	public void testCompiledInList() throws Exception {
		checkCompiledOutput("contains/inList.poc");
	}

	@Test
	public void testTranspiledInList() throws Exception {
		checkTranspiledOutput("contains/inList.poc");
	}

	@Test
	public void testInterpretedInSet() throws Exception {
		checkInterpretedOutput("contains/inSet.poc");
	}

	@Test
	public void testCompiledInSet() throws Exception {
		checkCompiledOutput("contains/inSet.poc");
	}

	@Test
	public void testTranspiledInSet() throws Exception {
		checkTranspiledOutput("contains/inSet.poc");
	}

	@Test
	public void testInterpretedInText() throws Exception {
		checkInterpretedOutput("contains/inText.poc");
	}

	@Test
	public void testCompiledInText() throws Exception {
		checkCompiledOutput("contains/inText.poc");
	}

	@Test
	public void testTranspiledInText() throws Exception {
		checkTranspiledOutput("contains/inText.poc");
	}

	@Test
	public void testInterpretedInTimeRange() throws Exception {
		checkInterpretedOutput("contains/inTimeRange.poc");
	}

	@Test
	public void testCompiledInTimeRange() throws Exception {
		checkCompiledOutput("contains/inTimeRange.poc");
	}

	@Test
	public void testTranspiledInTimeRange() throws Exception {
		checkTranspiledOutput("contains/inTimeRange.poc");
	}

	@Test
	public void testInterpretedInTuple() throws Exception {
		checkInterpretedOutput("contains/inTuple.poc");
	}

	@Test
	public void testCompiledInTuple() throws Exception {
		checkCompiledOutput("contains/inTuple.poc");
	}

	@Test
	public void testTranspiledInTuple() throws Exception {
		checkTranspiledOutput("contains/inTuple.poc");
	}

	@Test
	public void testInterpretedNinCharacterRange() throws Exception {
		checkInterpretedOutput("contains/ninCharacterRange.poc");
	}

	@Test
	public void testCompiledNinCharacterRange() throws Exception {
		checkCompiledOutput("contains/ninCharacterRange.poc");
	}

	@Test
	public void testTranspiledNinCharacterRange() throws Exception {
		checkTranspiledOutput("contains/ninCharacterRange.poc");
	}

	@Test
	public void testInterpretedNinDateRange() throws Exception {
		checkInterpretedOutput("contains/ninDateRange.poc");
	}

	@Test
	public void testCompiledNinDateRange() throws Exception {
		checkCompiledOutput("contains/ninDateRange.poc");
	}

	@Test
	public void testTranspiledNinDateRange() throws Exception {
		checkTranspiledOutput("contains/ninDateRange.poc");
	}

	@Test
	public void testInterpretedNinDict() throws Exception {
		checkInterpretedOutput("contains/ninDict.poc");
	}

	@Test
	public void testCompiledNinDict() throws Exception {
		checkCompiledOutput("contains/ninDict.poc");
	}

	@Test
	public void testTranspiledNinDict() throws Exception {
		checkTranspiledOutput("contains/ninDict.poc");
	}

	@Test
	public void testInterpretedNinIntegerRange() throws Exception {
		checkInterpretedOutput("contains/ninIntegerRange.poc");
	}

	@Test
	public void testCompiledNinIntegerRange() throws Exception {
		checkCompiledOutput("contains/ninIntegerRange.poc");
	}

	@Test
	public void testTranspiledNinIntegerRange() throws Exception {
		checkTranspiledOutput("contains/ninIntegerRange.poc");
	}

	@Test
	public void testInterpretedNinList() throws Exception {
		checkInterpretedOutput("contains/ninList.poc");
	}

	@Test
	public void testCompiledNinList() throws Exception {
		checkCompiledOutput("contains/ninList.poc");
	}

	@Test
	public void testTranspiledNinList() throws Exception {
		checkTranspiledOutput("contains/ninList.poc");
	}

	@Test
	public void testInterpretedNinSet() throws Exception {
		checkInterpretedOutput("contains/ninSet.poc");
	}

	@Test
	public void testCompiledNinSet() throws Exception {
		checkCompiledOutput("contains/ninSet.poc");
	}

	@Test
	public void testTranspiledNinSet() throws Exception {
		checkTranspiledOutput("contains/ninSet.poc");
	}

	@Test
	public void testInterpretedNinText() throws Exception {
		checkInterpretedOutput("contains/ninText.poc");
	}

	@Test
	public void testCompiledNinText() throws Exception {
		checkCompiledOutput("contains/ninText.poc");
	}

	@Test
	public void testTranspiledNinText() throws Exception {
		checkTranspiledOutput("contains/ninText.poc");
	}

	@Test
	public void testInterpretedNinTimeRange() throws Exception {
		checkInterpretedOutput("contains/ninTimeRange.poc");
	}

	@Test
	public void testCompiledNinTimeRange() throws Exception {
		checkCompiledOutput("contains/ninTimeRange.poc");
	}

	@Test
	public void testTranspiledNinTimeRange() throws Exception {
		checkTranspiledOutput("contains/ninTimeRange.poc");
	}

}

