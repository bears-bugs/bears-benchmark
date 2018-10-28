package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCondition extends BaseEParserTest {

	@Test
	public void testComplexIf() throws Exception {
		compareResourceEME("condition/complexIf.pec");
	}

	@Test
	public void testEmbeddedIf() throws Exception {
		compareResourceEME("condition/embeddedIf.pec");
	}

	@Test
	public void testReturnIf() throws Exception {
		compareResourceEME("condition/returnIf.pec");
	}

	@Test
	public void testSimpleIf() throws Exception {
		compareResourceEME("condition/simpleIf.pec");
	}

	@Test
	public void testSwitch() throws Exception {
		compareResourceEME("condition/switch.pec");
	}

	@Test
	public void testTernary() throws Exception {
		compareResourceEME("condition/ternary.pec");
	}

}

