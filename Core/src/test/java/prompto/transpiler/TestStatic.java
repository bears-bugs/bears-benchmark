package prompto.transpiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.script.Invocable;

import org.junit.Test;

import prompto.declaration.CategoryDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.parser.e.BaseEParserTest;
import prompto.runtime.Context;

public class TestStatic extends BaseEParserTest {

	@Test
	public void staticAnnotationIsDetected() throws Exception {
		DeclarationList decls = parseResource("samples/staticMethods.pec");
		decls.register(context);
		IDeclaration decl = decls.get(1);
		IDeclaration method = ((CategoryDeclaration)decl).getLocalMethods().getFirst();
		assertTrue(method.hasLocalAnnotation("Static"));
		assertTrue(method.hasLocalAnnotation("@Static"));
		assertFalse(method.hasLocalAnnotation("Unrelated"));
		assertFalse(method.hasLocalAnnotation("@Unrelated"));
		decl = decls.get(2);
		method = ((CategoryDeclaration)decl).getLocalMethods().getFirst();
		assertFalse(method.hasLocalAnnotation("Static"));
		assertFalse(method.hasLocalAnnotation("@Static"));
		assertTrue(method.hasInheritedAnnotation(context, "Static"));
		assertTrue(method.hasAnnotation(context, "@Static"));
	}

	@Test
	public void inlinedAnnotationIsDetectedForMembers() throws Exception {
		DeclarationList decls = parseResource("samples/inlinedEvent.pec");
		NativeCategoryDeclaration decl = (NativeCategoryDeclaration)decls.getFirst();
		decl.getLocalMethods().forEach(method->{
			assertTrue(method.containerHasLocalAnnotation("Inlined"));
			assertFalse(method.containerHasLocalAnnotation("Unrelated"));
		});
	}
	
	@Test
	public void inlinedCategoryIsNotTranspiled() throws Exception {
		DeclarationList decls = parseResource("samples/inlinedEvent.pec");
		Context context = Context.newGlobalContext();
		decls.register(context);
		Transpiler transpiler = new Transpiler(new Nashorn8Engine(), context);
		IMethodDeclaration method = (IMethodDeclaration)decls.get(1);
		method.declare(transpiler);
		assertFalse(transpiler.getDeclared().contains(decls.getFirst()));
	}

	@Test
	public void inlinedMethodCallIsInlined() throws Exception {
		DeclarationList decls = parseResource("samples/inlinedEvent.pec");
		Context context = Context.newGlobalContext();
		decls.register(context);
		Transpiler transpiler = new Transpiler(new Nashorn8Engine(), context);
		IMethodDeclaration method = (IMethodDeclaration)decls.get(1);
		method.declare(transpiler);
		Invocable invocable = Nashorn8Engine.transpile(transpiler);
		Object event = Collections.singletonMap("target", Collections.singletonMap( "defaultValue", "Hello!"));
		Object result = invocable.invokeFunction("eventHandler$TextEvent", event);
		assertEquals("Hello!", result);
	}
}
