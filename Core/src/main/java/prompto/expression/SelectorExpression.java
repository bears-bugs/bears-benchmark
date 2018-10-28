package prompto.expression;

import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.type.IType;


public abstract class SelectorExpression extends Section implements IExpression {

	IExpression parent;
	
	public SelectorExpression() {
	}
	
	public SelectorExpression(IExpression parent) {
		this.parent = parent;
	}
	
	public IExpression getParent() {
		return parent;
	}
	
	public void setParent(IExpression parent) {
		this.parent = parent;
	}
	
	public IType checkParent(Context context) {
		if (parent instanceof UnresolvedIdentifier)
			return ((UnresolvedIdentifier)parent).checkMember(context);
		else
			return parent.check(context);
 	}

	public IExpression resolveParent(Context context) {
        if(parent instanceof UnresolvedIdentifier) {
        	((UnresolvedIdentifier) parent).checkMember(context);
        	return ((UnresolvedIdentifier) parent).getResolved();
        } else
        	return parent;
	}
	


}
