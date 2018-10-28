package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestResource extends BaseOParserTest {

	@Test
	public void testReadResource() throws Exception {
		compareResourceOMO("resource/readResource.poc");
	}

	@Test
	public void testReadWithResource() throws Exception {
		compareResourceOMO("resource/readWithResource.poc");
	}

	@Test
	public void testWriteResource() throws Exception {
		compareResourceOMO("resource/writeResource.poc");
	}

	@Test
	public void testWriteWithResource() throws Exception {
		compareResourceOMO("resource/writeWithResource.poc");
	}

}

