package prompto.transpiler;

import prompto.declaration.AttributeDeclaration;
import prompto.runtime.Context;

public abstract class JSContext {

	static ThreadLocal<Context> context = new ThreadLocal<>();
	
	public static void set(Context instance) {
		context.set(instance);
	}
	
	public static Object findAttribute(String name) {
		AttributeDeclaration decl = context.get().findAttribute(name);
		if(decl==null)
			return null;
		else
			return new Attribute(decl);
	}
	
	public static class Attribute {
		
		AttributeDeclaration declaration;
		
		public Attribute(AttributeDeclaration declaration) {
			this.declaration = declaration;
		}
		
		public String getName() {
			return declaration.getName();
		}

		
	}

}
