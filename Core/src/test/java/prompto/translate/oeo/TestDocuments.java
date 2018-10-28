package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestDocuments extends BaseOParserTest {

	@Test
	public void testDeepItem() throws Exception {
		compareResourceOEO("documents/deepItem.poc");
	}

	@Test
	public void testDeepMember() throws Exception {
		compareResourceOEO("documents/deepMember.poc");
	}

	@Test
	public void testItem() throws Exception {
		compareResourceOEO("documents/item.poc");
	}

	@Test
	public void testLiteral() throws Exception {
		compareResourceOEO("documents/literal.poc");
	}

	@Test
	public void testMember() throws Exception {
		compareResourceOEO("documents/member.poc");
	}

}

