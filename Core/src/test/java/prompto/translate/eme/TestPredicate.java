package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestPredicate extends BaseEParserTest {

	@Test
	public void testContainsItem() throws Exception {
		compareResourceEME("predicate/containsItem.pec");
	}

	@Test
	public void testEquals() throws Exception {
		compareResourceEME("predicate/equals.pec");
	}

	@Test
	public void testGreater() throws Exception {
		compareResourceEME("predicate/greater.pec");
	}

	@Test
	public void testHasItem() throws Exception {
		compareResourceEME("predicate/hasItem.pec");
	}

	@Test
	public void testInList() throws Exception {
		compareResourceEME("predicate/inList.pec");
	}

	@Test
	public void testLesser() throws Exception {
		compareResourceEME("predicate/lesser.pec");
	}

	@Test
	public void testNotEquals() throws Exception {
		compareResourceEME("predicate/notEquals.pec");
	}

	@Test
	public void testPartial() throws Exception {
		compareResourceEME("predicate/partial.pec");
	}

	@Test
	public void testRoughly() throws Exception {
		compareResourceEME("predicate/roughly.pec");
	}

}

