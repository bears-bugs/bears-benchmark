package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSelf extends BaseEParserTest {

	@Test
	public void testSelfAsParameter() throws Exception {
		compareResourceEOE("self/selfAsParameter.pec");
	}

	@Test
	public void testSelfMember() throws Exception {
		compareResourceEOE("self/selfMember.pec");
	}

}

