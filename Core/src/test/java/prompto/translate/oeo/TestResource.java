package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestResource extends BaseOParserTest {

	@Test
	public void testReadResource() throws Exception {
		compareResourceOEO("resource/readResource.poc");
	}

	@Test
	public void testReadWithResource() throws Exception {
		compareResourceOEO("resource/readWithResource.poc");
	}

	@Test
	public void testWriteResource() throws Exception {
		compareResourceOEO("resource/writeResource.poc");
	}

	@Test
	public void testWriteWithResource() throws Exception {
		compareResourceOEO("resource/writeWithResource.poc");
	}

}

