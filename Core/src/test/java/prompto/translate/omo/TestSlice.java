package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSlice extends BaseOParserTest {

	@Test
	public void testSliceList() throws Exception {
		compareResourceOMO("slice/sliceList.poc");
	}

	@Test
	public void testSliceRange() throws Exception {
		compareResourceOMO("slice/sliceRange.poc");
	}

	@Test
	public void testSliceText() throws Exception {
		compareResourceOMO("slice/sliceText.poc");
	}

}

