package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestWidget extends BaseOParserTest {

	@Test
	public void testMinimal() throws Exception {
		compareResourceOMO("widget/minimal.poc");
	}

	@Test
	public void testNative() throws Exception {
		compareResourceOMO("widget/native.poc");
	}

	@Test
	public void testWithEvent() throws Exception {
		compareResourceOMO("widget/withEvent.poc");
	}

}

