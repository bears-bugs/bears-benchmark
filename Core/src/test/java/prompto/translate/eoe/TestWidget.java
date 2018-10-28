package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestWidget extends BaseEParserTest {

	@Test
	public void testMinimal() throws Exception {
		compareResourceEOE("widget/minimal.pec");
	}

	@Test
	public void testNative() throws Exception {
		compareResourceEOE("widget/native.pec");
	}

}

