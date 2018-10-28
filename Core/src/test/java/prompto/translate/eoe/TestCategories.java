package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCategories extends BaseEParserTest {

	@Test
	public void testAnyAsParameter() throws Exception {
		compareResourceEOE("categories/anyAsParameter.pec");
	}

	@Test
	public void testComposed() throws Exception {
		compareResourceEOE("categories/composed.pec");
	}

	@Test
	public void testCopyFromAscendant() throws Exception {
		compareResourceEOE("categories/copyFromAscendant.pec");
	}

	@Test
	public void testCopyFromAscendantWithOverride() throws Exception {
		compareResourceEOE("categories/copyFromAscendantWithOverride.pec");
	}

	@Test
	public void testCopyFromDescendant() throws Exception {
		compareResourceEOE("categories/copyFromDescendant.pec");
	}

	@Test
	public void testCopyFromDescendantWithOverride() throws Exception {
		compareResourceEOE("categories/copyFromDescendantWithOverride.pec");
	}

	@Test
	public void testCopyFromDocument() throws Exception {
		compareResourceEOE("categories/copyFromDocument.pec");
	}

}

