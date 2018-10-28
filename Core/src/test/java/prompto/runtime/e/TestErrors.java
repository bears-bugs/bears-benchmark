package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestErrors extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedDivideByZero() throws Exception {
		checkInterpretedOutput("errors/divideByZero.pec");
	}

	@Test
	public void testCompiledDivideByZero() throws Exception {
		checkCompiledOutput("errors/divideByZero.pec");
	}

	@Test
	public void testTranspiledDivideByZero() throws Exception {
		checkTranspiledOutput("errors/divideByZero.pec");
	}

	@Test
	public void testInterpretedIndexOutOfRange_listItem() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-listItem.pec");
	}

	@Test
	public void testCompiledIndexOutOfRange_listItem() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-listItem.pec");
	}

	@Test
	public void testTranspiledIndexOutOfRange_listItem() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-listItem.pec");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceList() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceList.pec");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceList() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceList.pec");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceList() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceList.pec");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceRange() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceRange.pec");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceRange() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceRange.pec");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceRange() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceRange.pec");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceText() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceText.pec");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceText() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceText.pec");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceText() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceText.pec");
	}

	@Test
	public void testInterpretedNullDict() throws Exception {
		checkInterpretedOutput("errors/nullDict.pec");
	}

	@Test
	public void testCompiledNullDict() throws Exception {
		checkCompiledOutput("errors/nullDict.pec");
	}

	@Test
	public void testTranspiledNullDict() throws Exception {
		checkTranspiledOutput("errors/nullDict.pec");
	}

	@Test
	public void testInterpretedNullItem() throws Exception {
		checkInterpretedOutput("errors/nullItem.pec");
	}

	@Test
	public void testCompiledNullItem() throws Exception {
		checkCompiledOutput("errors/nullItem.pec");
	}

	@Test
	public void testTranspiledNullItem() throws Exception {
		checkTranspiledOutput("errors/nullItem.pec");
	}

	@Test
	public void testInterpretedNullKey() throws Exception {
		checkInterpretedOutput("errors/nullKey.pec");
	}

	@Test
	public void testCompiledNullKey() throws Exception {
		checkCompiledOutput("errors/nullKey.pec");
	}

	@Test
	public void testTranspiledNullKey() throws Exception {
		checkTranspiledOutput("errors/nullKey.pec");
	}

	@Test
	public void testInterpretedNullMember() throws Exception {
		checkInterpretedOutput("errors/nullMember.pec");
	}

	@Test
	public void testCompiledNullMember() throws Exception {
		checkCompiledOutput("errors/nullMember.pec");
	}

	@Test
	public void testTranspiledNullMember() throws Exception {
		checkTranspiledOutput("errors/nullMember.pec");
	}

	@Test
	public void testInterpretedNullMethod() throws Exception {
		checkInterpretedOutput("errors/nullMethod.pec");
	}

	@Test
	public void testCompiledNullMethod() throws Exception {
		checkCompiledOutput("errors/nullMethod.pec");
	}

	@Test
	public void testTranspiledNullMethod() throws Exception {
		checkTranspiledOutput("errors/nullMethod.pec");
	}

	@Test
	public void testInterpretedUserException() throws Exception {
		checkInterpretedOutput("errors/userException.pec");
	}

	@Test
	public void testCompiledUserException() throws Exception {
		checkCompiledOutput("errors/userException.pec");
	}

	@Test
	public void testTranspiledUserException() throws Exception {
		checkTranspiledOutput("errors/userException.pec");
	}

}

