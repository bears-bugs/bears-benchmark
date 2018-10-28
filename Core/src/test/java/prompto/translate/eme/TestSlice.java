package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSlice extends BaseEParserTest {

	@Test
	public void testSliceList() throws Exception {
		compareResourceEME("slice/sliceList.pec");
	}

	@Test
	public void testSliceRange() throws Exception {
		compareResourceEME("slice/sliceRange.pec");
	}

	@Test
	public void testSliceText() throws Exception {
		compareResourceEME("slice/sliceText.pec");
	}

}

