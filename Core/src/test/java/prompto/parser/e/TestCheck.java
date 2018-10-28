package prompto.parser.e;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.declaration.DeclarationList;
import prompto.error.SyntaxError;



public class TestCheck extends BaseEParserTest {

	@Test
	public void testNativeAttribute() throws Exception {
		DeclarationList decls = parseString("define id as Integer attribute");
		decls.register(context);
		decls.check(context);
	}

	@Test
	public void testUndeclaredCategoryAttribute() throws Exception {
		DeclarationList decls = parseString("define person as Person attribute");
		decls.register(context);
		try {
			decls.check(context);
			fail("Should fail since Person is not declared !");
		} catch (SyntaxError e) {
			
		}
	}
	
	@Test
	public void testMethodAttribute() throws Exception {
		DeclarationList decls = parseString("define name as Text attribute\r\n" +
				"define printName as method receiving name doing:\r\n" +
				"\tprint with \"name\" + name as value\r\n" +
				"define Person as category with attribute printName");
		decls.register(context);
		try {
			decls.check(context);
			fail("Should fail since printName is not a category !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategoryAttribute() throws Exception {
		DeclarationList decls = parseString("define id as Integer attribute\r\n" +
				"define Person as category with attribute id\r\n" +
				"define person as Person attribute");
		decls.register(context);
		decls.check(context);
	}

	
	@Test
	public void testCategoryWithUndeclaredDerived() throws Exception {
		DeclarationList decls = parseString("define Employee as Person");
		try {
			decls.register(context);
			decls.check(context);
			fail("Should fail since Person not declared !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategoryWithUndeclaredAttribute() throws Exception {
		DeclarationList decls = parseString("define Person as category with attribute id");
		try {
			decls.register(context);
			decls.check(context);
			fail("Should fail since id not declared !");
		} catch (SyntaxError e) {
			
		}
	}

	@Test
	public void testCategory() throws Exception {
		DeclarationList decls = parseString("define id as Integer attribute\r\n" +
				"define Person as category with attribute id\r\n" +
				"define Employee as Person");
		decls.register(context);
		decls.check(context);
	}

	@Test
	public void testMethodWithUndeclaredAttribute() throws Exception {
		DeclarationList decls = parseString("define printName as method receiving name doing:\r\n" +
				"\tprint with \"name\" + name as value");
		try {
			decls.register(context);
			decls.check(context);
			fail("Should fail since name not declared !");
		} catch (SyntaxError e) {
			
		}
	}
	
	@Test
	public void testMethod() throws Exception {
		DeclarationList decls = parseString("define print as native method receiving Text value doing:\r\n" +
					"\tJava: System.out.println(value);\r\n" +
					"define name as Text attribute\r\n" +
					"define printName as method receiving name doing:\r\n" +
					"\tprint with \"name\" + name as value" );
		decls.register(context);
		decls.check(context);
	}

	@Test
	public void testList() throws Exception {
		DeclarationList decls = parseString("define testMethod as method receiving Text value doing:\r\n" +
					"\tlist = [ \"john\" , \"jim\" ]\r\n" +
					"\telem = list[1]\r\n");
		decls.register(context);
		decls.check(context);
	}

	@Test
	public void testDict() throws Exception {
		DeclarationList decls = parseString("define testMethod as method receiving Text value doing:\r\n" +
					"\tdict = < \"john\":123, \"jim\":345 >\r\n" +
					"\telem = dict[\"john\"]\r\n");
		decls.register(context);
		decls.check(context);
	}
	
	@Test
	public void testFetchOne() throws Exception {
		DeclarationList decls = parseString("define name as Text attribute\r\n" +
				"define Person as category with attribute name\r\n" +
				"define testMethod as method doing:\r\n" +
					"\ta = \"john\"\r\n" +
					"\tb = fetch one Person where name = a\r\n");
		decls.register(context);
		decls.check(context);
	}
	
	@Test
	public void testFetchAll() throws Exception {
		DeclarationList decls = parseString("define name as Text attribute\r\n" +
				"define Person as category with attribute name\r\n" +
				"define testMethod as method doing:\r\n" +
					"\ta = \"john\"\r\n" +
					"\tb = fetch all Person where name = a\r\n");
		decls.register(context);
		decls.check(context);
	}
	
	@Test
	public void testDocumentParam() throws Exception {
		DeclarationList decls = parseString("define testMethod as method receiving Document doc doing:\r\n" +
					"\tif \"stuff\" <> doc.action:\n" +
					"\t\ta = 0\n");
		decls.register(context);
		decls.check(context);
	}


}
