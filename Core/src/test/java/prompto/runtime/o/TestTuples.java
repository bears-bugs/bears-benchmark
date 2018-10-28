package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestTuples extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedMultiAssignment() throws Exception {
		checkInterpretedOutput("tuples/multiAssignment.poc");
	}

	@Test
	public void testCompiledMultiAssignment() throws Exception {
		checkCompiledOutput("tuples/multiAssignment.poc");
	}

	@Test
	public void testTranspiledMultiAssignment() throws Exception {
		checkTranspiledOutput("tuples/multiAssignment.poc");
	}

	@Test
	public void testInterpretedSingleAssignment() throws Exception {
		checkInterpretedOutput("tuples/singleAssignment.poc");
	}

	@Test
	public void testCompiledSingleAssignment() throws Exception {
		checkCompiledOutput("tuples/singleAssignment.poc");
	}

	@Test
	public void testTranspiledSingleAssignment() throws Exception {
		checkTranspiledOutput("tuples/singleAssignment.poc");
	}

	@Test
	public void testInterpretedTupleElement() throws Exception {
		checkInterpretedOutput("tuples/tupleElement.poc");
	}

	@Test
	public void testCompiledTupleElement() throws Exception {
		checkCompiledOutput("tuples/tupleElement.poc");
	}

	@Test
	public void testTranspiledTupleElement() throws Exception {
		checkTranspiledOutput("tuples/tupleElement.poc");
	}

}

