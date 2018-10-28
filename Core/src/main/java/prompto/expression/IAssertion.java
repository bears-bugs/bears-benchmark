package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.declaration.TestMethodDeclaration;
import prompto.runtime.Context;

public interface IAssertion {

	boolean interpretAssert(Context context, TestMethodDeclaration testMethodDeclaration);
	default void compileAssert(Context context, MethodInfo method, Flags flags, TestMethodDeclaration test) {
		throw new UnsupportedOperationException("Missing compileAssert for " + this.getClass().getName());
	}

}
