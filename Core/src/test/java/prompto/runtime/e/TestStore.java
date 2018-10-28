package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestStore extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedDeleteRecords() throws Exception {
		checkInterpretedOutput("store/deleteRecords.pec");
	}

	@Test
	public void testCompiledDeleteRecords() throws Exception {
		checkCompiledOutput("store/deleteRecords.pec");
	}

	@Test
	public void testTranspiledDeleteRecords() throws Exception {
		checkTranspiledOutput("store/deleteRecords.pec");
	}

	@Test
	public void testInterpretedFlush() throws Exception {
		checkInterpretedOutput("store/flush.pec");
	}

	@Test
	public void testCompiledFlush() throws Exception {
		checkCompiledOutput("store/flush.pec");
	}

	@Test
	public void testTranspiledFlush() throws Exception {
		checkTranspiledOutput("store/flush.pec");
	}

	@Test
	public void testInterpretedListRecords() throws Exception {
		checkInterpretedOutput("store/listRecords.pec");
	}

	@Test
	public void testCompiledListRecords() throws Exception {
		checkCompiledOutput("store/listRecords.pec");
	}

	@Test
	public void testTranspiledListRecords() throws Exception {
		checkTranspiledOutput("store/listRecords.pec");
	}

	@Test
	public void testInterpretedManyRecords() throws Exception {
		checkInterpretedOutput("store/manyRecords.pec");
	}

	@Test
	public void testCompiledManyRecords() throws Exception {
		checkCompiledOutput("store/manyRecords.pec");
	}

	@Test
	public void testTranspiledManyRecords() throws Exception {
		checkTranspiledOutput("store/manyRecords.pec");
	}

	@Test
	public void testInterpretedManyUntypedRecords() throws Exception {
		checkInterpretedOutput("store/manyUntypedRecords.pec");
	}

	@Test
	public void testCompiledManyUntypedRecords() throws Exception {
		checkCompiledOutput("store/manyUntypedRecords.pec");
	}

	@Test
	public void testTranspiledManyUntypedRecords() throws Exception {
		checkTranspiledOutput("store/manyUntypedRecords.pec");
	}

	@Test
	public void testInterpretedSimpleRecord() throws Exception {
		checkInterpretedOutput("store/simpleRecord.pec");
	}

	@Test
	public void testCompiledSimpleRecord() throws Exception {
		checkCompiledOutput("store/simpleRecord.pec");
	}

	@Test
	public void testTranspiledSimpleRecord() throws Exception {
		checkTranspiledOutput("store/simpleRecord.pec");
	}

	@Test
	public void testInterpretedSlicedRecords() throws Exception {
		checkInterpretedOutput("store/slicedRecords.pec");
	}

	@Test
	public void testCompiledSlicedRecords() throws Exception {
		checkCompiledOutput("store/slicedRecords.pec");
	}

	@Test
	public void testTranspiledSlicedRecords() throws Exception {
		checkTranspiledOutput("store/slicedRecords.pec");
	}

	@Test
	public void testInterpretedSortedRecords() throws Exception {
		checkInterpretedOutput("store/sortedRecords.pec");
	}

	@Test
	public void testCompiledSortedRecords() throws Exception {
		checkCompiledOutput("store/sortedRecords.pec");
	}

	@Test
	public void testTranspiledSortedRecords() throws Exception {
		checkTranspiledOutput("store/sortedRecords.pec");
	}

	@Test
	public void testInterpretedSubRecord() throws Exception {
		checkInterpretedOutput("store/subRecord.pec");
	}

	@Test
	public void testCompiledSubRecord() throws Exception {
		checkCompiledOutput("store/subRecord.pec");
	}

	@Test
	public void testTranspiledSubRecord() throws Exception {
		checkTranspiledOutput("store/subRecord.pec");
	}

	@Test
	public void testInterpretedUntypedRecord() throws Exception {
		checkInterpretedOutput("store/untypedRecord.pec");
	}

	@Test
	public void testCompiledUntypedRecord() throws Exception {
		checkCompiledOutput("store/untypedRecord.pec");
	}

	@Test
	public void testTranspiledUntypedRecord() throws Exception {
		checkTranspiledOutput("store/untypedRecord.pec");
	}

}

