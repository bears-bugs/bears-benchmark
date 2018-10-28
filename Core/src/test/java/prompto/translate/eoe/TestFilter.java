package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestFilter extends BaseEParserTest {

	@Test
	public void testFilterFromCursor() throws Exception {
		compareResourceEOE("filter/filterFromCursor.pec");
	}

	@Test
	public void testFilterFromList() throws Exception {
		compareResourceEOE("filter/filterFromList.pec");
	}

	@Test
	public void testFilterFromSet() throws Exception {
		compareResourceEOE("filter/filterFromSet.pec");
	}

}

