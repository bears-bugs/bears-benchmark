package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestCategories extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAnyAsParameter() throws Exception {
		checkInterpretedOutput("categories/anyAsParameter.pec");
	}

	@Test
	public void testCompiledAnyAsParameter() throws Exception {
		checkCompiledOutput("categories/anyAsParameter.pec");
	}

	@Test
	public void testTranspiledAnyAsParameter() throws Exception {
		checkTranspiledOutput("categories/anyAsParameter.pec");
	}

	@Test
	public void testInterpretedComposed() throws Exception {
		checkInterpretedOutput("categories/composed.pec");
	}

	@Test
	public void testCompiledComposed() throws Exception {
		checkCompiledOutput("categories/composed.pec");
	}

	@Test
	public void testTranspiledComposed() throws Exception {
		checkTranspiledOutput("categories/composed.pec");
	}

	@Test
	public void testInterpretedCopyFromAscendant() throws Exception {
		checkInterpretedOutput("categories/copyFromAscendant.pec");
	}

	@Test
	public void testCompiledCopyFromAscendant() throws Exception {
		checkCompiledOutput("categories/copyFromAscendant.pec");
	}

	@Test
	public void testTranspiledCopyFromAscendant() throws Exception {
		checkTranspiledOutput("categories/copyFromAscendant.pec");
	}

	@Test
	public void testInterpretedCopyFromAscendantWithOverride() throws Exception {
		checkInterpretedOutput("categories/copyFromAscendantWithOverride.pec");
	}

	@Test
	public void testCompiledCopyFromAscendantWithOverride() throws Exception {
		checkCompiledOutput("categories/copyFromAscendantWithOverride.pec");
	}

	@Test
	public void testTranspiledCopyFromAscendantWithOverride() throws Exception {
		checkTranspiledOutput("categories/copyFromAscendantWithOverride.pec");
	}

	@Test
	public void testInterpretedCopyFromDescendant() throws Exception {
		checkInterpretedOutput("categories/copyFromDescendant.pec");
	}

	@Test
	public void testCompiledCopyFromDescendant() throws Exception {
		checkCompiledOutput("categories/copyFromDescendant.pec");
	}

	@Test
	public void testTranspiledCopyFromDescendant() throws Exception {
		checkTranspiledOutput("categories/copyFromDescendant.pec");
	}

	@Test
	public void testInterpretedCopyFromDescendantWithOverride() throws Exception {
		checkInterpretedOutput("categories/copyFromDescendantWithOverride.pec");
	}

	@Test
	public void testCompiledCopyFromDescendantWithOverride() throws Exception {
		checkCompiledOutput("categories/copyFromDescendantWithOverride.pec");
	}

	@Test
	public void testTranspiledCopyFromDescendantWithOverride() throws Exception {
		checkTranspiledOutput("categories/copyFromDescendantWithOverride.pec");
	}

	@Test
	public void testInterpretedCopyFromDocument() throws Exception {
		checkInterpretedOutput("categories/copyFromDocument.pec");
	}

	@Test
	public void testCompiledCopyFromDocument() throws Exception {
		checkCompiledOutput("categories/copyFromDocument.pec");
	}

	@Test
	public void testTranspiledCopyFromDocument() throws Exception {
		checkTranspiledOutput("categories/copyFromDocument.pec");
	}

}

