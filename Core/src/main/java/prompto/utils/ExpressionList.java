package prompto.utils;

import prompto.expression.IExpression;
import prompto.transpiler.Transpiler;

@SuppressWarnings("serial")
public class ExpressionList extends ObjectList<IExpression>{

	public ExpressionList() {
	}
	
	public ExpressionList(IExpression item) {
		this.add(item);
	}

	public void toDialect(CodeWriter writer) {
		if(this.size()>0) {
			for(IExpression exp : this) {
				exp.toDialect(writer);
				writer.append(", ");
			}
			writer.trimLast(2);
		}
	}

	public void declare(Transpiler transpiler) {
		this.forEach(exp->exp.declare(transpiler));
	}

	public void transpile(Transpiler transpiler) {
		if(this.size()>0) {
			this.forEach(exp->{
				exp.transpile(transpiler);
				transpiler.append(", ");
			});
			transpiler.trimLast(2);
		}
	}

}
