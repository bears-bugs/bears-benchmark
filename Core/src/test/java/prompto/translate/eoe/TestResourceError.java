package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestResourceError extends BaseEParserTest {

	@Test
	public void testBadRead() throws Exception {
		compareResourceEOE("resourceError/badRead.pec");
	}

	@Test
	public void testBadResource() throws Exception {
		compareResourceEOE("resourceError/badResource.pec");
	}

	@Test
	public void testBadWrite() throws Exception {
		compareResourceEOE("resourceError/badWrite.pec");
	}

}

