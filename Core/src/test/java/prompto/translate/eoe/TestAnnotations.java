package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestAnnotations extends BaseEParserTest {

	@Test
	public void testCallback() throws Exception {
		compareResourceEOE("annotations/callback.pec");
	}

	@Test
	public void testCategory() throws Exception {
		compareResourceEOE("annotations/category.pec");
	}

}

