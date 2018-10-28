package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestExit extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedAssignedReturn() throws Exception {
		checkInterpretedOutput("exit/assignedReturn.pec");
	}

	@Test
	public void testCompiledAssignedReturn() throws Exception {
		checkCompiledOutput("exit/assignedReturn.pec");
	}

	@Test
	public void testTranspiledAssignedReturn() throws Exception {
		checkTranspiledOutput("exit/assignedReturn.pec");
	}

	@Test
	public void testInterpretedAssignedReturnInDoWhile() throws Exception {
		checkInterpretedOutput("exit/assignedReturnInDoWhile.pec");
	}

	@Test
	public void testCompiledAssignedReturnInDoWhile() throws Exception {
		checkCompiledOutput("exit/assignedReturnInDoWhile.pec");
	}

	@Test
	public void testTranspiledAssignedReturnInDoWhile() throws Exception {
		checkTranspiledOutput("exit/assignedReturnInDoWhile.pec");
	}

	@Test
	public void testInterpretedAssignedReturnInForEach() throws Exception {
		checkInterpretedOutput("exit/assignedReturnInForEach.pec");
	}

	@Test
	public void testCompiledAssignedReturnInForEach() throws Exception {
		checkCompiledOutput("exit/assignedReturnInForEach.pec");
	}

	@Test
	public void testTranspiledAssignedReturnInForEach() throws Exception {
		checkTranspiledOutput("exit/assignedReturnInForEach.pec");
	}

	@Test
	public void testInterpretedAssignedReturnInIf() throws Exception {
		checkInterpretedOutput("exit/assignedReturnInIf.pec");
	}

	@Test
	public void testCompiledAssignedReturnInIf() throws Exception {
		checkCompiledOutput("exit/assignedReturnInIf.pec");
	}

	@Test
	public void testTranspiledAssignedReturnInIf() throws Exception {
		checkTranspiledOutput("exit/assignedReturnInIf.pec");
	}

	@Test
	public void testInterpretedAssignedReturnInWhile() throws Exception {
		checkInterpretedOutput("exit/assignedReturnInWhile.pec");
	}

	@Test
	public void testCompiledAssignedReturnInWhile() throws Exception {
		checkCompiledOutput("exit/assignedReturnInWhile.pec");
	}

	@Test
	public void testTranspiledAssignedReturnInWhile() throws Exception {
		checkTranspiledOutput("exit/assignedReturnInWhile.pec");
	}

	@Test
	public void testInterpretedUnassignedReturn() throws Exception {
		checkInterpretedOutput("exit/unassignedReturn.pec");
	}

	@Test
	public void testCompiledUnassignedReturn() throws Exception {
		checkCompiledOutput("exit/unassignedReturn.pec");
	}

	@Test
	public void testTranspiledUnassignedReturn() throws Exception {
		checkTranspiledOutput("exit/unassignedReturn.pec");
	}

	@Test
	public void testInterpretedUnassignedReturnInDoWhile() throws Exception {
		checkInterpretedOutput("exit/unassignedReturnInDoWhile.pec");
	}

	@Test
	public void testCompiledUnassignedReturnInDoWhile() throws Exception {
		checkCompiledOutput("exit/unassignedReturnInDoWhile.pec");
	}

	@Test
	public void testTranspiledUnassignedReturnInDoWhile() throws Exception {
		checkTranspiledOutput("exit/unassignedReturnInDoWhile.pec");
	}

	@Test
	public void testInterpretedUnassignedReturnInForEach() throws Exception {
		checkInterpretedOutput("exit/unassignedReturnInForEach.pec");
	}

	@Test
	public void testCompiledUnassignedReturnInForEach() throws Exception {
		checkCompiledOutput("exit/unassignedReturnInForEach.pec");
	}

	@Test
	public void testTranspiledUnassignedReturnInForEach() throws Exception {
		checkTranspiledOutput("exit/unassignedReturnInForEach.pec");
	}

	@Test
	public void testInterpretedUnassignedReturnInIf() throws Exception {
		checkInterpretedOutput("exit/unassignedReturnInIf.pec");
	}

	@Test
	public void testCompiledUnassignedReturnInIf() throws Exception {
		checkCompiledOutput("exit/unassignedReturnInIf.pec");
	}

	@Test
	public void testTranspiledUnassignedReturnInIf() throws Exception {
		checkTranspiledOutput("exit/unassignedReturnInIf.pec");
	}

	@Test
	public void testInterpretedUnassignedReturnInWhile() throws Exception {
		checkInterpretedOutput("exit/unassignedReturnInWhile.pec");
	}

	@Test
	public void testCompiledUnassignedReturnInWhile() throws Exception {
		checkCompiledOutput("exit/unassignedReturnInWhile.pec");
	}

	@Test
	public void testTranspiledUnassignedReturnInWhile() throws Exception {
		checkTranspiledOutput("exit/unassignedReturnInWhile.pec");
	}

}

