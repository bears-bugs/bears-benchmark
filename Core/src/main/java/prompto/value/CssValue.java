package prompto.value;

import prompto.css.CssExpression;
import prompto.type.CssType;

public class CssValue extends BaseValue {

	CssExpression expression;
	
	public CssValue(CssExpression expression) {
		super(CssType.instance());
		this.expression = expression;
	}

}
