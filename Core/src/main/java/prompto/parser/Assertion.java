package prompto.parser;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.expression.EqualsExpression;
import prompto.expression.IAssertion;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.BooleanType;
import prompto.type.IType;
import prompto.utils.CodeWriter;

/* the purpose of this class is simply to link an expression with a section */
public class Assertion extends Section {

	IExpression expression;
	
	public Assertion(IExpression expression) {
		this.expression = expression;
	}

	public void toDialect(CodeWriter writer) {
		expression.toDialect(writer);
	}

	public Context check(Context context) {
		IType type = expression.check(context);
		if(type!=BooleanType.instance())
			context.getProblemListener().reportIllegalNonBoolean(this, type);
		// need to optionally auto-downcast
		if(expression instanceof EqualsExpression) 
			context = ((EqualsExpression)expression).downCastForCheck(context);
		return context;
	}

	public boolean interpret(Context context, TestMethodDeclaration test) throws PromptoError {
		return ((IAssertion)expression).interpretAssert(context, test);
	}

	public void compile(Context context, MethodInfo method, Flags flags, TestMethodDeclaration test) {
		((IAssertion)expression).compileAssert(context, method, flags, test);
	}

	public void declare(Transpiler transpiler) {
		expression.declare(transpiler);
	}

	public void transpile(Transpiler transpiler) {
		expression.transpile(transpiler);
	}

	public String getExpected(Context context, Dialect dialect) {
		CodeWriter writer = new CodeWriter(dialect, context);
		expression.toDialect(writer);
		return writer.toString();
	}

	public void transpileFound(Transpiler transpiler, Dialect dialect) {
		expression.transpileFound(transpiler, dialect);
	}

}
