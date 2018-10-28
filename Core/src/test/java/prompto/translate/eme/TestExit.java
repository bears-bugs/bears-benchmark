package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestExit extends BaseEParserTest {

	@Test
	public void testAssignedReturn() throws Exception {
		compareResourceEME("exit/assignedReturn.pec");
	}

	@Test
	public void testAssignedReturnInDoWhile() throws Exception {
		compareResourceEME("exit/assignedReturnInDoWhile.pec");
	}

	@Test
	public void testAssignedReturnInForEach() throws Exception {
		compareResourceEME("exit/assignedReturnInForEach.pec");
	}

	@Test
	public void testAssignedReturnInIf() throws Exception {
		compareResourceEME("exit/assignedReturnInIf.pec");
	}

	@Test
	public void testAssignedReturnInWhile() throws Exception {
		compareResourceEME("exit/assignedReturnInWhile.pec");
	}

	@Test
	public void testUnassignedReturn() throws Exception {
		compareResourceEME("exit/unassignedReturn.pec");
	}

	@Test
	public void testUnassignedReturnInDoWhile() throws Exception {
		compareResourceEME("exit/unassignedReturnInDoWhile.pec");
	}

	@Test
	public void testUnassignedReturnInForEach() throws Exception {
		compareResourceEME("exit/unassignedReturnInForEach.pec");
	}

	@Test
	public void testUnassignedReturnInIf() throws Exception {
		compareResourceEME("exit/unassignedReturnInIf.pec");
	}

	@Test
	public void testUnassignedReturnInWhile() throws Exception {
		compareResourceEME("exit/unassignedReturnInWhile.pec");
	}

}

