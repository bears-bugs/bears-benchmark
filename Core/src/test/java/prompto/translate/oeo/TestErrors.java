package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestErrors extends BaseOParserTest {

	@Test
	public void testDivideByZero() throws Exception {
		compareResourceOEO("errors/divideByZero.poc");
	}

	@Test
	public void testIndexOutOfRange_listItem() throws Exception {
		compareResourceOEO("errors/indexOutOfRange-listItem.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceList() throws Exception {
		compareResourceOEO("errors/indexOutOfRange-sliceList.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceRange() throws Exception {
		compareResourceOEO("errors/indexOutOfRange-sliceRange.poc");
	}

	@Test
	public void testIndexOutOfRange_sliceText() throws Exception {
		compareResourceOEO("errors/indexOutOfRange-sliceText.poc");
	}

	@Test
	public void testNullDict() throws Exception {
		compareResourceOEO("errors/nullDict.poc");
	}

	@Test
	public void testNullItem() throws Exception {
		compareResourceOEO("errors/nullItem.poc");
	}

	@Test
	public void testNullKey() throws Exception {
		compareResourceOEO("errors/nullKey.poc");
	}

	@Test
	public void testNullMember() throws Exception {
		compareResourceOEO("errors/nullMember.poc");
	}

	@Test
	public void testNullMethod() throws Exception {
		compareResourceOEO("errors/nullMethod.poc");
	}

	@Test
	public void testUserException() throws Exception {
		compareResourceOEO("errors/userException.poc");
	}

}

