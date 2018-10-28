package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestInfer extends BaseEParserTest {

	@Test
	public void testInferDict() throws Exception {
		compareResourceEME("infer/inferDict.pec");
	}

	@Test
	public void testInferList() throws Exception {
		compareResourceEME("infer/inferList.pec");
	}

	@Test
	public void testInferSet() throws Exception {
		compareResourceEME("infer/inferSet.pec");
	}

}

