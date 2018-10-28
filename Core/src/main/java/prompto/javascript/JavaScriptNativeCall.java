package prompto.javascript;

import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.statement.MethodCall;
import prompto.statement.NativeCall;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class JavaScriptNativeCall extends NativeCall {

	JavaScriptStatement statement;
	
	public JavaScriptNativeCall(JavaScriptStatement statement) {
		this.statement = statement;
	}
	
	public JavaScriptStatement getStatement() {
		return statement;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("JavaScript: ");
		statement.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		return VoidType.instance(); // TODO
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		throw new RuntimeException("Should never get there!");
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.statement.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		return this.statement.transpile(transpiler);
	}

	public void transpileInlineMethodCall(Transpiler transpiler, IMethodDeclaration declaration, MethodCall methodCall) {
		this.statement.transpileInlineMethodCall(transpiler, declaration, methodCall);
	}

}
