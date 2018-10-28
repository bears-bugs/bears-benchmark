package prompto.value;

import java.lang.reflect.Type;

import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.type.MethodType;

public class ClosureValue extends BaseValue {
	
	Context context;
	
	public ClosureValue(Context context, MethodType type) {
		super(type);
		this.context = context;
	}
	
	@Override
	public Object convertTo(Context context, Type type) {
		if(type==IMethodDeclaration.class)
			return getMethod();
		else
			return super.convertTo(context, type);
	}
	
	private IMethodDeclaration getMethod() {
		return ((MethodType)type).getMethod();
	}
	
	public IValue interpret(Context context) throws PromptoError {
		IMethodDeclaration declaration = getMethod();
		this.context.enterMethod(declaration);
		try {
			Context parentMost = this.context.getParentMostContext();
			parentMost.setParentContext(context);
			IValue result = declaration.interpret(this.context);
			parentMost.setParentContext(null);
			return result;
		} finally {
			this.context.leaveMethod(declaration);
		}
	}

	public Identifier getName() {
		return getMethod().getId();
	}

	public ArgumentList getArguments() {
		return getMethod().getArguments();
	}

	public IType getReturnType() {
		return getMethod().getReturnType();
	}

}
