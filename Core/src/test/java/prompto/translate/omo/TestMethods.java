package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMethods extends BaseOParserTest {

	@Test
	public void testAnonymous() throws Exception {
		compareResourceOMO("methods/anonymous.poc");
	}

	@Test
	public void testAttribute() throws Exception {
		compareResourceOMO("methods/attribute.poc");
	}

	@Test
	public void testDefault() throws Exception {
		compareResourceOMO("methods/default.poc");
	}

	@Test
	public void testE_as_e_bug() throws Exception {
		compareResourceOMO("methods/e_as_e_bug.poc");
	}

	@Test
	public void testExplicit() throws Exception {
		compareResourceOMO("methods/explicit.poc");
	}

	@Test
	public void testExplicitMember() throws Exception {
		compareResourceOMO("methods/explicitMember.poc");
	}

	@Test
	public void testExpressionWith() throws Exception {
		compareResourceOMO("methods/expressionWith.poc");
	}

	@Test
	public void testExtended() throws Exception {
		compareResourceOMO("methods/extended.poc");
	}

	@Test
	public void testGlobal() throws Exception {
		compareResourceOMO("methods/global.poc");
	}

	@Test
	public void testImplicitMember() throws Exception {
		compareResourceOMO("methods/implicitMember.poc");
	}

	@Test
	public void testMember() throws Exception {
		compareResourceOMO("methods/member.poc");
	}

	@Test
	public void testOverride() throws Exception {
		compareResourceOMO("methods/override.poc");
	}

	@Test
	public void testPolymorphic_abstract() throws Exception {
		compareResourceOMO("methods/polymorphic_abstract.poc");
	}

	@Test
	public void testPolymorphic_implicit() throws Exception {
		compareResourceOMO("methods/polymorphic_implicit.poc");
	}

	@Test
	public void testPolymorphic_named() throws Exception {
		compareResourceOMO("methods/polymorphic_named.poc");
	}

	@Test
	public void testPolymorphic_runtime() throws Exception {
		compareResourceOMO("methods/polymorphic_runtime.poc");
	}

	@Test
	public void testReturn() throws Exception {
		compareResourceOMO("methods/return.poc");
	}

	@Test
	public void testSpecified() throws Exception {
		compareResourceOMO("methods/specified.poc");
	}

}

