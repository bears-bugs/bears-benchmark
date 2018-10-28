package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestWidget extends BaseOParserTest {

	@Test
	public void testMinimal() throws Exception {
		compareResourceOEO("widget/minimal.poc");
	}

	@Test
	public void testNative() throws Exception {
		compareResourceOEO("widget/native.poc");
	}

	@Test
	public void testWithEvent() throws Exception {
		compareResourceOEO("widget/withEvent.poc");
	}

}

