package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestNative extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAnyId() throws Exception {
		checkInterpretedOutput("native/anyId.pec");
	}

	@Test
	public void testCompiledAnyId() throws Exception {
		checkCompiledOutput("native/anyId.pec");
	}

	@Test
	public void testTranspiledAnyId() throws Exception {
		checkTranspiledOutput("native/anyId.pec");
	}

	@Test
	public void testInterpretedAnyText() throws Exception {
		checkInterpretedOutput("native/anyText.pec");
	}

	@Test
	public void testCompiledAnyText() throws Exception {
		checkCompiledOutput("native/anyText.pec");
	}

	@Test
	public void testTranspiledAnyText() throws Exception {
		checkTranspiledOutput("native/anyText.pec");
	}

	@Test
	public void testInterpretedAttribute() throws Exception {
		checkInterpretedOutput("native/attribute.pec");
	}

	@Test
	public void testCompiledAttribute() throws Exception {
		checkCompiledOutput("native/attribute.pec");
	}

	@Test
	public void testTranspiledAttribute() throws Exception {
		checkTranspiledOutput("native/attribute.pec");
	}

	@Test
	public void testInterpretedCategory() throws Exception {
		checkInterpretedOutput("native/category.pec");
	}

	@Test
	public void testCompiledCategory() throws Exception {
		checkCompiledOutput("native/category.pec");
	}

	@Test
	public void testTranspiledCategory() throws Exception {
		checkTranspiledOutput("native/category.pec");
	}

	@Test
	public void testInterpretedCategoryReturn() throws Exception {
		checkInterpretedOutput("native/categoryReturn.pec");
	}

	@Test
	public void testCompiledCategoryReturn() throws Exception {
		checkCompiledOutput("native/categoryReturn.pec");
	}

	@Test
	public void testTranspiledCategoryReturn() throws Exception {
		checkTranspiledOutput("native/categoryReturn.pec");
	}

	@Test
	public void testInterpretedMethod() throws Exception {
		checkInterpretedOutput("native/method.pec");
	}

	@Test
	public void testCompiledMethod() throws Exception {
		checkCompiledOutput("native/method.pec");
	}

	@Test
	public void testTranspiledMethod() throws Exception {
		checkTranspiledOutput("native/method.pec");
	}

	@Test
	public void testInterpretedNow() throws Exception {
		checkInterpretedOutput("native/now.pec");
	}

	@Test
	public void testCompiledNow() throws Exception {
		checkCompiledOutput("native/now.pec");
	}

	@Test
	public void testTranspiledNow() throws Exception {
		checkTranspiledOutput("native/now.pec");
	}

	@Test
	public void testInterpretedPrinter() throws Exception {
		checkInterpretedOutput("native/printer.pec");
	}

	@Test
	public void testCompiledPrinter() throws Exception {
		checkCompiledOutput("native/printer.pec");
	}

}

