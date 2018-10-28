package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestContains extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedContainsAllDict() throws Exception {
		checkInterpretedOutput("contains/containsAllDict.pec");
	}

	@Test
	public void testCompiledContainsAllDict() throws Exception {
		checkCompiledOutput("contains/containsAllDict.pec");
	}

	@Test
	public void testTranspiledContainsAllDict() throws Exception {
		checkTranspiledOutput("contains/containsAllDict.pec");
	}

	@Test
	public void testInterpretedContainsAllList() throws Exception {
		checkInterpretedOutput("contains/containsAllList.pec");
	}

	@Test
	public void testCompiledContainsAllList() throws Exception {
		checkCompiledOutput("contains/containsAllList.pec");
	}

	@Test
	public void testTranspiledContainsAllList() throws Exception {
		checkTranspiledOutput("contains/containsAllList.pec");
	}

	@Test
	public void testInterpretedContainsAllRange() throws Exception {
		checkInterpretedOutput("contains/containsAllRange.pec");
	}

	@Test
	public void testCompiledContainsAllRange() throws Exception {
		checkCompiledOutput("contains/containsAllRange.pec");
	}

	@Test
	public void testTranspiledContainsAllRange() throws Exception {
		checkTranspiledOutput("contains/containsAllRange.pec");
	}

	@Test
	public void testInterpretedContainsAllSet() throws Exception {
		checkInterpretedOutput("contains/containsAllSet.pec");
	}

	@Test
	public void testCompiledContainsAllSet() throws Exception {
		checkCompiledOutput("contains/containsAllSet.pec");
	}

	@Test
	public void testTranspiledContainsAllSet() throws Exception {
		checkTranspiledOutput("contains/containsAllSet.pec");
	}

	@Test
	public void testInterpretedContainsAllText() throws Exception {
		checkInterpretedOutput("contains/containsAllText.pec");
	}

	@Test
	public void testCompiledContainsAllText() throws Exception {
		checkCompiledOutput("contains/containsAllText.pec");
	}

	@Test
	public void testTranspiledContainsAllText() throws Exception {
		checkTranspiledOutput("contains/containsAllText.pec");
	}

	@Test
	public void testInterpretedContainsAllTuple() throws Exception {
		checkInterpretedOutput("contains/containsAllTuple.pec");
	}

	@Test
	public void testCompiledContainsAllTuple() throws Exception {
		checkCompiledOutput("contains/containsAllTuple.pec");
	}

	@Test
	public void testTranspiledContainsAllTuple() throws Exception {
		checkTranspiledOutput("contains/containsAllTuple.pec");
	}

	@Test
	public void testInterpretedContainsAnyDict() throws Exception {
		checkInterpretedOutput("contains/containsAnyDict.pec");
	}

	@Test
	public void testCompiledContainsAnyDict() throws Exception {
		checkCompiledOutput("contains/containsAnyDict.pec");
	}

	@Test
	public void testTranspiledContainsAnyDict() throws Exception {
		checkTranspiledOutput("contains/containsAnyDict.pec");
	}

	@Test
	public void testInterpretedContainsAnyList() throws Exception {
		checkInterpretedOutput("contains/containsAnyList.pec");
	}

	@Test
	public void testCompiledContainsAnyList() throws Exception {
		checkCompiledOutput("contains/containsAnyList.pec");
	}

	@Test
	public void testTranspiledContainsAnyList() throws Exception {
		checkTranspiledOutput("contains/containsAnyList.pec");
	}

	@Test
	public void testInterpretedContainsAnyRange() throws Exception {
		checkInterpretedOutput("contains/containsAnyRange.pec");
	}

	@Test
	public void testCompiledContainsAnyRange() throws Exception {
		checkCompiledOutput("contains/containsAnyRange.pec");
	}

	@Test
	public void testTranspiledContainsAnyRange() throws Exception {
		checkTranspiledOutput("contains/containsAnyRange.pec");
	}

	@Test
	public void testInterpretedContainsAnySet() throws Exception {
		checkInterpretedOutput("contains/containsAnySet.pec");
	}

	@Test
	public void testCompiledContainsAnySet() throws Exception {
		checkCompiledOutput("contains/containsAnySet.pec");
	}

	@Test
	public void testTranspiledContainsAnySet() throws Exception {
		checkTranspiledOutput("contains/containsAnySet.pec");
	}

	@Test
	public void testInterpretedContainsAnyText() throws Exception {
		checkInterpretedOutput("contains/containsAnyText.pec");
	}

	@Test
	public void testCompiledContainsAnyText() throws Exception {
		checkCompiledOutput("contains/containsAnyText.pec");
	}

	@Test
	public void testTranspiledContainsAnyText() throws Exception {
		checkTranspiledOutput("contains/containsAnyText.pec");
	}

	@Test
	public void testInterpretedContainsAnyTuple() throws Exception {
		checkInterpretedOutput("contains/containsAnyTuple.pec");
	}

	@Test
	public void testCompiledContainsAnyTuple() throws Exception {
		checkCompiledOutput("contains/containsAnyTuple.pec");
	}

	@Test
	public void testTranspiledContainsAnyTuple() throws Exception {
		checkTranspiledOutput("contains/containsAnyTuple.pec");
	}

	@Test
	public void testInterpretedInCharacterRange() throws Exception {
		checkInterpretedOutput("contains/inCharacterRange.pec");
	}

	@Test
	public void testCompiledInCharacterRange() throws Exception {
		checkCompiledOutput("contains/inCharacterRange.pec");
	}

	@Test
	public void testTranspiledInCharacterRange() throws Exception {
		checkTranspiledOutput("contains/inCharacterRange.pec");
	}

	@Test
	public void testInterpretedInDateRange() throws Exception {
		checkInterpretedOutput("contains/inDateRange.pec");
	}

	@Test
	public void testCompiledInDateRange() throws Exception {
		checkCompiledOutput("contains/inDateRange.pec");
	}

	@Test
	public void testTranspiledInDateRange() throws Exception {
		checkTranspiledOutput("contains/inDateRange.pec");
	}

	@Test
	public void testInterpretedInDict() throws Exception {
		checkInterpretedOutput("contains/inDict.pec");
	}

	@Test
	public void testCompiledInDict() throws Exception {
		checkCompiledOutput("contains/inDict.pec");
	}

	@Test
	public void testTranspiledInDict() throws Exception {
		checkTranspiledOutput("contains/inDict.pec");
	}

	@Test
	public void testInterpretedInIntegerRange() throws Exception {
		checkInterpretedOutput("contains/inIntegerRange.pec");
	}

	@Test
	public void testCompiledInIntegerRange() throws Exception {
		checkCompiledOutput("contains/inIntegerRange.pec");
	}

	@Test
	public void testTranspiledInIntegerRange() throws Exception {
		checkTranspiledOutput("contains/inIntegerRange.pec");
	}

	@Test
	public void testInterpretedInList() throws Exception {
		checkInterpretedOutput("contains/inList.pec");
	}

	@Test
	public void testCompiledInList() throws Exception {
		checkCompiledOutput("contains/inList.pec");
	}

	@Test
	public void testTranspiledInList() throws Exception {
		checkTranspiledOutput("contains/inList.pec");
	}

	@Test
	public void testInterpretedInSet() throws Exception {
		checkInterpretedOutput("contains/inSet.pec");
	}

	@Test
	public void testCompiledInSet() throws Exception {
		checkCompiledOutput("contains/inSet.pec");
	}

	@Test
	public void testTranspiledInSet() throws Exception {
		checkTranspiledOutput("contains/inSet.pec");
	}

	@Test
	public void testInterpretedInText() throws Exception {
		checkInterpretedOutput("contains/inText.pec");
	}

	@Test
	public void testCompiledInText() throws Exception {
		checkCompiledOutput("contains/inText.pec");
	}

	@Test
	public void testTranspiledInText() throws Exception {
		checkTranspiledOutput("contains/inText.pec");
	}

	@Test
	public void testInterpretedInTimeRange() throws Exception {
		checkInterpretedOutput("contains/inTimeRange.pec");
	}

	@Test
	public void testCompiledInTimeRange() throws Exception {
		checkCompiledOutput("contains/inTimeRange.pec");
	}

	@Test
	public void testTranspiledInTimeRange() throws Exception {
		checkTranspiledOutput("contains/inTimeRange.pec");
	}

	@Test
	public void testInterpretedInTuple() throws Exception {
		checkInterpretedOutput("contains/inTuple.pec");
	}

	@Test
	public void testCompiledInTuple() throws Exception {
		checkCompiledOutput("contains/inTuple.pec");
	}

	@Test
	public void testTranspiledInTuple() throws Exception {
		checkTranspiledOutput("contains/inTuple.pec");
	}

	@Test
	public void testInterpretedNinCharacterRange() throws Exception {
		checkInterpretedOutput("contains/ninCharacterRange.pec");
	}

	@Test
	public void testCompiledNinCharacterRange() throws Exception {
		checkCompiledOutput("contains/ninCharacterRange.pec");
	}

	@Test
	public void testTranspiledNinCharacterRange() throws Exception {
		checkTranspiledOutput("contains/ninCharacterRange.pec");
	}

	@Test
	public void testInterpretedNinDateRange() throws Exception {
		checkInterpretedOutput("contains/ninDateRange.pec");
	}

	@Test
	public void testCompiledNinDateRange() throws Exception {
		checkCompiledOutput("contains/ninDateRange.pec");
	}

	@Test
	public void testTranspiledNinDateRange() throws Exception {
		checkTranspiledOutput("contains/ninDateRange.pec");
	}

	@Test
	public void testInterpretedNinDict() throws Exception {
		checkInterpretedOutput("contains/ninDict.pec");
	}

	@Test
	public void testCompiledNinDict() throws Exception {
		checkCompiledOutput("contains/ninDict.pec");
	}

	@Test
	public void testTranspiledNinDict() throws Exception {
		checkTranspiledOutput("contains/ninDict.pec");
	}

	@Test
	public void testInterpretedNinIntegerRange() throws Exception {
		checkInterpretedOutput("contains/ninIntegerRange.pec");
	}

	@Test
	public void testCompiledNinIntegerRange() throws Exception {
		checkCompiledOutput("contains/ninIntegerRange.pec");
	}

	@Test
	public void testTranspiledNinIntegerRange() throws Exception {
		checkTranspiledOutput("contains/ninIntegerRange.pec");
	}

	@Test
	public void testInterpretedNinList() throws Exception {
		checkInterpretedOutput("contains/ninList.pec");
	}

	@Test
	public void testCompiledNinList() throws Exception {
		checkCompiledOutput("contains/ninList.pec");
	}

	@Test
	public void testTranspiledNinList() throws Exception {
		checkTranspiledOutput("contains/ninList.pec");
	}

	@Test
	public void testInterpretedNinSet() throws Exception {
		checkInterpretedOutput("contains/ninSet.pec");
	}

	@Test
	public void testCompiledNinSet() throws Exception {
		checkCompiledOutput("contains/ninSet.pec");
	}

	@Test
	public void testTranspiledNinSet() throws Exception {
		checkTranspiledOutput("contains/ninSet.pec");
	}

	@Test
	public void testInterpretedNinText() throws Exception {
		checkInterpretedOutput("contains/ninText.pec");
	}

	@Test
	public void testCompiledNinText() throws Exception {
		checkCompiledOutput("contains/ninText.pec");
	}

	@Test
	public void testTranspiledNinText() throws Exception {
		checkTranspiledOutput("contains/ninText.pec");
	}

	@Test
	public void testInterpretedNinTimeRange() throws Exception {
		checkInterpretedOutput("contains/ninTimeRange.pec");
	}

	@Test
	public void testCompiledNinTimeRange() throws Exception {
		checkCompiledOutput("contains/ninTimeRange.pec");
	}

	@Test
	public void testTranspiledNinTimeRange() throws Exception {
		checkTranspiledOutput("contains/ninTimeRange.pec");
	}

}

