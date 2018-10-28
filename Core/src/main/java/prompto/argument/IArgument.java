package prompto.argument;

import java.lang.reflect.Type;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.expression.DefaultExpression;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.INamed;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.value.IValue;


public interface IArgument extends INamed {
	
	String getProto();
	String getSignature(Dialect dialect);
	void register(Context context);
	void check(Context context);
	IValue checkValue(Context context,IExpression value) throws PromptoError;
	void toDialect(CodeWriter writer);
	DefaultExpression getDefaultExpression();
	boolean setMutable(boolean set);
	boolean isMutable();
	Type getJavaType(Context context);
	StackLocal registerLocal(Context context, MethodInfo method, Flags flags);
	default void extractLocal(Context context, MethodInfo method, Flags flags) {}
	void compileAssignment(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments, boolean isFirst);
	default void declare(Transpiler transpiler) { throw new UnsupportedOperationException("declare " + this.getClass().getName()); }
	default void transpile(Transpiler transpiler) { throw new UnsupportedOperationException("transpile " + this.getClass().getName()); }
	default String getTranspiledName(Context context) { throw new UnsupportedOperationException("getTranspiledName " + this.getClass().getName()); }
	default void transpileCall(Transpiler transpiler, IExpression expression) { throw new UnsupportedOperationException("transpileCall " + this.getClass().getName()); }
	
}
