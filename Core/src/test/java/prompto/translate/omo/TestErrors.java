package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestErrors extends BaseOParserTest {

	@Test
	public void testDivideByZero() throws Exception {
		compareResourceOMO("errors/divideByZero.poc");
	}

	@Test
	public void testIndexOutOfRange_listItem() throws Exception {
		compareResourceOMO("errors/indexOutOfRange-listItem.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceList() throws Exception {
		compareResourceOMO("errors/indexOutOfRange-sliceList.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceRange() throws Exception {
		compareResourceOMO("errors/indexOutOfRange-sliceRange.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceText() throws Exception {
		compareResourceOMO("errors/indexOutOfRange-sliceText.poc");
	}

	@Test
	public void testNullDict() throws Exception {
		compareResourceOMO("errors/nullDict.poc");
	}

	@Test
	public void testNullItem() throws Exception {
		compareResourceOMO("errors/nullItem.poc");
	}

	@Test
	public void testNullKey() throws Exception {
		compareResourceOMO("errors/nullKey.poc");
	}

	@Test
	public void testNullMember() throws Exception {
		compareResourceOMO("errors/nullMember.poc");
	}

	@Test
	public void testNullMethod() throws Exception {
		compareResourceOMO("errors/nullMethod.poc");
	}

	@Test
	public void testUserException() throws Exception {
		compareResourceOMO("errors/userException.poc");
	}

}

