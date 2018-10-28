package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestStore extends BaseEParserTest {

	@Test
	public void testDeleteRecords() throws Exception {
		compareResourceEME("store/deleteRecords.pec");
	}

	@Test
	public void testFlush() throws Exception {
		compareResourceEME("store/flush.pec");
	}

	@Test
	public void testListRecords() throws Exception {
		compareResourceEME("store/listRecords.pec");
	}

	@Test
	public void testManyRecords() throws Exception {
		compareResourceEME("store/manyRecords.pec");
	}

	@Test
	public void testManyUntypedRecords() throws Exception {
		compareResourceEME("store/manyUntypedRecords.pec");
	}

	@Test
	public void testSimpleRecord() throws Exception {
		compareResourceEME("store/simpleRecord.pec");
	}

	@Test
	public void testSlicedRecords() throws Exception {
		compareResourceEME("store/slicedRecords.pec");
	}

	@Test
	public void testSortedRecords() throws Exception {
		compareResourceEME("store/sortedRecords.pec");
	}

	@Test
	public void testSubRecord() throws Exception {
		compareResourceEME("store/subRecord.pec");
	}

	@Test
	public void testUntypedRecord() throws Exception {
		compareResourceEME("store/untypedRecord.pec");
	}

}

