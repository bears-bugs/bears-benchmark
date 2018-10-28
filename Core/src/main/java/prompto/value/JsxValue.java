package prompto.value;

import prompto.jsx.IJsxExpression;
import prompto.type.JsxType;

public class JsxValue extends BaseValue {

	IJsxExpression expression;
	
	public JsxValue(IJsxExpression expression) {
		super(JsxType.instance());
		this.expression = expression;
	}

}
