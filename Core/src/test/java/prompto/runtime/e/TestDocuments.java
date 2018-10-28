package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestDocuments extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedBlob() throws Exception {
		checkInterpretedOutput("documents/blob.pec");
	}

	@Test
	public void testCompiledBlob() throws Exception {
		checkCompiledOutput("documents/blob.pec");
	}

	@Test
	public void testTranspiledBlob() throws Exception {
		checkTranspiledOutput("documents/blob.pec");
	}

	@Test
	public void testInterpretedDeepItem() throws Exception {
		checkInterpretedOutput("documents/deepItem.pec");
	}

	@Test
	public void testCompiledDeepItem() throws Exception {
		checkCompiledOutput("documents/deepItem.pec");
	}

	@Test
	public void testTranspiledDeepItem() throws Exception {
		checkTranspiledOutput("documents/deepItem.pec");
	}

	@Test
	public void testInterpretedDeepMember() throws Exception {
		checkInterpretedOutput("documents/deepMember.pec");
	}

	@Test
	public void testCompiledDeepMember() throws Exception {
		checkCompiledOutput("documents/deepMember.pec");
	}

	@Test
	public void testTranspiledDeepMember() throws Exception {
		checkTranspiledOutput("documents/deepMember.pec");
	}

	@Test
	public void testInterpretedItem() throws Exception {
		checkInterpretedOutput("documents/item.pec");
	}

	@Test
	public void testCompiledItem() throws Exception {
		checkCompiledOutput("documents/item.pec");
	}

	@Test
	public void testTranspiledItem() throws Exception {
		checkTranspiledOutput("documents/item.pec");
	}

	@Test
	public void testInterpretedLiteral() throws Exception {
		checkInterpretedOutput("documents/literal.pec");
	}

	@Test
	public void testCompiledLiteral() throws Exception {
		checkCompiledOutput("documents/literal.pec");
	}

	@Test
	public void testTranspiledLiteral() throws Exception {
		checkTranspiledOutput("documents/literal.pec");
	}

	@Test
	public void testInterpretedMember() throws Exception {
		checkInterpretedOutput("documents/member.pec");
	}

	@Test
	public void testCompiledMember() throws Exception {
		checkCompiledOutput("documents/member.pec");
	}

	@Test
	public void testTranspiledMember() throws Exception {
		checkTranspiledOutput("documents/member.pec");
	}

	@Test
	public void testInterpretedNamedItem() throws Exception {
		checkInterpretedOutput("documents/namedItem.pec");
	}

	@Test
	public void testCompiledNamedItem() throws Exception {
		checkCompiledOutput("documents/namedItem.pec");
	}

	@Test
	public void testTranspiledNamedItem() throws Exception {
		checkTranspiledOutput("documents/namedItem.pec");
	}

}

