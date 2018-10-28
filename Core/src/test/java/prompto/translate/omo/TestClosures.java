package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestClosures extends BaseOParserTest {

	@Test
	public void testGlobalClosureNoArg() throws Exception {
		compareResourceOMO("closures/globalClosureNoArg.poc");
	}

	@Test
	public void testGlobalClosureWithArg() throws Exception {
		compareResourceOMO("closures/globalClosureWithArg.poc");
	}

	@Test
	public void testInstanceClosureNoArg() throws Exception {
		compareResourceOMO("closures/instanceClosureNoArg.poc");
	}

}

