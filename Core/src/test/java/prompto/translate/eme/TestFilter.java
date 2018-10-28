package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestFilter extends BaseEParserTest {

	@Test
	public void testFilterFromCursor() throws Exception {
		compareResourceEME("filter/filterFromCursor.pec");
	}

	@Test
	public void testFilterFromList() throws Exception {
		compareResourceEME("filter/filterFromList.pec");
	}

	@Test
	public void testFilterFromSet() throws Exception {
		compareResourceEME("filter/filterFromSet.pec");
	}

}

