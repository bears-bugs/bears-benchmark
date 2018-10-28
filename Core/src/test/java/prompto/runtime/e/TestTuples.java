package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestTuples extends BaseEParserTest {

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
		checkInterpretedOutput("tuples/multiAssignment.pec");
	}

	@Test
	public void testCompiledMultiAssignment() throws Exception {
		checkCompiledOutput("tuples/multiAssignment.pec");
	}

	@Test
	public void testTranspiledMultiAssignment() throws Exception {
		checkTranspiledOutput("tuples/multiAssignment.pec");
	}

	@Test
	public void testInterpretedSingleAssignment() throws Exception {
		checkInterpretedOutput("tuples/singleAssignment.pec");
	}

	@Test
	public void testCompiledSingleAssignment() throws Exception {
		checkCompiledOutput("tuples/singleAssignment.pec");
	}

	@Test
	public void testTranspiledSingleAssignment() throws Exception {
		checkTranspiledOutput("tuples/singleAssignment.pec");
	}

	@Test
	public void testInterpretedTupleElement() throws Exception {
		checkInterpretedOutput("tuples/tupleElement.pec");
	}

	@Test
	public void testCompiledTupleElement() throws Exception {
		checkCompiledOutput("tuples/tupleElement.pec");
	}

	@Test
	public void testTranspiledTupleElement() throws Exception {
		checkTranspiledOutput("tuples/tupleElement.pec");
	}

}

