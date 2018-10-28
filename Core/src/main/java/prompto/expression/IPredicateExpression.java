package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.IQueryBuilder;

public interface IPredicateExpression extends IExpression {

	void interpretQuery(Context context, IQueryBuilder query) throws PromptoError;
	default void compileQuery(Context context, MethodInfo method, Flags flags) {
		System.err.println("Need to implement compileQuery for " + this.getClass().getName());
		throw new UnsupportedOperationException();
	}


}
