package prompto.instance;

import prompto.compiler.Flags;
import prompto.compiler.ResultInfo;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public interface IAssignableInstance {

	IType check(Context context);
	IType checkAssignValue(Context context, IType valueType);
	IType checkAssignMember(Context context, Identifier name, IType valueType);
	IType checkAssignItem(Context context, IType itemType, IType valueType);
	void assign(Context context, IExpression expression) throws PromptoError;
	IValue interpret(Context context) throws PromptoError;
	void toDialect(CodeWriter writer, IExpression expression);
	default ResultInfo compileParent(Context context, MethodInfo method, Flags flags) {
		throw new UnsupportedOperationException("compileParent " + this.getClass().getName());
	}
	default ResultInfo compileAssign(Context context, MethodInfo method, Flags flags, IExpression expression) {
		throw new UnsupportedOperationException("compileAssign " + this.getClass().getName());
	}
	default void declare(Transpiler transpiler)  {
		throw new UnsupportedOperationException("declare " + this.getClass().getName());
	}
	default void transpile(Transpiler transpiler) {
		throw new UnsupportedOperationException("transpile " + this.getClass().getName());
	}
	default void declareAssign(Transpiler transpiler, IExpression expression) {
		throw new UnsupportedOperationException("declareAssign " + this.getClass().getName());
	}
	default void transpileAssign(Transpiler transpiler, IExpression expression) {
		throw new UnsupportedOperationException("transpileAssign " + this.getClass().getName());
	}
	default void transpileAssignParent(Transpiler transpiler) {
		throw new UnsupportedOperationException("transpileAssignParent " + this.getClass().getName());
	}
	

}
