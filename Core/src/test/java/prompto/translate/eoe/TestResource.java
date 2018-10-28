package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestResource extends BaseEParserTest {

	@Test
	public void testReadResource() throws Exception {
		compareResourceEOE("resource/readResource.pec");
	}

	@Test
	public void testReadWithResource() throws Exception {
		compareResourceEOE("resource/readWithResource.pec");
	}

	@Test
	public void testWriteResource() throws Exception {
		compareResourceEOE("resource/writeResource.pec");
	}

	@Test
	public void testWriteWithResource() throws Exception {
		compareResourceEOE("resource/writeWithResource.pec");
	}

}

