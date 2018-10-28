package prompto.error;

import java.io.ByteArrayInputStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import prompto.declaration.DeclarationList;
import prompto.parser.Dialect;
import prompto.parser.IParser;
import prompto.problem.IProblem;
import prompto.problem.ProblemCollector;
import prompto.runtime.Context;

public class TestErrorListener {

	@Test
	public void testNoError() throws Exception {
		checkProblem("define x as Text attribute", 0, null);
	}

	@Test
	public void testIllegalToken() throws Exception {
		IProblem error = checkProblem("'%abc", 2, "Unrecognized character sequence:");
		assertEquals(0, error.getStartIndex());
	}

	@Test
	public void testExtraToken() throws Exception {
		IProblem error = checkProblem("abc", 1, "Unwanted token:");
		assertEquals(0, error.getStartIndex());
	}
	
	@Ignore 
	@Test
	public void testMissingToken() throws Exception {
		IProblem error = checkProblem("define x Text attribute", 1, "Missing token:"); // missing 'as'
		assertEquals(9, error.getStartIndex());
	}

	@Ignore
	@Test
	public void testMissingToken2() throws Exception {
		IProblem error = checkProblem("define as Text attribute", 1, "Missing token:"); // missing identifier
		assertEquals(7, error.getStartIndex());
	}

	@Test
	public void testInvalidSyntaxError() throws Exception {
		IProblem error = checkProblem("define", 1, "Invalid syntax at: define<EOF>"); 
		assertEquals(6, error.getStartIndex());
	}
	
	@Test
	public void testDuplicateError() throws Exception {
		checkProblem("define x as Text attribute\n"
				+ "define x as Text attribute\n", 1, "Duplicate declaration: x"); 
	}
	
	@Test
	public void testUnknownIdentifierInAssignmentError() throws Exception {
		checkProblem("define m as method doing:\n"
				+ "\tl = x\n", 1, "Unknown identifier: x"); 
	}

	@Test
	public void testUnknownIdentifierInExpressionError() throws Exception {
		checkProblem("define \"m\" as test method doing:\n"
				+ "\tl = x\n"
				+ "and verifying:\n"
				+ "\tl.length >= 3", 2, "Unknown identifier: x"); 
	}

	
	@Test
	public void testUnknownBindingClassError() throws Exception {
		checkProblem("define m as native method returning Text doing:\n"
				+ "\tJava: return missing.Klass.MyMethod();\n"
				, 2, "Unknown identifier: missing.Klass"); 
	}
	
	
	@Test
	public void testUnknownMethodError() throws Exception {
		checkProblem("define main as method receiving Text<:> args doing:\n"
				+ "\tprintLine \"Hello app\"\n"
				, 1, "Unknown method: printLine");
	}


	private IProblem checkProblem(String prompto, int count, String firstError) throws Exception {
		IProblem error = null;
		ProblemCollector listener = new ProblemCollector();
		IParser parser = Dialect.E.getParserFactory().newParser();
		parser.setProblemListener(listener);
		DeclarationList decls = parser.parse(null, new ByteArrayInputStream(prompto.getBytes()));
		Context context = Context.newGlobalContext();
		context.setProblemListener(listener);
		decls.register(context);
		decls.check(context);
		assertEquals(count, listener.getCount());
		if(count>0) {
			error = listener.getProblems().iterator().next();
			String errorMsg = error.getMessage();
			if(errorMsg.length()>firstError.length())
				errorMsg = errorMsg.substring(0, firstError.length());
			assertEquals(firstError, errorMsg);
		}
		return error;
	}


}
