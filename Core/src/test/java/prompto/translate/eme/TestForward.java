package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestForward extends BaseEParserTest {

	@Test
	public void testForward() throws Exception {
		compareResourceEME("forward/forward.pec");
	}

}

