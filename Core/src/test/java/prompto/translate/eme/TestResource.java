package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestResource extends BaseEParserTest {

	@Test
	public void testReadResource() throws Exception {
		compareResourceEME("resource/readResource.pec");
	}

	@Test
	public void testReadWithResource() throws Exception {
		compareResourceEME("resource/readWithResource.pec");
	}

	@Test
	public void testWriteResource() throws Exception {
		compareResourceEME("resource/writeResource.pec");
	}

	@Test
	public void testWriteWithResource() throws Exception {
		compareResourceEME("resource/writeWithResource.pec");
	}

}

