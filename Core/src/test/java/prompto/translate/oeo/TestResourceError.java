package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestResourceError extends BaseOParserTest {

	@Test
	public void testBadRead() throws Exception {
		compareResourceOEO("resourceError/badRead.poc");
	}

	@Test
	public void testBadResource() throws Exception {
		compareResourceOEO("resourceError/badResource.poc");
	}

	@Test
	public void testBadWrite() throws Exception {
		compareResourceOEO("resourceError/badWrite.poc");
	}

}

