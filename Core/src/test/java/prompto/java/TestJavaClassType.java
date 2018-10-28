package prompto.java;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.type.ListType;

public class TestJavaClassType {

	public List<AttributeDeclaration> getAllAttributes() {
		return null;
	}
	
	@Test
	public void testAttributeList() throws Exception {
		Method method = TestJavaClassType.class.getMethod("getAllAttributes");
		assertNotNull(method);
		Type type = method.getGenericReturnType();
		assertNotNull(type);
		Context context = Context.newGlobalContext();
		Identifier name = new Identifier("Attribute");
		NativeCategoryDeclaration declaration = new NativeCategoryDeclaration(name, null, null, null, null);
		context.registerNativeBinding(AttributeDeclaration.class, declaration);
		IType listType = new ListType(new CategoryType(name));
		IType returnType = new JavaClassType(type).convertJavaClassToPromptoType(context, listType);
		assertEquals(listType, returnType);
	}

}
