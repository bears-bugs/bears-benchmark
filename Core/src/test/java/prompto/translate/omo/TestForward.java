package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestForward extends BaseOParserTest {

	@Test
	public void testForward() throws Exception {
		compareResourceOMO("forward/forward.poc");
	}

}

