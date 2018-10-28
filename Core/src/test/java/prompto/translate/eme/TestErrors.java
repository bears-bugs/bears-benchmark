package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestErrors extends BaseEParserTest {

	@Test
	public void testDivideByZero() throws Exception {
		compareResourceEME("errors/divideByZero.pec");
	}

	@Test
	public void testIndexOutOfRange_listItem() throws Exception {
		compareResourceEME("errors/indexOutOfRange-listItem.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceList() throws Exception {
		compareResourceEME("errors/indexOutOfRange-sliceList.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceRange() throws Exception {
		compareResourceEME("errors/indexOutOfRange-sliceRange.pec");
	}

	@Test
	public void testIndexOutOfRange_sliceText() throws Exception {
		compareResourceEME("errors/indexOutOfRange-sliceText.pec");
	}

	@Test
	public void testNullDict() throws Exception {
		compareResourceEME("errors/nullDict.pec");
	}

	@Test
	public void testNullItem() throws Exception {
		compareResourceEME("errors/nullItem.pec");
	}

	@Test
	public void testNullKey() throws Exception {
		compareResourceEME("errors/nullKey.pec");
	}

	@Test
	public void testNullMember() throws Exception {
		compareResourceEME("errors/nullMember.pec");
	}

	@Test
	public void testNullMethod() throws Exception {
		compareResourceEME("errors/nullMethod.pec");
	}

	@Test
	public void testUnexpected() throws Exception {
		compareResourceEME("errors/unexpected.pec");
	}

	@Test
	public void testUserException() throws Exception {
		compareResourceEME("errors/userException.pec");
	}

}

