package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestInfer extends BaseEParserTest {

	@Test
	public void testInferDict() throws Exception {
		compareResourceEOE("infer/inferDict.pec");
	}

	@Test
	public void testInferList() throws Exception {
		compareResourceEOE("infer/inferList.pec");
	}

	@Test
	public void testInferSet() throws Exception {
		compareResourceEOE("infer/inferSet.pec");
	}

}

