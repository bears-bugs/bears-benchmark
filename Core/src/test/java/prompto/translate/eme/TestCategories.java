package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCategories extends BaseEParserTest {

	@Test
	public void testAnyAsParameter() throws Exception {
		compareResourceEME("categories/anyAsParameter.pec");
	}

	@Test
	public void testComposed() throws Exception {
		compareResourceEME("categories/composed.pec");
	}

	@Test
	public void testCopyFromAscendant() throws Exception {
		compareResourceEME("categories/copyFromAscendant.pec");
	}

	@Test
	public void testCopyFromAscendantWithOverride() throws Exception {
		compareResourceEME("categories/copyFromAscendantWithOverride.pec");
	}

	@Test
	public void testCopyFromDescendant() throws Exception {
		compareResourceEME("categories/copyFromDescendant.pec");
	}

	@Test
	public void testCopyFromDescendantWithOverride() throws Exception {
		compareResourceEME("categories/copyFromDescendantWithOverride.pec");
	}

	@Test
	public void testCopyFromDocument() throws Exception {
		compareResourceEME("categories/copyFromDocument.pec");
	}

}

