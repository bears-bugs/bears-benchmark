package prompto.parser.e;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import prompto.argument.CategoryArgument;
import prompto.argument.ExtendedArgument;
import prompto.argument.IArgument;
import prompto.declaration.DeclarationList;
import prompto.grammar.Identifier;
import prompto.type.AnyType;
import prompto.type.BooleanType;
import prompto.type.CategoryType;
import prompto.type.DateTimeType;
import prompto.type.DateType;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.MissingType;
import prompto.type.TextType;
import prompto.utils.IdentifierList;



public class TestAnonymousTypes extends BaseEParserTest { 

	@Before
	public void register() throws Exception {
		DeclarationList stmts = parseString("define id as Integer attribute\r\n" +
				"define name as String attribute\r\n" +
				"define other as String attribute\r\n" +
				"define Simple as category with attribute name\r\n" +
				"define Root as category with attribute id\r\n" +
				"define DerivedWithOther as Root with attribute other\r\n" +
				"define DerivedWithName as Root with attribute name\r\n");
		stmts.register(context);
	}
	
	@Test
	public void testAnonymousAnyType() throws Exception {
		// any x
		IArgument argument = new CategoryArgument(AnyType.instance(), new Identifier("x"));
		argument.register(context);
		IType st = argument.getType(context);
		assertTrue(st instanceof AnyType);
		assertTrue(st.isAssignableFrom(context, BooleanType.instance()));
		assertTrue(st.isAssignableFrom(context, IntegerType.instance()));
		assertTrue(st.isAssignableFrom(context, DecimalType.instance()));
		assertTrue(st.isAssignableFrom(context, TextType.instance()));
		assertTrue(st.isAssignableFrom(context, DateType.instance()));
		assertTrue(st.isAssignableFrom(context, DateTimeType.instance()));
		assertTrue(st.isAssignableFrom(context, MissingType.instance())); // missing type always compatible
		assertTrue(st.isAssignableFrom(context, AnyType.instance())); 
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Simple"))));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Root"))));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithOther"))));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithName"))));
	}
	
	@Test
	public void testAnonymousAnyTypeWithAttribute() throws Exception {
		// any x with attribute: name
		IdentifierList list = new IdentifierList(new Identifier("name"));
		IArgument argument = new ExtendedArgument(AnyType.instance(), new Identifier("x"), list);
		argument.register(context);
		IType st = argument.getType(context);
		assertTrue(st instanceof CategoryType);
		assertFalse(st.isAssignableFrom(context, BooleanType.instance()));
		assertFalse(st.isAssignableFrom(context, IntegerType.instance()));
		assertFalse(st.isAssignableFrom(context, DecimalType.instance()));
		assertFalse(st.isAssignableFrom(context, TextType.instance()));
		assertFalse(st.isAssignableFrom(context, DateType.instance()));
		assertFalse(st.isAssignableFrom(context, DateTimeType.instance()));
		assertFalse(st.isAssignableFrom(context, MissingType.instance())); 
		assertFalse(st.isAssignableFrom(context, AnyType.instance())); // any type never compatible
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Simple")))); // since Simple has a name
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("Root")))); // since Root has no name
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithOther")))); // since DerivedWithOther has no name
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithName")))); // since DerivedWithName has a name
	}
	
	@Test
	public void testAnonymousCategoryType() throws Exception {
		// Root x
		IArgument argument = new CategoryArgument(new CategoryType(new Identifier("Root")), new Identifier("x"));
		argument.register(context);
		IType st = argument.getType(context);
		assertTrue(st instanceof CategoryType);
		assertFalse(st.isAssignableFrom(context, BooleanType.instance()));
		assertFalse(st.isAssignableFrom(context, IntegerType.instance()));
		assertFalse(st.isAssignableFrom(context, DecimalType.instance()));
		assertFalse(st.isAssignableFrom(context, TextType.instance()));
		assertFalse(st.isAssignableFrom(context, DateType.instance()));
		assertFalse(st.isAssignableFrom(context, DateTimeType.instance()));
		assertFalse(st.isAssignableFrom(context, MissingType.instance())); 
		assertFalse(st.isAssignableFrom(context, AnyType.instance())); // any type never compatible
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("Simple"))));  // since Simple does not extend Root
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Root")))); // since Root is Root
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithOther")))); // since DerivedWithOther extends Root
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithName")))); // since DerivedWithName extends Root
	}

	@Test
	public void testAnonymousCategoryTypeWithAttribute() throws Exception {
		// Root x with attribute: name
		IdentifierList list = new IdentifierList(new Identifier("name"));
		IArgument argument = new ExtendedArgument(new CategoryType(new Identifier("Root")), new Identifier("test"), list);
		argument.register(context);
		IType st = argument.getType(context);
		assertTrue(st instanceof CategoryType);
		assertFalse(st.isAssignableFrom(context, BooleanType.instance()));
		assertFalse(st.isAssignableFrom(context, IntegerType.instance()));
		assertFalse(st.isAssignableFrom(context, DecimalType.instance()));
		assertFalse(st.isAssignableFrom(context, TextType.instance()));
		assertFalse(st.isAssignableFrom(context, DateType.instance()));
		assertFalse(st.isAssignableFrom(context, DateTimeType.instance()));
		assertFalse(st.isAssignableFrom(context, MissingType.instance())); 
		assertFalse(st.isAssignableFrom(context, AnyType.instance())); // any type never compatible
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("Simple"))));  // since Simple does not extend Root
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("Root")))); // since Root has no name
		assertFalse(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithOther")))); // since DerivedWithOther has no name
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("DerivedWithName")))); // since DerivedWithName has a name
	}
	
}
