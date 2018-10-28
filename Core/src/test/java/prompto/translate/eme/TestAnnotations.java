package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestAnnotations extends BaseEParserTest {

	@Test
	public void testCallback() throws Exception {
		compareResourceEME("annotations/callback.pec");
	}

	@Test
	public void testCategory() throws Exception {
		compareResourceEME("annotations/category.pec");
	}

}

