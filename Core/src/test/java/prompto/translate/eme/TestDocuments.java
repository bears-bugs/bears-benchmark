package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestDocuments extends BaseEParserTest {

	@Test
	public void testBlob() throws Exception {
		compareResourceEME("documents/blob.pec");
	}

	@Test
	public void testDeepItem() throws Exception {
		compareResourceEME("documents/deepItem.pec");
	}

	@Test
	public void testDeepMember() throws Exception {
		compareResourceEME("documents/deepMember.pec");
	}

	@Test
	public void testItem() throws Exception {
		compareResourceEME("documents/item.pec");
	}

	@Test
	public void testLiteral() throws Exception {
		compareResourceEME("documents/literal.pec");
	}

	@Test
	public void testMember() throws Exception {
		compareResourceEME("documents/member.pec");
	}

	@Test
	public void testNamedItem() throws Exception {
		compareResourceEME("documents/namedItem.pec");
	}

}

