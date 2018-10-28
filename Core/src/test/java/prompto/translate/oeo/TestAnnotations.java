package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestAnnotations extends BaseOParserTest {

	@Test
	public void testCallback() throws Exception {
		compareResourceOEO("annotations/callback.poc");
	}

	@Test
	public void testCategory() throws Exception {
		compareResourceOEO("annotations/category.poc");
	}

}

