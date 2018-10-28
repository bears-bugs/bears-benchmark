package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLazy extends BaseOParserTest {

	@Test
	public void testCyclic() throws Exception {
		compareResourceOEO("lazy/cyclic.poc");
	}

	@Test
	public void testDict() throws Exception {
		compareResourceOEO("lazy/dict.poc");
	}

	@Test
	public void testList() throws Exception {
		compareResourceOEO("lazy/list.poc");
	}

	@Test
	public void testSet() throws Exception {
		compareResourceOEO("lazy/set.poc");
	}

	@Test
	public void testTransient() throws Exception {
		compareResourceOEO("lazy/transient.poc");
	}

}

