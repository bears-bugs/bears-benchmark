package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestTuples extends BaseEParserTest {

	@Test
	public void testMultiAssignment() throws Exception {
		compareResourceEOE("tuples/multiAssignment.pec");
	}

	@Test
	public void testSingleAssignment() throws Exception {
		compareResourceEOE("tuples/singleAssignment.pec");
	}

	@Test
	public void testTupleElement() throws Exception {
		compareResourceEOE("tuples/tupleElement.pec");
	}

}

