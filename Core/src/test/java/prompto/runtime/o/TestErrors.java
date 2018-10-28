package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestErrors extends BaseOParserTest {

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
		checkInterpretedOutput("errors/divideByZero.poc");
	}

	@Test
	public void testCompiledDivideByZero() throws Exception {
		checkCompiledOutput("errors/divideByZero.poc");
	}

	@Test
	public void testTranspiledDivideByZero() throws Exception {
		checkTranspiledOutput("errors/divideByZero.poc");
	}

	@Test
	public void testInterpretedIndexOutOfRange_listItem() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-listItem.poc");
	}

	@Test
	public void testCompiledIndexOutOfRange_listItem() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-listItem.poc");
	}

	@Test
	public void testTranspiledIndexOutOfRange_listItem() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-listItem.poc");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceList() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceList.poc");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceList() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceList.poc");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceList() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceList.poc");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceRange() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceRange.poc");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceRange() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceRange.poc");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceRange() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceRange.poc");
	}

	@Test
	public void testInterpretedIndexOutOfRange_sliceText() throws Exception {
		checkInterpretedOutput("errors/indexOutOfRange-sliceText.poc");
	}

	@Test
	public void testCompiledIndexOutOfRange_sliceText() throws Exception {
		checkCompiledOutput("errors/indexOutOfRange-sliceText.poc");
	}

	@Test
	public void testTranspiledIndexOutOfRange_sliceText() throws Exception {
		checkTranspiledOutput("errors/indexOutOfRange-sliceText.poc");
	}

	@Test
	public void testInterpretedNullDict() throws Exception {
		checkInterpretedOutput("errors/nullDict.poc");
	}

	@Test
	public void testCompiledNullDict() throws Exception {
		checkCompiledOutput("errors/nullDict.poc");
	}

	@Test
	public void testTranspiledNullDict() throws Exception {
		checkTranspiledOutput("errors/nullDict.poc");
	}

	@Test
	public void testInterpretedNullItem() throws Exception {
		checkInterpretedOutput("errors/nullItem.poc");
	}

	@Test
	public void testCompiledNullItem() throws Exception {
		checkCompiledOutput("errors/nullItem.poc");
	}

	@Test
	public void testTranspiledNullItem() throws Exception {
		checkTranspiledOutput("errors/nullItem.poc");
	}

	@Test
	public void testInterpretedNullKey() throws Exception {
		checkInterpretedOutput("errors/nullKey.poc");
	}

	@Test
	public void testCompiledNullKey() throws Exception {
		checkCompiledOutput("errors/nullKey.poc");
	}

	@Test
	public void testTranspiledNullKey() throws Exception {
		checkTranspiledOutput("errors/nullKey.poc");
	}

	@Test
	public void testInterpretedNullMember() throws Exception {
		checkInterpretedOutput("errors/nullMember.poc");
	}

	@Test
	public void testCompiledNullMember() throws Exception {
		checkCompiledOutput("errors/nullMember.poc");
	}

	@Test
	public void testTranspiledNullMember() throws Exception {
		checkTranspiledOutput("errors/nullMember.poc");
	}

	@Test
	public void testInterpretedNullMethod() throws Exception {
		checkInterpretedOutput("errors/nullMethod.poc");
	}

	@Test
	public void testCompiledNullMethod() throws Exception {
		checkCompiledOutput("errors/nullMethod.poc");
	}

	@Test
	public void testTranspiledNullMethod() throws Exception {
		checkTranspiledOutput("errors/nullMethod.poc");
	}

	@Test
	public void testInterpretedUserException() throws Exception {
		checkInterpretedOutput("errors/userException.poc");
	}

	@Test
	public void testCompiledUserException() throws Exception {
		checkCompiledOutput("errors/userException.poc");
	}

	@Test
	public void testTranspiledUserException() throws Exception {
		checkTranspiledOutput("errors/userException.poc");
	}

}

