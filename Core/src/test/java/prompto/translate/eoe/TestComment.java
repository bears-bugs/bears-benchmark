package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestComment extends BaseEParserTest {

	@Test
	public void testComment() throws Exception {
		compareResourceEOE("comment/comment.pec");
	}

}

