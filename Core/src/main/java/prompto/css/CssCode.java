package prompto.css;

import prompto.expression.IExpression;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class CssCode implements ICssValue {

	IExpression expression;
	
	public CssCode(IExpression expression) {
		this.expression = expression;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("{");
		this.expression.toDialect(writer);
		writer.append("}");
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.expression.declare(transpiler);
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		this.expression.transpile(transpiler);
	}

}
