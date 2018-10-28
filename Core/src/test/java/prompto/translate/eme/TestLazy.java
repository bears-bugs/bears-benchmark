package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestLazy extends BaseEParserTest {

	@Test
	public void testCyclic() throws Exception {
		compareResourceEME("lazy/cyclic.pec");
	}

	@Test
	public void testDict() throws Exception {
		compareResourceEME("lazy/dict.pec");
	}

	@Test
	public void testList() throws Exception {
		compareResourceEME("lazy/list.pec");
	}

	@Test
	public void testSet() throws Exception {
		compareResourceEME("lazy/set.pec");
	}

	@Test
	public void testTransient() throws Exception {
		compareResourceEME("lazy/transient.pec");
	}

}

