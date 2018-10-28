package prompto.transpiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.script.Invocable;

import org.junit.Test;

import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.parser.e.BaseEParserTest;
import prompto.runtime.Context;

public class TestInline extends BaseEParserTest {

	@Test
	public void inlinedAnnotationIsDetectedForCategory() throws Exception {
		DeclarationList decls = parseResource("samples/inlinedEvent.pec");
		IDeclaration decl = decls.getFirst();
		assertTrue(decl.hasLocalAnnotation("Inlined"));
		assertTrue(decl.hasLocalAnnotation("@Inlined"));
		assertFalse(decl.hasLocalAnnotation("Unrelated"));
		assertFalse(decl.hasLocalAnnotation("@Unrelated"));
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
