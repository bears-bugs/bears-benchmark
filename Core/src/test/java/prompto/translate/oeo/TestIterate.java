package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestIterate extends BaseOParserTest {

	@Test
	public void testForEachIntegerList() throws Exception {
		compareResourceOEO("iterate/forEachIntegerList.poc");
	}

}

