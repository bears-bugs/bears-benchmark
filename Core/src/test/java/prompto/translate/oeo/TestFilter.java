package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestFilter extends BaseOParserTest {

	@Test
	public void testFilterFromList() throws Exception {
		compareResourceOEO("filter/filterFromList.poc");
	}

	@Test
	public void testFilterFromSet() throws Exception {
		compareResourceOEO("filter/filterFromSet.poc");
	}

}

