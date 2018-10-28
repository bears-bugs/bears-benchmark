package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestTuples extends BaseOParserTest {

	@Test
	public void testMultiAssignment() throws Exception {
		compareResourceOMO("tuples/multiAssignment.poc");
	}

	@Test
	public void testSingleAssignment() throws Exception {
		compareResourceOMO("tuples/singleAssignment.poc");
	}

	@Test
	public void testTupleElement() throws Exception {
		compareResourceOMO("tuples/tupleElement.poc");
	}

}

