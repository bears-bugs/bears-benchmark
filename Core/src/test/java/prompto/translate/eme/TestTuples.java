package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestTuples extends BaseEParserTest {

	@Test
	public void testMultiAssignment() throws Exception {
		compareResourceEME("tuples/multiAssignment.pec");
	}

	@Test
	public void testSingleAssignment() throws Exception {
		compareResourceEME("tuples/singleAssignment.pec");
	}

	@Test
	public void testTupleElement() throws Exception {
		compareResourceEME("tuples/tupleElement.pec");
	}

}

