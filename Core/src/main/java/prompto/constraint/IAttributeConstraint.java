package prompto.constraint;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.ITranspilable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public interface IAttributeConstraint extends ITranspilable {

	void checkValue(Context context, IValue value) throws PromptoError;
	void toDialect(CodeWriter writer);
	void compile(Context context, MethodInfo method, Flags flags);
	default void declare(Transpiler transpiler, String name, IType type) {
		throw new UnsupportedOperationException("declare " + this.getClass().getName());
	}

}
