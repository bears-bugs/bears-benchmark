package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestLazy extends BaseOParserTest {

	@Test
	public void testCyclic() throws Exception {
		compareResourceOMO("lazy/cyclic.poc");
	}

	@Test
	public void testDict() throws Exception {
		compareResourceOMO("lazy/dict.poc");
	}

	@Test
	public void testList() throws Exception {
		compareResourceOMO("lazy/list.poc");
	}

	@Test
	public void testSet() throws Exception {
		compareResourceOMO("lazy/set.poc");
	}

	@Test
	public void testTransient() throws Exception {
		compareResourceOMO("lazy/transient.poc");
	}

}

