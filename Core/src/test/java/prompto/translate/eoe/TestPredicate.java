package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestPredicate extends BaseEParserTest {

	@Test
	public void testContainsItem() throws Exception {
		compareResourceEOE("predicate/containsItem.pec");
	}

	@Test
	public void testEquals() throws Exception {
		compareResourceEOE("predicate/equals.pec");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceEOE("predicate/greater.pec");
	}

	@Test
	public void testHasItem() throws Exception {
		compareResourceEOE("predicate/hasItem.pec");
	}

	@Test
	public void testInList() throws Exception {
		compareResourceEOE("predicate/inList.pec");
	}

	@Test
	public void testLesser() throws Exception {
		compareResourceEOE("predicate/lesser.pec");
	}

	@Test
	public void testNotEquals() throws Exception {
		compareResourceEOE("predicate/notEquals.pec");
	}

	@Test
	public void testPartial() throws Exception {
		compareResourceEOE("predicate/partial.pec");
	}

	@Test
	public void testRoughly() throws Exception {
		compareResourceEOE("predicate/roughly.pec");
	}

}

