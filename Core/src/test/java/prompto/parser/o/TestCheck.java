package prompto.parser.o;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.declaration.DeclarationList;
import prompto.error.SyntaxError;



public class TestCheck extends BaseOParserTest {

	@Test
	public void testNativeAttribute() throws Exception {
		DeclarationList stmts = parseString("attribute id: Integer;");
		stmts.register(context);
		stmts.check(context);
	}

	@Test
	public void testUndeclaredCategoryAttribute() throws Exception {
		DeclarationList stmts = parseString("attribute person : Person;");
		stmts.register(context);
		try {
			stmts.check(context);
			fail("Should fail since Person is not declared !");
		} catch (SyntaxError e) {
			
		}
	}
	
	@Test
	public void testMethodAttribute() throws Exception {
		DeclarationList stmts = parseString("attribute name: Text;" +
				"method PrintName(name) { " +
				"print ( value = \"name\" + name ); }" +
				"category Person extends PrintName;");
		stmts.register(context);
		try {
			stmts.check(context);
			fail("Should fail since printName is not a category !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategoryAttribute() throws Exception {
		DeclarationList stmts = parseString("attribute id: Integer;" +
				"category Person(id);" +
				"attribute person: Person;");
		stmts.register(context);
		stmts.check(context);
	}

	
	@Test
	public void testCategoryWithUndeclaredDerived() throws Exception {
		DeclarationList stmts = parseString("category Employee extends Person;");
		try {
			stmts.register(context);
			stmts.check(context);
			fail("Should fail since Person not declared !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategoryWithUndeclaredAttribute() throws Exception {
		DeclarationList stmts = parseString("category Person(id);");
		try {
			stmts.register(context);
			stmts.check(context);
			fail("Should fail since id not declared !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategory() throws Exception {
		DeclarationList stmts = parseString("attribute id: Integer;" +
				"category Person(id);" +
				"category Employee extends Person;");
		stmts.register(context);
		stmts.check(context);
	}

	@Test
	public void testMethodWithUndeclaredAttribute() throws Exception {
		DeclarationList stmts = parseString("method printName(name) {" +
				"print (value = \"name\" + name ); }");
		try {
			stmts.register(context);
			stmts.check(context);
			fail("Should fail since name not declared !");
		} catch (SyntaxError e) {
			
		}
	}
	
	@Test
	public void testMethod() throws Exception {
		DeclarationList stmts = parseString("native method print( Text value) {" +
					"Java: System.out.println(value); }" +
					"attribute name: Text;" +
					"method printName(name ) {" +
					"print( value = \"name\" + name ); }" );
		stmts.register(context);
		stmts.check(context);
	}

	@Test
	public void testList() throws Exception {
		DeclarationList stmts = parseString("method testMethod (Text value) {" +
					"list = [ \"john\" , \"jim\" ];" +
					"elem = list[1]; }");
		stmts.register(context);
		stmts.check(context);
	}

	@Test
	public void testDict() throws Exception {
		DeclarationList stmts = parseString("method testMethod (Text value) {" +
					"dict = { \"john\":123, \"jim\":345 };" +
					"elem = dict[\"john\"]; }");
		stmts.register(context);
		stmts.check(context);
	}
}
