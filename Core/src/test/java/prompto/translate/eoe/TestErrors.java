package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestErrors extends BaseEParserTest {

	@Test
	public void testDivideByZero() throws Exception {
		compareResourceEOE("errors/divideByZero.pec");
	}

	@Test
	public void testIndexOutOfRange_listItem() throws Exception {
		compareResourceEOE("errors/indexOutOfRange-listItem.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceList() throws Exception {
		compareResourceEOE("errors/indexOutOfRange-sliceList.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceRange() throws Exception {
		compareResourceEOE("errors/indexOutOfRange-sliceRange.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceText() throws Exception {
		compareResourceEOE("errors/indexOutOfRange-sliceText.pec");
	}

	@Test
	public void testNullDict() throws Exception {
		compareResourceEOE("errors/nullDict.pec");
	}

	@Test
	public void testNullItem() throws Exception {
		compareResourceEOE("errors/nullItem.pec");
	}

	@Test
	public void testNullKey() throws Exception {
		compareResourceEOE("errors/nullKey.pec");
	}

	@Test
	public void testNullMember() throws Exception {
		compareResourceEOE("errors/nullMember.pec");
	}

	@Test
	public void testNullMethod() throws Exception {
		compareResourceEOE("errors/nullMethod.pec");
	}

	@Test
	public void testUnexpected() throws Exception {
		compareResourceEOE("errors/unexpected.pec");
	}

	@Test
	public void testUserException() throws Exception {
		compareResourceEOE("errors/userException.pec");
	}

}

