package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestUuid extends BaseEParserTest {

	@Test
	public void testUuid() throws Exception {
		compareResourceEOE("uuid/uuid.pec");
	}

}

