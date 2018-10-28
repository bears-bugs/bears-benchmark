package prompto.jsx;

import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public interface IJsxValue {

	IType check(Context context);


	default void toDialect(CodeWriter writer) {
		throw new UnsupportedOperationException("toDialect " + this.getClass().getName());
	}

	void declare(Transpiler transpiler);
	boolean transpile(Transpiler transpiler);

}
