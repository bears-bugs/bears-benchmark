package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestDocuments extends BaseOParserTest {

	@Test
	public void testDeepItem() throws Exception {
		compareResourceOMO("documents/deepItem.poc");
	}

	@Test
	public void testDeepMember() throws Exception {
		compareResourceOMO("documents/deepMember.poc");
	}

	@Test
	public void testItem() throws Exception {
		compareResourceOMO("documents/item.poc");
	}

	@Test
	public void testLiteral() throws Exception {
		compareResourceOMO("documents/literal.poc");
	}

	@Test
	public void testMember() throws Exception {
		compareResourceOMO("documents/member.poc");
	}

}

