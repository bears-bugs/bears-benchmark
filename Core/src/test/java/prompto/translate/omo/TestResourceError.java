package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestResourceError extends BaseOParserTest {

	@Test
	public void testBadRead() throws Exception {
		compareResourceOMO("resourceError/badRead.poc");
	}

	@Test
	public void testBadResource() throws Exception {
		compareResourceOMO("resourceError/badResource.poc");
	}

	@Test
	public void testBadWrite() throws Exception {
		compareResourceOMO("resourceError/badWrite.poc");
	}

}

