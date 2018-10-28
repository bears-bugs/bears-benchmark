package prompto.runtime;

import prompto.argument.IArgument;
import prompto.argument.ITypedArgument;
import prompto.declaration.IMethodDeclaration;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.type.DictType;
import prompto.type.IType;
import prompto.type.TextType;

public abstract class MethodLocator {

	public static IMethodDeclaration locateMethod(Context context, Identifier methodName, IExpression args) {
		MethodDeclarationMap map = context.getRegisteredDeclaration(MethodDeclarationMap.class, methodName);
		if(map==null)
			throw new SyntaxError("Could not find a \"" + methodName + "\" method.");
		return locateMethod(map, args!=null);
	}

	static IMethodDeclaration locateMethod(MethodDeclarationMap map, boolean hasArgs) {
		if(hasArgs)
			return locateMethod(map, new DictType(TextType.instance()));
		else
			return locateMethod(map);
	}

	static IMethodDeclaration locateMethod(MethodDeclarationMap map, IType ... argTypes) {
		// try exact match first
		for(IMethodDeclaration method : map.values()) {
			if(MethodLocator.identicalArguments(method.getArguments(), argTypes))
				return method;
		}
		// match Text{} argument, will pass null 
		if(argTypes.length==0) for(IMethodDeclaration method : map.values()) {
			if(isSingleTextDictArgument(method.getArguments()))
				return method;
		}
		// match no argument, will ignore options
		for(IMethodDeclaration method : map.values()) {
			if(method.getArguments().size()==0)
				return method;
		}
		throw new SyntaxError("Could not find a compatible \"" + map.getId() + "\" method.");
	}

	static boolean isSingleTextDictArgument(ArgumentList arguments) {
		if(arguments.size()!=1)
			return false;
		IArgument arg = arguments.getFirst();
		if(!(arg instanceof ITypedArgument))
			return false;
		return ((ITypedArgument)arg).getType().equals(Interpreter.argsType);
	}

	static boolean identicalArguments(ArgumentList arguments, IType[] argTypes) {
		if(arguments.size()!=argTypes.length)
			return false;
		int idx = 0;
		for(IArgument argument : arguments) {
			if(!(argument instanceof ITypedArgument))
				return false;
			IType argType = argTypes[idx++];
			if(!argType.equals(((ITypedArgument)argument).getType()))
				return false;
		}
		return true;
	}

}
