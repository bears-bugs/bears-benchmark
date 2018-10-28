package prompto.parser.e;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.declaration.DeclarationList;



public class TestParserFiles extends BaseEParserTest {

	@Test
	public void testEmpty() throws Exception {
		DeclarationList stmts = parseString("");
		assertNotNull(stmts);
		assertEquals(0,stmts.size());
	}
	
	@Test
	public void testNative() throws Exception {
		DeclarationList stmts = parseResource("native/method.pec");
		assertNotNull(stmts);
		assertEquals(2,stmts.size());
	}

	@Test
	public void testSpecified() throws Exception {
		DeclarationList stmts = parseResource("methods/specified.pec");
		assertNotNull(stmts);
		assertEquals(6,stmts.size());
	}

	@Test
	public void testAttribute() throws Exception {
		DeclarationList stmts = parseResource("methods/attribute.pec");
		assertNotNull(stmts);
		assertEquals(6,stmts.size());
	}

	@Test
	public void testImplicitMember() throws Exception {
		DeclarationList stmts = parseResource("methods/implicitMember.pec");
		assertNotNull(stmts);
		assertEquals(6,stmts.size());
	}
	
	@Test
	public void testPolymorphicImplicit() throws Exception {
		DeclarationList stmts = parseResource("methods/polymorphic_implicit.pec");
		assertNotNull(stmts);
		assertEquals(12,stmts.size());
	}
	
	@Test
	public void testEnumeratedCategory() throws Exception {
		DeclarationList stmts = parseResource("enums/categoryEnum.pec");
		assertNotNull(stmts);
		assertEquals(6,stmts.size());
	}

}
