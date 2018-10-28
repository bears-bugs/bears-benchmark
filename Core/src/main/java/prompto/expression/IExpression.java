package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

/* an IExpression is the result of parsing a piece of Prompto code
 * every expression requires :
 *  - to be checked for syntax verification purpose
 *  - to be interpreted at runtime, which results in a value
 * 	- to be translated into any dialect
 */
public interface IExpression {
	
	IType check(Context context);
	IValue interpret(Context context) throws PromptoError;
	void toDialect(CodeWriter writer);

	default ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		throw new UnsupportedOperationException("compile " + this.getClass().getName());
	}
	default void declare(Transpiler transpiler) {
		throw new UnsupportedOperationException("declare " + this.getClass().getName());
	}
	default boolean transpile(Transpiler transpiler) {
		throw new UnsupportedOperationException("transpile " + this.getClass().getName());
	}
	default void declareQuery(Transpiler transpiler) {
		throw new UnsupportedOperationException("declareQuery " + this.getClass().getName());
	}
	default void transpileQuery(Transpiler transpiler, String builderName) {
		throw new UnsupportedOperationException("transpileQuery " + this.getClass().getName());
	}
	default void transpileFound(Transpiler transpiler, Dialect dialect) {
		throw new UnsupportedOperationException("transpileFound " + this.getClass().getName());
	}
	
}
