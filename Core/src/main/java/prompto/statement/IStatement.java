package prompto.statement;

import prompto.expression.IExpression;
import prompto.parser.ISection;
import prompto.transpiler.Transpiler;

public interface IStatement extends IExpression, ISection {

	default boolean canReturn() {
		return false;
	}

	default void declare(Transpiler transpiler) { throw new UnsupportedOperationException("declare " + this.getClass().getName()); }
	default boolean transpile(Transpiler transpiler)  { throw new UnsupportedOperationException("transpile " + this.getClass().getName()); }

}
