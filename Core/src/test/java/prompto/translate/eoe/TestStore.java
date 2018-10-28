package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestStore extends BaseEParserTest {

	@Test
	public void testDeleteRecords() throws Exception {
		compareResourceEOE("store/deleteRecords.pec");
	}

	@Test
	public void testFlush() throws Exception {
		compareResourceEOE("store/flush.pec");
	}

	@Test
	public void testListRecords() throws Exception {
		compareResourceEOE("store/listRecords.pec");
	}

	@Test
	public void testManyRecords() throws Exception {
		compareResourceEOE("store/manyRecords.pec");
	}

	@Test
	public void testManyUntypedRecords() throws Exception {
		compareResourceEOE("store/manyUntypedRecords.pec");
	}

	@Test
	public void testSimpleRecord() throws Exception {
		compareResourceEOE("store/simpleRecord.pec");
	}

	@Test
	public void testSlicedRecords() throws Exception {
		compareResourceEOE("store/slicedRecords.pec");
	}

	@Test
	public void testSortedRecords() throws Exception {
		compareResourceEOE("store/sortedRecords.pec");
	}

	@Test
	public void testSubRecord() throws Exception {
		compareResourceEOE("store/subRecord.pec");
	}

	@Test
	public void testUntypedRecord() throws Exception {
		compareResourceEOE("store/untypedRecord.pec");
	}

}

