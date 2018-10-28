package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestUuid extends BaseEParserTest {

	@Test
	public void testUuid() throws Exception {
		compareResourceEME("uuid/uuid.pec");
	}

}

