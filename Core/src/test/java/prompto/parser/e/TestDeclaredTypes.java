package prompto.parser.e;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import prompto.declaration.DeclarationList;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
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


 
public class TestDeclaredTypes extends BaseEParserTest {

	@Before
	public void registerCategoryTypes() throws Exception {
		DeclarationList stmts = parseString("define id as Integer attribute\r\n" +
				"define name as String attribute\r\n" +
				"define Root as category with attribute id\r\n" +
				"define Derived as Root with attribute name\r\n" +
				"define Unrelated as category with attributes id and name\r\n");
		stmts.register(context);
	}
	
	private boolean isAssignableTo(Context context, IType t1, IType t2) {
		return t2.isAssignableFrom(context, t1);
	}
	
	@Test
	public void testBooleanType() throws Exception {
		IType st = BooleanType.instance();
		assertEquals(st,BooleanType.instance());
		assertTrue(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance())); 
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test
	public void testIntegerType() throws Exception {
		IType st = IntegerType.instance();
		assertEquals(st,IntegerType.instance());
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertTrue(isAssignableTo(context, st, IntegerType.instance()));
		assertTrue(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test
	public void testDecimalType() throws Exception {
		IType st = DecimalType.instance();
		assertEquals(st,DecimalType.instance());
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertTrue(isAssignableTo(context, st, IntegerType.instance()));
		assertTrue(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}

	@Test
	public void testTextType() throws Exception {
		IType st = TextType.instance();
		assertEquals(st,TextType.instance());
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertTrue(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test
	public void testDateType() throws Exception {
		IType st = DateType.instance();
		assertEquals(st,DateType.instance());
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertTrue(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test 
	public void testDateTimeType() throws Exception {
		IType st = DateTimeType.instance();
		assertEquals(st,DateTimeType.instance());
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertTrue(isAssignableTo(context, st, DateType.instance()));
		assertTrue(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test
	public void testMissingType() throws Exception {
		IType st = MissingType.instance();
		assertEquals(st,MissingType.instance());
		assertTrue(st.isAssignableFrom(context, BooleanType.instance()));
		assertTrue(st.isAssignableFrom(context, IntegerType.instance()));
		assertTrue(st.isAssignableFrom(context, DecimalType.instance()));
		assertTrue(st.isAssignableFrom(context, TextType.instance()));
		assertTrue(st.isAssignableFrom(context, DateType.instance()));
		assertTrue(st.isAssignableFrom(context, DateTimeType.instance()));
		assertTrue(st.isAssignableFrom(context, MissingType.instance()));
		assertTrue(st.isAssignableFrom(context, AnyType.instance()));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Root"))));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Derived"))));
		assertTrue(st.isAssignableFrom(context, new CategoryType(new Identifier("Unrelated"))));
	}

	@Test
	public void testRootCategoryType() throws Exception {
		IType st = new CategoryType(new Identifier("Root"));
		assertEquals(st,new CategoryType(new Identifier("Root")));
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertTrue(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}

	@Test
	public void testDerivedCategoryType() throws Exception {
		IType st = new CategoryType(new Identifier("Derived"));
		assertEquals(st,new CategoryType(new Identifier("Derived")));
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertTrue(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertTrue(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}
	
	@Test
	public void testUnrelatedCategoryType() throws Exception {
		IType st = new CategoryType(new Identifier("Unrelated"));
		assertEquals(st,new CategoryType(new Identifier("Unrelated")));
		assertFalse(isAssignableTo(context, st, BooleanType.instance()));
		assertFalse(isAssignableTo(context, st, IntegerType.instance()));
		assertFalse(isAssignableTo(context, st, DecimalType.instance()));
		assertFalse(isAssignableTo(context, st, TextType.instance()));
		assertFalse(isAssignableTo(context, st, DateType.instance()));
		assertFalse(isAssignableTo(context, st, DateTimeType.instance()));
		assertTrue(isAssignableTo(context, st, MissingType.instance()));
		assertTrue(isAssignableTo(context, st, AnyType.instance()));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Root"))));
		assertFalse(isAssignableTo(context, st, new CategoryType(new Identifier("Derived"))));
		assertTrue(isAssignableTo(context, st, new CategoryType(new Identifier("Unrelated"))));
	}

}
