package prompto.parser.e;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.runtime.Context.MethodDeclarationMap;


public class TestScope extends BaseEParserTest {

	@Test(expected=SyntaxError.class)
	public void testAttribute() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("id")));
		DeclarationList stmts = parseString("define id as Integer attribute");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("id"));
		assertNotNull(actual);
		assertTrue(actual instanceof AttributeDeclaration);
		stmts = parseString("define id as Integer attribute");
		stmts.register(context);
	}
	
	@Test(expected=SyntaxError.class)
	public void testCategory() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("Person")));
		DeclarationList stmts = parseString("define Person as category with attributes id and name");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("Person"));
		assertNotNull(actual);
		assertTrue(actual instanceof CategoryDeclaration);
		stmts = parseString("define Person as category with attributes id and name");
		stmts.register(context);
	}
	
	@Test
	public void testMethod() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("printName")));
		DeclarationList stmts = parseString("define name as Text attribute\r\n"
				+ "define printName as method receiving name doing:\r\n"
				+ "\tprint with \"name\" + name as value");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("printName"));
		assertNotNull(actual);
		assertTrue(actual instanceof MethodDeclarationMap);
		stmts = parseString("define printName as method receiving Person p doing:"
				+ "\r\n\tprint with \"person\" + p.name as value");
		assertNotNull(stmts);
		stmts.register(context);
	}
}
