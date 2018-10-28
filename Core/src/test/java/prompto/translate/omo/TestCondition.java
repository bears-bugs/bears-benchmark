package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestCondition extends BaseOParserTest {

	@Test
	public void testComplexIf() throws Exception {
		compareResourceOMO("condition/complexIf.poc");
	}

	@Test
	public void testEmbeddedIf() throws Exception {
		compareResourceOMO("condition/embeddedIf.poc");
	}

	@Test
	public void testReturnIf() throws Exception {
		compareResourceOMO("condition/returnIf.poc");
	}

	@Test
	public void testSimpleIf() throws Exception {
		compareResourceOMO("condition/simpleIf.poc");
	}

	@Test
	public void testSwitch() throws Exception {
		compareResourceOMO("condition/switch.poc");
	}

	@Test
	public void testTernary() throws Exception {
		compareResourceOMO("condition/ternary.poc");
	}

}

