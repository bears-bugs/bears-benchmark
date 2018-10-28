package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestComment extends BaseEParserTest {

	@Test
	public void testComment() throws Exception {
		compareResourceEME("comment/comment.pec");
	}

}

