package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLazy extends BaseEParserTest {

	@Test
	public void testCyclic() throws Exception {
		compareResourceEOE("lazy/cyclic.pec");
	}

	@Test
	public void testDict() throws Exception {
		compareResourceEOE("lazy/dict.pec");
	}

	@Test
	public void testList() throws Exception {
		compareResourceEOE("lazy/list.pec");
	}

	@Test
	public void testSet() throws Exception {
		compareResourceEOE("lazy/set.pec");
	}

	@Test
	public void testTransient() throws Exception {
		compareResourceEOE("lazy/transient.pec");
	}

}

