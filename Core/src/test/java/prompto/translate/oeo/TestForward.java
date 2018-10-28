package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestForward extends BaseOParserTest {

	@Test
	public void testForward() throws Exception {
		compareResourceOEO("forward/forward.poc");
	}

}

