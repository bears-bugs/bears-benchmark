package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestDocuments extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedDeepItem() throws Exception {
		checkInterpretedOutput("documents/deepItem.poc");
	}

	@Test
	public void testCompiledDeepItem() throws Exception {
		checkCompiledOutput("documents/deepItem.poc");
	}

	@Test
	public void testTranspiledDeepItem() throws Exception {
		checkTranspiledOutput("documents/deepItem.poc");
	}

	@Test
	public void testInterpretedDeepMember() throws Exception {
		checkInterpretedOutput("documents/deepMember.poc");
	}

	@Test
	public void testCompiledDeepMember() throws Exception {
		checkCompiledOutput("documents/deepMember.poc");
	}

	@Test
	public void testTranspiledDeepMember() throws Exception {
		checkTranspiledOutput("documents/deepMember.poc");
	}

	@Test
	public void testInterpretedItem() throws Exception {
		checkInterpretedOutput("documents/item.poc");
	}

	@Test
	public void testCompiledItem() throws Exception {
		checkCompiledOutput("documents/item.poc");
	}

	@Test
	public void testTranspiledItem() throws Exception {
		checkTranspiledOutput("documents/item.poc");
	}

	@Test
	public void testInterpretedLiteral() throws Exception {
		checkInterpretedOutput("documents/literal.poc");
	}

	@Test
	public void testCompiledLiteral() throws Exception {
		checkCompiledOutput("documents/literal.poc");
	}

	@Test
	public void testTranspiledLiteral() throws Exception {
		checkTranspiledOutput("documents/literal.poc");
	}

	@Test
	public void testInterpretedMember() throws Exception {
		checkInterpretedOutput("documents/member.poc");
	}

	@Test
	public void testCompiledMember() throws Exception {
		checkCompiledOutput("documents/member.poc");
	}

	@Test
	public void testTranspiledMember() throws Exception {
		checkTranspiledOutput("documents/member.poc");
	}

}

