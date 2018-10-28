package prompto.java;

import prompto.compiler.ResultInfo;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public interface JavaExpression extends ISection {
	
	IType check(Context context);
	Object interpret(Context context) throws PromptoError;
	void toDialect(CodeWriter writer);
	
	default ResultInfo compile(Context context, MethodInfo method) {
		System.err.println("Need to implement compile for " + this.getClass().getName());
		throw new UnsupportedOperationException();
	}
	
}
