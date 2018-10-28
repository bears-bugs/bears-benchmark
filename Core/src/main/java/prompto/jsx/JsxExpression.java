package prompto.jsx;

import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public class JsxExpression implements IJsxValue, IJsxExpression {

	IExpression expression;
	
	public JsxExpression(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public IType check(Context context) {
		return expression.check(context);
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("{");
		expression.toDialect(writer);
		writer.append("}");
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.expression.declare(transpiler);
	}
	
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		this.expression.transpile(transpiler);
		return false;
	}

}
