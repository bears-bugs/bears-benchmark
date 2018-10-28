package prompto.expression;

import java.util.Collection;
import java.util.stream.StreamSupport;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.ContOp;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoString;
import prompto.parser.Dialect;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.store.AttributeInfo;
import prompto.store.IQueryBuilder;
import prompto.store.IStore;
import prompto.store.IQueryBuilder.MatchOp;
import prompto.transpiler.Transpiler;
import prompto.type.CharacterType;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.type.TextType;
import prompto.utils.CodeWriter;
import prompto.value.Boolean;
import prompto.value.IContainer;
import prompto.value.IInstance;
import prompto.value.IIterable;
import prompto.value.IValue;

public class ContainsExpression extends Section implements IPredicateExpression, IAssertion {

	IExpression left;
	ContOp operator;
	IExpression right;
		
	public ContainsExpression(IExpression left, ContOp operator, IExpression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return left.toString() + " " + operator.toString() + " " + right.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		left.toDialect(writer);
		writer.append(" ");
		writer.append(operator.toString());
		writer.append(" ");
		right.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		IType lt = left.check(context);
		IType rt = right.check(context);
		switch(operator) {
		case IN:
		case NOT_IN:
			return rt.checkContains(context,lt);
		case HAS:
		case NOT_HAS:
			return lt.checkContains(context, rt);
		default:
			return lt.checkContainsAllOrAny(context, rt);
		}
	}
	
	@Override
    public IValue interpret(Context context) throws PromptoError {
    	IValue lval = left.interpret(context);
    	IValue rval = right.interpret(context);
    	return interpret(context, lval, rval);

    }
    
    @Override
    public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
    	ResultInfo result = compileOperator(context, method, flags.withPrimitive(true));
    	if (operator.name().startsWith("NOT_"))
    		return compileNot(context, method, flags, result);
    	else if((!flags.toPrimitive() && boolean.class==result.getType()))
    		return CompilerUtils.booleanToBoolean(method);
    	else
    		return result;
    }
    
    private ResultInfo compileNot(Context context, MethodInfo method, Flags flags, ResultInfo info) {
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		CompilerUtils.reverseBoolean(method);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}

	private ResultInfo compileOperator(Context context, MethodInfo method, Flags flags) {
    	switch (operator) {
            case IN:
            case NOT_IN:
            	return compileContains(context, method, flags, right, left);
            case HAS:
            case NOT_HAS:
            	return compileContains(context, method, flags, left, right);
            case HAS_ALL:
            case NOT_HAS_ALL:
            	return compileContainsAll(context, method, flags, left, right);
            case HAS_ANY:
            case NOT_HAS_ANY:
            	return compileContainsAny(context, method, flags, left, right);
            default:
        		throw new UnsupportedOperationException("Uknown operator: " + operator.name());
        }
    }

	private ResultInfo compileContains(Context context, MethodInfo method, Flags flags, IExpression left, IExpression right) {
		ResultInfo linfo = left.compile(context, method, flags);
		right.compile(context, method, flags.withPrimitive(false));
		if(String.class==linfo.getType()) {
			MethodConstant m = new MethodConstant(PromptoString.class, "contains", String.class, Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKESTATIC, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		} else {
			MethodConstant m = new MethodConstant(linfo.getType(), "contains", Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		}
	}

	private ResultInfo compileContainsAll(Context context, MethodInfo method, Flags flags, IExpression left, IExpression right) {
		ResultInfo linfo = left.compile(context, method, flags);
		right.compile(context, method, flags.withPrimitive(false));
		if(String.class==linfo.getType()) {
			MethodConstant m = new MethodConstant(PromptoString.class, "containsAll", String.class, Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKESTATIC, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		} else {
			MethodConstant m = new MethodConstant(linfo.getType(), "containsAll", Collection.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		}
	}

	private ResultInfo compileContainsAny(Context context, MethodInfo method, Flags flags, IExpression left, IExpression right) {
		ResultInfo linfo = left.compile(context, method, flags);
		right.compile(context, method, flags.withPrimitive(false));
		if(String.class==linfo.getType()) {
			MethodConstant m = new MethodConstant(PromptoString.class, "containsAny", String.class, Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKESTATIC, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		} else {
			MethodConstant m = new MethodConstant(linfo.getType(), "containsAny", Collection.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
			if(flags.toPrimitive())
				return new ResultInfo(boolean.class);
			else
				return CompilerUtils.booleanToBoolean(method);
		}
	}

	private IValue interpret(Context context, IValue lval, IValue rval) throws PromptoError {
        java.lang.Boolean result = null;
        switch (operator)
        {
            case IN:
            case NOT_IN:
                if(rval instanceof IContainer)
                    result = ((IContainer<?>)rval).hasItem(context, lval);
                else if(rval instanceof IIterable)
                	result = containsOne(context, (IIterable<?>)rval, lval);
                break;
            case HAS:
            case NOT_HAS:
                if(lval instanceof IContainer)
                    result = ((IContainer<?>)lval).hasItem(context, rval);
                else if(lval instanceof IIterable)
                	result = containsOne(context, (IIterable<?>)lval, rval);
                break;
            case HAS_ALL:
            case NOT_HAS_ALL:
                if (lval instanceof IContainer && rval instanceof IContainer)
                    result = containsAll(context, (IContainer<?>)lval, (IContainer<?>)rval);
                break;
            case HAS_ANY:
            case NOT_HAS_ANY:
                if (lval instanceof IContainer && rval instanceof IContainer)
                    result = containsAny(context, (IContainer<?>)lval, (IContainer<?>)rval);
                break;
        }
        if (result != null)
        {
            if (operator.name().startsWith("NOT_"))
                result = !result;
            return Boolean.valueOf(result);
        }
        if (operator.name().endsWith("IN"))
        {
            IValue tmp = lval;
            lval = rval;
            rval = tmp;
        }
        String lowerName = operator.name().toLowerCase().replace('_', ' ');
        throw new SyntaxError("Illegal comparison: " + lval.getClass().getSimpleName() +
        		" " + lowerName + " " + rval.getClass().getSimpleName());	
    }

	private java.lang.Boolean containsOne(Context context, IIterable<?> container, IValue item) {
		return StreamSupport.stream(container.getIterable(context).spliterator(), false)
				.anyMatch((o)->o.equals(item));
	}

	public boolean containsAll(Context context, IContainer<?> container, IContainer<?> items) throws PromptoError
    {
        for (IValue item : items.getIterable(context)) {
            if (!container.hasItem(context, item))
                 return false;
        }
        return true;
    }
    public boolean containsAny(Context context, IContainer<?> container, IContainer<?> items) throws PromptoError
    {
        for (IValue item : items.getIterable(context)) {
           if (container.hasItem(context, item))
                    return true;
        }
        return false;
    }
    
	@Override
	public boolean interpretAssert(Context context, TestMethodDeclaration test) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
		IValue result = interpret(context, lval, rval);
		if(result==Boolean.TRUE) 
			return true;
		String expected = buildExpectedMessage(context, test);
		String actual = lval.toString() + " " + operator.toString() + " " + rval.toString();
		test.printFailedAssertion(context, expected, actual);
		return false;
	}
	
	private String buildExpectedMessage(Context context, TestMethodDeclaration test) {
		CodeWriter writer = new CodeWriter(test.getDialect(), context);
		this.toDialect(writer);
		return writer.toString();
	}

	@Override
	public void compileAssert(Context context, MethodInfo method, Flags flags, TestMethodDeclaration test) {
		context = context.newChildContext();
		StackState finalState = method.captureStackState();
		// compile left and store in local
		IType leftType = this.left.check(context);
		ResultInfo leftInfo = this.left.compile(context, method, flags.withPrimitive(false));
		String leftName = method.nextTransientName("left");
		StackLocal left = method.registerLocal(leftName, VerifierType.ITEM_Object, new ClassConstant(leftInfo.getType()));
		CompilerUtils.compileASTORE(method, left);
		// compile right and store in local
		IType rightType = this.right.check(context);
		ResultInfo rightInfo = this.right.compile(context, method, flags.withPrimitive(true));
		String rightName = method.nextTransientName("right");
		StackLocal right = method.registerLocal(rightName, VerifierType.ITEM_Object, new ClassConstant(rightInfo.getType()));
		CompilerUtils.compileASTORE(method, right);
		// call regular compile
		IExpression newLeft = new InstanceExpression(new Identifier(leftName));
		context.registerValue(new Variable(new Identifier(leftName), leftType));
		IExpression newRight = new InstanceExpression(new Identifier(rightName));
		context.registerValue(new Variable(new Identifier(rightName), rightType));
		ContainsExpression newExp = new ContainsExpression(newLeft, this.operator, newRight);
		ResultInfo info = newExp.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		// 1 = success
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); 
		// increment failure counter
		method.addInstruction(Opcode.ICONST_1);
		method.addInstruction(Opcode.IADD);
		// build failure message
		String message = buildExpectedMessage(context, test);
		message = test.buildFailedAssertionMessagePrefix(message);
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		CompilerUtils.compileALOAD(method, left);
		MethodConstant toString = new MethodConstant(leftInfo.getType(), "toString", String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, toString);
		MethodConstant concat = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		method.addInstruction(Opcode.LDC, new StringConstant(" " + operator.toString() + " "));
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		CompilerUtils.compileALOAD(method, right);
		toString = new MethodConstant(rightInfo.getType(), "toString", String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, toString);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		test.compileFailure(context, method, flags);
		// success/final
		method.unregisterLocal(right);
		method.unregisterLocal(left);
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);
	}
	
	@Override
	public void interpretQuery(Context context, IQueryBuilder query) throws PromptoError {
		IValue value = null;
		String name = readFieldName(left);
		boolean reverse = name==null;
		if(name!=null)
			value = right.interpret(context);
		else {
			name = readFieldName(right);
			if(name!=null)
				value = left.interpret(context);
			else
				throw new SyntaxError("Unable to interpret predicate");
		}
		MatchOp matchOp = getMatchOp(context, getAttributeType(context, name), value.getType(), this.operator, reverse);
		if(value instanceof IInstance)
			value = ((IInstance)value).getMember(context, new Identifier(IStore.dbIdName), false);
		AttributeInfo info = context.findAttribute(name).getAttributeInfo(context);
		Object data = value==null ? null : value.getStorableData();
		query.<Object>verify(info, matchOp, data);
		if(operator.name().startsWith("NOT_"))
			query.not();
	}
	
	@Override
	public void compileQuery(Context context, MethodInfo method, Flags flags) {
		IType valueType = null;
		String name = readFieldName(left);
		boolean reverse = name==null;
		if(name!=null)
			valueType = right.check(context);
		else {
			name = readFieldName(right);
			if(name!=null)
				valueType = left.check(context);
			else
				throw new SyntaxError("Unable to interpret predicate");
		}
		AttributeInfo info = context.findAttribute(name).getAttributeInfo(context);
		CompilerUtils.compileAttributeInfo(context, method, flags, info);
		MatchOp match = getMatchOp(context, getAttributeType(context, name), valueType, this.operator, reverse);
		CompilerUtils.compileJavaEnum(context, method, flags, match);
		if(reverse)
			left.compile(context, method, flags);
		else
			right.compile(context, method, flags);
		InterfaceConstant m = new InterfaceConstant(IQueryBuilder.class,
				"verify", AttributeInfo.class, MatchOp.class, Object.class, IQueryBuilder.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, m);
		if(operator.name().startsWith("NOT_")) {
			m = new InterfaceConstant(IQueryBuilder.class, "not", IQueryBuilder.class);
			method.addInstruction(Opcode.INVOKEINTERFACE, m);
		}
	}
	
	
	private IType getAttributeType(Context context, String name) {
		return context.getRegisteredDeclaration(AttributeDeclaration.class, new Identifier(name)).getType();
	}

	private MatchOp getMatchOp(Context context, IType fieldType, IType valueType, ContOp operator, boolean reverse) {
		if(reverse) {
			operator = operator.reverse();
			if(operator==null)
				context.getProblemListener().reportIllegalOperation("Cannot reverse " + this.operator, this);
			return getMatchOp(context, valueType, fieldType, operator, false);
		}
		if((fieldType==TextType.instance() || valueType==CharacterType.instance()) &&
				(valueType==TextType.instance() || valueType==CharacterType.instance())) {
			switch(operator) {
			case HAS:
			case NOT_HAS:
				return MatchOp.CONTAINS;
			default:
				// throw below
			}
		} 
		if(valueType instanceof ContainerType) {
			switch(operator) {
			case IN:
			case NOT_IN:
				return MatchOp.IN;
			default:
				// throw below
			}
		} 
		if(fieldType instanceof ContainerType) {
			switch(operator) {
			case HAS:
			case NOT_HAS:
				return MatchOp.CONTAINS;
			default:
				// throw below
			}
		} 
		throw new SyntaxError("Unsupported operator: " + operator.name());
	}

	private String readFieldName(IExpression exp) {
		if(exp instanceof UnresolvedIdentifier
			|| exp instanceof InstanceExpression
			|| exp instanceof MemberSelector)
			return exp.toString();
		else
			return null;
	}

	@Override
	public void declare(Transpiler transpiler) {
	    IType lt = this.left.check(transpiler.getContext());
	    IType rt = this.right.check(transpiler.getContext());
	    switch(this.operator) {
	        case IN:
	        case NOT_IN:
	            rt.declareContains(transpiler, lt, this.right, this.left);
	            break;
	        case HAS:
	        case NOT_HAS:
	            lt.declareContains(transpiler, rt, this.left, this.right);
	            break;
	        default:
	            lt.declareContainsAllOrAny(transpiler, rt, this.left, this.right);
	    }
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		IType lt = this.left.check(transpiler.getContext());
		IType rt = this.right.check(transpiler.getContext());
	    switch(this.operator) {
	        case NOT_IN:
	            transpiler.append("!");
	        case IN:
	            rt.transpileContains(transpiler, lt, this.right, this.left);
	            return false;
	        case NOT_HAS:
	        	transpiler.append("!");
	        case HAS:
	            lt.transpileContains(transpiler, rt, this.left, this.right);
	            return false;
	        case NOT_HAS_ALL:
	            transpiler.append("!");
	        case HAS_ALL:
	            lt.transpileContainsAll(transpiler, rt, this.left, this.right);
	            return false;
	        case NOT_HAS_ANY:
	            transpiler.append("!");
	        case HAS_ANY:
	            lt.transpileContainsAny(transpiler, rt, this.left, this.right);
	            return false;
	        default:
	            throw new UnsupportedOperationException("Unsupported " + this.operator);
	    }
	}
	
	@Override
	public void transpileQuery(Transpiler transpiler, String builderName) {
	    boolean reverse = false;
	    IExpression value = null;
	    String name = this.readFieldName(this.left);
	    if (name != null)
	        value = this.right;
	    else {
	        reverse = true;
	        name = this.readFieldName(this.right);
	        if (name != null)
	            value = this.left;
	        else
	            throw new SyntaxError("Unable to transpile predicate");
	    }
	    AttributeDeclaration decl = transpiler.getContext().findAttribute(name);
	    AttributeInfo info = decl.getAttributeInfo(transpiler.getContext());
	    IType type = value.check(transpiler.getContext());
	    // TODO check for dbId field of instance value
	    MatchOp matchOp = this.getMatchOp(transpiler.getContext(), decl.getType(), type, this.operator, reverse);
	    transpiler.append(builderName).append(".verify(").append(info.toTranspiled()).append(", MatchOp.").append(matchOp.name()).append(", ");
	    value.transpile(transpiler);
	    transpiler.append(");").newLine();
	    if (this.operator.name().indexOf("NOT_")==0)
	        transpiler.append(builderName).append(".not();").newLine();
	}
	
	@Override
	public void transpileFound(Transpiler transpiler, Dialect dialect) {
	    transpiler.append("(");
	    this.left.transpile(transpiler);
	    transpiler.append(") + '").append(this.operator.toString()).append("' + (");
	    this.right.transpile(transpiler);
	    transpiler.append(")");
	}
}
