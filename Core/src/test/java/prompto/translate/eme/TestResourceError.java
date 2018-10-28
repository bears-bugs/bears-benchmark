package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestResourceError extends BaseEParserTest {

	@Test
	public void testBadRead() throws Exception {
		compareResourceEME("resourceError/badRead.pec");
	}

	@Test
	public void testBadResource() throws Exception {
		compareResourceEME("resourceError/badResource.pec");
	}

	@Test
	public void testBadWrite() throws Exception {
		compareResourceEME("resourceError/badWrite.pec");
	}

}

