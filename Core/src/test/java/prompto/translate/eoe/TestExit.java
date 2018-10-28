package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestExit extends BaseEParserTest {

	@Test
	public void testAssignedReturn() throws Exception {
		compareResourceEOE("exit/assignedReturn.pec");
	}

	@Test
	public void testAssignedReturnInDoWhile() throws Exception {
		compareResourceEOE("exit/assignedReturnInDoWhile.pec");
	}

	@Test
	public void testAssignedReturnInForEach() throws Exception {
		compareResourceEOE("exit/assignedReturnInForEach.pec");
	}

	@Test
	public void testAssignedReturnInIf() throws Exception {
		compareResourceEOE("exit/assignedReturnInIf.pec");
	}

	@Test
	public void testAssignedReturnInWhile() throws Exception {
		compareResourceEOE("exit/assignedReturnInWhile.pec");
	}

	@Test
	public void testUnassignedReturn() throws Exception {
		compareResourceEOE("exit/unassignedReturn.pec");
	}

	@Test
	public void testUnassignedReturnInDoWhile() throws Exception {
		compareResourceEOE("exit/unassignedReturnInDoWhile.pec");
	}

	@Test
	public void testUnassignedReturnInForEach() throws Exception {
		compareResourceEOE("exit/unassignedReturnInForEach.pec");
	}

	@Test
	public void testUnassignedReturnInIf() throws Exception {
		compareResourceEOE("exit/unassignedReturnInIf.pec");
	}

	@Test
	public void testUnassignedReturnInWhile() throws Exception {
		compareResourceEOE("exit/unassignedReturnInWhile.pec");
	}

}

