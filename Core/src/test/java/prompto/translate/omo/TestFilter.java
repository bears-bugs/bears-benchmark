package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestFilter extends BaseOParserTest {

	@Test
	public void testFilterFromList() throws Exception {
		compareResourceOMO("filter/filterFromList.poc");
	}

	@Test
	public void testFilterFromSet() throws Exception {
		compareResourceOMO("filter/filterFromSet.poc");
	}

}

