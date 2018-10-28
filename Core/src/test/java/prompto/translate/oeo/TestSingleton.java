package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSingleton extends BaseOParserTest {

	@Test
	public void testAttribute() throws Exception {
		compareResourceOEO("singleton/attribute.poc");
	}

	@Test
	public void testMember() throws Exception {
		compareResourceOEO("singleton/member.poc");
	}

}

