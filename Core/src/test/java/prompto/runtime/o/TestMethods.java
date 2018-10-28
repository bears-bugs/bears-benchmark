package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestMethods extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAnonymous() throws Exception {
		checkInterpretedOutput("methods/anonymous.poc");
	}

	@Test
	public void testCompiledAnonymous() throws Exception {
		checkCompiledOutput("methods/anonymous.poc");
	}

	@Test
	public void testTranspiledAnonymous() throws Exception {
		checkTranspiledOutput("methods/anonymous.poc");
	}

	@Test
	public void testInterpretedAttribute() throws Exception {
		checkInterpretedOutput("methods/attribute.poc");
	}

	@Test
	public void testCompiledAttribute() throws Exception {
		checkCompiledOutput("methods/attribute.poc");
	}

	@Test
	public void testTranspiledAttribute() throws Exception {
		checkTranspiledOutput("methods/attribute.poc");
	}

	@Test
	public void testInterpretedDefault() throws Exception {
		checkInterpretedOutput("methods/default.poc");
	}

	@Test
	public void testCompiledDefault() throws Exception {
		checkCompiledOutput("methods/default.poc");
	}

	@Test
	public void testTranspiledDefault() throws Exception {
		checkTranspiledOutput("methods/default.poc");
	}

	@Test
	public void testInterpretedE_as_e_bug() throws Exception {
		checkInterpretedOutput("methods/e_as_e_bug.poc");
	}

	@Test
	public void testCompiledE_as_e_bug() throws Exception {
		checkCompiledOutput("methods/e_as_e_bug.poc");
	}

	@Test
	public void testTranspiledE_as_e_bug() throws Exception {
		checkTranspiledOutput("methods/e_as_e_bug.poc");
	}

	@Test
	public void testInterpretedExplicit() throws Exception {
		checkInterpretedOutput("methods/explicit.poc");
	}

	@Test
	public void testCompiledExplicit() throws Exception {
		checkCompiledOutput("methods/explicit.poc");
	}

	@Test
	public void testTranspiledExplicit() throws Exception {
		checkTranspiledOutput("methods/explicit.poc");
	}

	@Test
	public void testInterpretedExplicitMember() throws Exception {
		checkInterpretedOutput("methods/explicitMember.poc");
	}

	@Test
	public void testCompiledExplicitMember() throws Exception {
		checkCompiledOutput("methods/explicitMember.poc");
	}

	@Test
	public void testTranspiledExplicitMember() throws Exception {
		checkTranspiledOutput("methods/explicitMember.poc");
	}

	@Test
	public void testInterpretedExpressionWith() throws Exception {
		checkInterpretedOutput("methods/expressionWith.poc");
	}

	@Test
	public void testCompiledExpressionWith() throws Exception {
		checkCompiledOutput("methods/expressionWith.poc");
	}

	@Test
	public void testTranspiledExpressionWith() throws Exception {
		checkTranspiledOutput("methods/expressionWith.poc");
	}

	@Test
	public void testInterpretedExtended() throws Exception {
		checkInterpretedOutput("methods/extended.poc");
	}

	@Test
	public void testCompiledExtended() throws Exception {
		checkCompiledOutput("methods/extended.poc");
	}

	@Test
	public void testTranspiledExtended() throws Exception {
		checkTranspiledOutput("methods/extended.poc");
	}

	@Test
	public void testInterpretedImplicitMember() throws Exception {
		checkInterpretedOutput("methods/implicitMember.poc");
	}

	@Test
	public void testCompiledImplicitMember() throws Exception {
		checkCompiledOutput("methods/implicitMember.poc");
	}

	@Test
	public void testTranspiledImplicitMember() throws Exception {
		checkTranspiledOutput("methods/implicitMember.poc");
	}

	@Test
	public void testInterpretedMember() throws Exception {
		checkInterpretedOutput("methods/member.poc");
	}

	@Test
	public void testCompiledMember() throws Exception {
		checkCompiledOutput("methods/member.poc");
	}

	@Test
	public void testTranspiledMember() throws Exception {
		checkTranspiledOutput("methods/member.poc");
	}

	@Test
	public void testInterpretedOverride() throws Exception {
		checkInterpretedOutput("methods/override.poc");
	}

	@Test
	public void testCompiledOverride() throws Exception {
		checkCompiledOutput("methods/override.poc");
	}

	@Test
	public void testTranspiledOverride() throws Exception {
		checkTranspiledOutput("methods/override.poc");
	}

	@Test
	public void testInterpretedPolymorphic_abstract() throws Exception {
		checkInterpretedOutput("methods/polymorphic_abstract.poc");
	}

	@Test
	public void testCompiledPolymorphic_abstract() throws Exception {
		checkCompiledOutput("methods/polymorphic_abstract.poc");
	}

	@Test
	public void testTranspiledPolymorphic_abstract() throws Exception {
		checkTranspiledOutput("methods/polymorphic_abstract.poc");
	}

	@Test
	public void testInterpretedPolymorphic_implicit() throws Exception {
		checkInterpretedOutput("methods/polymorphic_implicit.poc");
	}

	@Test
	public void testCompiledPolymorphic_implicit() throws Exception {
		checkCompiledOutput("methods/polymorphic_implicit.poc");
	}

	@Test
	public void testTranspiledPolymorphic_implicit() throws Exception {
		checkTranspiledOutput("methods/polymorphic_implicit.poc");
	}

	@Test
	public void testInterpretedPolymorphic_named() throws Exception {
		checkInterpretedOutput("methods/polymorphic_named.poc");
	}

	@Test
	public void testCompiledPolymorphic_named() throws Exception {
		checkCompiledOutput("methods/polymorphic_named.poc");
	}

	@Test
	public void testTranspiledPolymorphic_named() throws Exception {
		checkTranspiledOutput("methods/polymorphic_named.poc");
	}

	@Test
	public void testInterpretedPolymorphic_runtime() throws Exception {
		checkInterpretedOutput("methods/polymorphic_runtime.poc");
	}

	@Test
	public void testCompiledPolymorphic_runtime() throws Exception {
		checkCompiledOutput("methods/polymorphic_runtime.poc");
	}

	@Test
	public void testTranspiledPolymorphic_runtime() throws Exception {
		checkTranspiledOutput("methods/polymorphic_runtime.poc");
	}

	@Test
	public void testInterpretedSpecified() throws Exception {
		checkInterpretedOutput("methods/specified.poc");
	}

	@Test
	public void testCompiledSpecified() throws Exception {
		checkCompiledOutput("methods/specified.poc");
	}

	@Test
	public void testTranspiledSpecified() throws Exception {
		checkTranspiledOutput("methods/specified.poc");
	}

}

