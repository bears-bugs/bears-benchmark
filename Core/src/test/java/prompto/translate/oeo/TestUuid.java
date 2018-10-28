package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestUuid extends BaseOParserTest {

	@Test
	public void testUuid() throws Exception {
		compareResourceOEO("uuid/uuid.poc");
	}

}

