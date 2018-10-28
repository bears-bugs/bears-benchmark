package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestUuid extends BaseOParserTest {

	@Test
	public void testUuid() throws Exception {
		compareResourceOMO("uuid/uuid.poc");
	}

}

