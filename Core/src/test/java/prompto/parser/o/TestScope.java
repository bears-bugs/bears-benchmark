package prompto.parser.o;

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


public class TestScope extends BaseOParserTest {

	@Test(expected=SyntaxError.class)
	public void testAttribute() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("id")));
		DeclarationList stmts = parseString("attribute id: Integer; ");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("id"));
		assertNotNull(actual);
		assertTrue(actual instanceof AttributeDeclaration);
		stmts = parseString("attribute id: Integer; ");
		stmts.register(context);
	}
	
	@Test(expected=SyntaxError.class)
	public void testCategory() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("Person")));
		DeclarationList stmts = parseString("category Person(id, name);");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("Person"));
		assertNotNull(actual);
		assertTrue(actual instanceof CategoryDeclaration);
		stmts = parseString("category Person(id, name);");
		stmts.register(context);
	}
	
	@Test
	public void testMethod() throws Exception {
		assertNull(context.getRegisteredDeclaration(IDeclaration.class, new Identifier("printName")));
		DeclarationList stmts = parseString("attribute name: Text;"
				+ "method printName( name ) {"
				+ "print (value=name); }");
		assertNotNull(stmts);
		stmts.register(context);
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, new Identifier("printName"));
		assertNotNull(actual);
		assertTrue(actual instanceof MethodDeclarationMap);
		stmts = parseString("method printName (Person p ) {"
				+ "print (value = \"person\" + p.name ); } ");
		assertNotNull(stmts);
		stmts.register(context);
	}
}
