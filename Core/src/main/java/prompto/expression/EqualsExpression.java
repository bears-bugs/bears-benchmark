package prompto.expression;

import java.util.HashMap;
import java.util.Map;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.IOperatorFunction;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ShortOperand;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.EqOp;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoAny;
import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoDateTime;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoPeriod;
import prompto.intrinsic.PromptoRange;
import prompto.intrinsic.PromptoSet;
import prompto.intrinsic.PromptoTime;
import prompto.intrinsic.PromptoVersion;
import prompto.literal.NullLiteral;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.LinkedValue;
import prompto.runtime.LinkedVariable;
import prompto.runtime.Variable;
import prompto.store.AttributeInfo;
import prompto.store.IQueryBuilder;
import prompto.store.IQueryBuilder.MatchOp;
import prompto.store.IStore;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.BooleanType;
import prompto.type.CharacterType;
import prompto.type.ContainerType;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.TextType;
import prompto.utils.CodeWriter;
import prompto.value.Boolean;
import prompto.value.Character;
import prompto.value.Date;
import prompto.value.DateTime;
import prompto.value.Decimal;
import prompto.value.Dictionary;
import prompto.value.IInstance;
import prompto.value.IValue;
import prompto.value.Integer;
import prompto.value.ListValue;
import prompto.value.NullValue;
import prompto.value.Period;
import prompto.value.RangeBase;
import prompto.value.SetValue;
import prompto.value.Text;
import prompto.value.Time;
import prompto.value.TypeValue;
import prompto.value.Version;

public class EqualsExpression implements IPredicateExpression, IAssertion {

	IExpression left;
	EqOp operator;
	IExpression right;
	
	public EqualsExpression(IExpression left, EqOp operator, IExpression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return left.toString() + ' ' + operator.toString() + ' ' + right.toString();
	}
	
	static final String VOWELS = "AEIO"; // sufficient here
	
	@Override
	public void toDialect(CodeWriter writer) {
		left.toDialect(writer);
		writer.append(" ");
		writer.append(operator.toString(writer.getDialect()));
		// make this a AN
		if(operator==EqOp.IS_A || operator==EqOp.IS_NOT_A) {
			String name = right.toString();
			if(VOWELS.indexOf(name.charAt(0))>=0)
				writer.append("n");
		}
		writer.append(" ");
		right.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		IType lt = left.check(context);
		if(lt instanceof ContainerType)
			lt = ((ContainerType)lt).getItemType();
		IType rt = right.check(context);
		if(rt instanceof ContainerType)
			rt = ((ContainerType)rt).getItemType();
		switch(operator) {
			case CONTAINS:
			case NOT_CONTAINS:
				if(lt!=TextType.instance() || (rt!=TextType.instance() && rt!=CharacterType.instance()))
					throw new SyntaxError("'contains' only operates on textual values!");
			default:	
				return BooleanType.instance(); // can compare all objects
		}
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue lval = left.interpret(context);
		if(lval==null)
			lval = NullValue.instance();
		IValue rval = right.interpret(context);
		if(rval==null)
			rval = NullValue.instance();
		return interpret(context, lval, rval);
	}

	private IValue interpret(Context context, IValue lval, IValue rval) throws PromptoError {
		boolean equal = false;
		switch(operator) {
		case IS:
			equal = lval==rval;
			break;
		case IS_NOT:
			equal = lval!=rval;
			break;
		case IS_A:
			equal = interpretIsA(context,lval,rval);
			break;
		case IS_NOT_A:
			equal = !interpretIsA(context,lval,rval);
			break;
		case EQUALS:
			equal = interpretEquals(context,lval,rval);
			break;
		case NOT_EQUALS:
			equal = !interpretEquals(context,lval,rval);
			break;
		case ROUGHLY:
			equal = lval.roughly(context, rval);
			break;
		case CONTAINS:
			equal = interpretContains(context,lval,rval);
			break;
		case NOT_CONTAINS:
			equal = !interpretContains(context,lval,rval);
			break;
		}
		return Boolean.valueOf(equal);	}

	private boolean interpretIsA(Context context, IValue lval, IValue rval) throws PromptoError {
		IType actual = lval.getType();
		IType expected = ((TypeValue)rval).getValue();
		return expected.isAssignableFrom(context, actual);
	}

	private boolean interpretEquals(Context context, IValue lval, IValue rval) throws PromptoError {
		if(lval==rval)
			return true;
		else if(lval==NullValue.instance() || rval==NullValue.instance())
			return false;
		else
			return lval.equals(rval);
	}

	private boolean interpretContains(Context context, IValue lval, IValue rval) throws PromptoError {
		if(lval==rval)
			return true;
		else if(lval==NullValue.instance() || rval==NullValue.instance())
			return false;
		else 
			return lval.contains(context, rval);
	}

	public Context downCastForCheck(Context context) {
		try {
			return downCast(context, false);
		} catch(PromptoError e) {
			throw new RuntimeException("Should never get there!");
		}
	}
	public Context downCastForInterpret(Context context) throws PromptoError {
		return downCast(context, true);
	}

	private Context downCast(Context context, boolean setValue) throws PromptoError {
		if(operator==EqOp.IS_A) {
			Identifier name = readLeftName();
			if(name!=null) {
				INamed value = context.getRegisteredValue(INamed.class, name);
				IType type = ((TypeExpression)right).getType();
				Context local = context.newChildContext();
				value = new LinkedVariable(type, value);
				local.registerValue(value, false);
				if(setValue)
					local.setValue(name, new LinkedValue(context, type));
				context = local;
			}
		}
		return context;
	}
	
	public Context prepareAutodowncast(Context context, MethodInfo method) {
		if(operator==EqOp.IS_A) {
			Identifier name = readLeftName();
			if(name!=null) {
				IType type = ((TypeExpression)right).getType();
				ClassConstant c = new ClassConstant(type.getJavaType(context));
				StackLocal local = method.getRegisteredLocal(name.toString());
				((StackLocal.ObjectLocal)local).markForAutodowncast(c);
				return downCastForCheck(context);
			}
		}
		return context;
	}

	public void cancelAutodowncast(Context context, MethodInfo method) {
		if(operator==EqOp.IS_A) {
			Identifier name = readLeftName();
			if(name!=null) {
				StackLocal local = method.getRegisteredLocal(name.toString());
				((StackLocal.ObjectLocal)local).unmarkForAutodowncast();
			}
		}
	}
	
	private Identifier readLeftName() {
		if(left instanceof InstanceExpression)
			return ((InstanceExpression)left).getId();
		else if(left instanceof UnresolvedIdentifier)
			return ((UnresolvedIdentifier)left).getId();
		return null;
	}
	
	@Override
	public boolean interpretAssert(Context context, TestMethodDeclaration test) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
		IValue result = interpret(context, lval, rval);
		if(result==Boolean.TRUE) 
			return true;
		String expected = buildExpectedMessage(context, test);
		String actual = lval.toString() + " " + operator.toString(test.getDialect()) + " " + rval.toString();
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
		ResultInfo rightInfo = this.right.compile(context, method, flags.withPrimitive(false));
		String rightName = method.nextTransientName("right");
		StackLocal right = method.registerLocal(rightName, VerifierType.ITEM_Object, new ClassConstant(rightInfo.getType()));
		CompilerUtils.compileASTORE(method, right);
		// call regular compile
		IExpression newLeft = new InstanceExpression(new Identifier(leftName));
		context.registerValue(new Variable(new Identifier(leftName), leftType));
		IExpression newRight = new InstanceExpression(new Identifier(rightName));
		context.registerValue(new Variable(new Identifier(rightName), rightType));
		EqualsExpression newExp = new EqualsExpression(newLeft, this.operator, newRight);
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
		MethodConstant stringValueOf = new MethodConstant(String.class, "valueOf", Object.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, stringValueOf);
		MethodConstant concat = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		method.addInstruction(Opcode.LDC, new StringConstant(" " + operator.toString(test.getDialect()) + " "));
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		CompilerUtils.compileALOAD(method, right);
		method.addInstruction(Opcode.INVOKESTATIC, stringValueOf);
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
		if(name!=null)
			value = right.interpret(context);
		else {
			name = readFieldName(right);
			if(name!=null)
				value = left.interpret(context);
			else
				throw new SyntaxError("Unable to interpret predicate");
		}
		if(value instanceof IInstance)
			value = ((IInstance)value).getMember(context, new Identifier(IStore.dbIdName), false);
		AttributeDeclaration decl = context.findAttribute(name);
		AttributeInfo info = decl==null ? null : decl.getAttributeInfo(context);
		Object data = value==null ? null : value.getStorableData();
		MatchOp match = getMatchOp();
		query.<Object>verify(info, match, data);
		if(operator==EqOp.NOT_EQUALS || operator==EqOp.NOT_CONTAINS)
			query.not();
	}
	
	private MatchOp getMatchOp() {
		switch(operator) {
		case EQUALS:
		case NOT_EQUALS:
			return MatchOp.EQUALS;
		case ROUGHLY:
			return MatchOp.ROUGHLY;
		case CONTAINS:
		case NOT_CONTAINS:
			return MatchOp.CONTAINS;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void compileQuery(Context context, MethodInfo method, Flags flags) {
		boolean reverse = compileAttributeInfo(context, method, flags);
		MatchOp match = getMatchOp();
		CompilerUtils.compileJavaEnum(context, method, flags, match);
		if(reverse)
			left.compile(context, method, flags);
		else
			right.compile(context, method, flags);
		InterfaceConstant m = new InterfaceConstant(IQueryBuilder.class,
				"verify", AttributeInfo.class, MatchOp.class, Object.class, IQueryBuilder.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, m);
		if(operator==EqOp.NOT_EQUALS) {
			m = new InterfaceConstant(IQueryBuilder.class, "not", IQueryBuilder.class);
			method.addInstruction(Opcode.INVOKEINTERFACE, m);
		}
	}
	

	private boolean compileAttributeInfo(Context context, MethodInfo method, Flags flags) {
		String name = readFieldName(left);
		boolean reverse = name==null;
		if(reverse)
			name = readFieldName(right);
		AttributeInfo info = context.findAttribute(name).getAttributeInfo(context);
		CompilerUtils.compileAttributeInfo(context, method, flags, info);
		return reverse;
	}

	private String readFieldName(IExpression exp) {
		if(exp instanceof UnresolvedIdentifier
			|| exp instanceof InstanceExpression
			|| exp instanceof MemberSelector)
			return exp.toString();
		else
			return null;
	}

	static Map<Class<?>, IOperatorFunction> testers = createTesters();
	
	private static Map<Class<?>, IOperatorFunction> createTesters() {
		Map<Class<?>, IOperatorFunction> map = new HashMap<>();
		map.put(boolean.class, Boolean::compileEquals); 
		map.put(java.lang.Boolean.class, Boolean::compileEquals); 
		map.put(char.class, Character::compileEquals);
		map.put(java.lang.Character.class, Character::compileEquals);
		map.put(String.class, Text::compileEquals); 
		map.put(double.class, Decimal::compileEquals);
		map.put(Double.class, Decimal::compileEquals); 
		map.put(long.class, Integer::compileEquals);
		map.put(Long.class, Integer::compileEquals); 
		map.put(PromptoAny.class, AnyType::compileEquals); 
		map.put(PromptoRange.Long.class, RangeBase::compileEquals); 
		map.put(PromptoRange.Character.class, RangeBase::compileEquals); 
		map.put(PromptoRange.Date.class, RangeBase::compileEquals); 
		map.put(PromptoRange.Time.class, RangeBase::compileEquals); 
		map.put(PromptoDate.class, Date::compileEquals); 
		map.put(PromptoDateTime.class, DateTime::compileEquals); 
		map.put(PromptoTime.class, Time::compileEquals); 
		map.put(PromptoPeriod.class, Period::compileEquals); 
		map.put(PromptoVersion.class, Version::compileEquals); 
		map.put(PromptoDict.class, Dictionary::compileEquals);
		map.put(PromptoSet.class, SetValue::compileEquals);  /*
		map.put(PromptoTuple.class, TupleValue::compileEquals); */
		map.put(PromptoList.class, ListValue::compileEquals); 
		return map;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		switch(operator) {
		case EQUALS:
			return compileEquals(context, method, flags.withReverse(false));
		case NOT_EQUALS:
			return compileEquals(context, method, flags.withReverse(true));
		case ROUGHLY:
			return compileEquals(context, method, flags.withReverse(false).withRoughly(true));
		case IS:
			return compileIs(context, method, flags.withReverse(false));
		case IS_NOT:
			return compileIs(context, method, flags.withReverse(true));
		case IS_A:
			return compileIsA(context, method, flags.withReverse(false));
		case IS_NOT_A:
			return compileIsA(context, method, flags.withReverse(true));
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private ResultInfo compileIsA(Context context, MethodInfo method, Flags flags) {
		right.compile(context, method, flags.withPrimitive(false));
		left.compile(context, method, flags.withPrimitive(false));
		MethodConstant m = new MethodConstant(Class.class, "isInstance", Object.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		if(flags.isReverse())
			CompilerUtils.reverseBoolean(method);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}

	public ResultInfo compileIs(Context context, MethodInfo method, Flags flags) {
		if(left.equals(right)) 
			method.addInstruction(flags.isReverse() ? Opcode.ICONST_0 : Opcode.ICONST_1);
		else if(left instanceof NullLiteral)
			compileIsNull(context, method, flags, right);
		else if(right instanceof NullLiteral)
			compileIsNull(context, method, flags, left);
		else 
			compileIsInstance(context, method, flags);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	private void compileIsNull(Context context, MethodInfo method, Flags flags, IExpression value) {
		StackState initialState = method.captureStackState();
		value.compile(context, method, flags.withPrimitive(false));
		Opcode opcode = flags.isReverse() ? Opcode.IFNONNULL : Opcode.IFNULL;
		method.addInstruction(opcode, new ShortOperand((short)7));
		compileIsEpilogue(context, method, flags, initialState);
	}

	private void compileIsEpilogue(Context context, MethodInfo method, Flags flags, StackState initialState) {
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		method.restoreFullStackState(initialState);
		method.placeLabel(initialState);
		method.addInstruction(Opcode.ICONST_1);
		StackState lastState = method.captureStackState();
		method.placeLabel(lastState);
	}

	private void compileIsInstance(Context context, MethodInfo method, Flags flags) {
		StackState initialState = method.captureStackState();
		left.compile(context, method, flags.withPrimitive(false));
		right.compile(context, method, flags.withPrimitive(false));
		Opcode opcode = flags.isReverse() ? Opcode.IF_ACMPNE : Opcode.IF_ACMPEQ;
		method.addInstruction(opcode, new ShortOperand((short)7));
		compileIsEpilogue(context, method, flags, initialState);
	}

	public ResultInfo compileEquals(Context context, MethodInfo method, Flags flags) {
		ResultInfo lval = left.compile(context, method, flags.withPrimitive(true));
		IOperatorFunction tester = testers.get(lval.getType());
		if(tester==null) {
			System.err.println("Missing IOperatorFunction for = " + lval.getType().getTypeName());
			throw new SyntaxError("Cannot check equality of " + lval.getType().getTypeName() + " with " + right.check(context).getFamily());
		}
		return tester.compile(context, method, flags, lval, right);
	}

	@Override
	public void declare(Transpiler transpiler) {
	    this.left.declare(transpiler);
	    this.right.declare(transpiler);
	    if(this.operator == EqOp.ROUGHLY) {
	        transpiler.require("removeAccents");
	    }
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    switch (this.operator) {
        case EQUALS:
            this.transpileEquals(transpiler);
            break;
        case NOT_EQUALS:
            this.transpileNotEquals(transpiler);
            break;
        case ROUGHLY:
            this.transpileRoughly(transpiler);
            break;
            /*
        case EqOp.CONTAINS:
            this.transpileContains(transpiler);
            break;
        case EqOp.NOT_CONTAINS:
            this.transpileNotContains(transpiler);
            break;
            */
        case IS:
            this.transpileIs(transpiler);
            break;
        case IS_NOT:
            this.transpileIsNot(transpiler);
            break;
        case IS_A:
            this.transpileIsA(transpiler);
            break;
        default:
            throw new Error("Cannot transpile:" + this.operator.toString());
	    }
	    return false;
    }

	private void transpileIsA(Transpiler transpiler) {
		if(!(this.right instanceof TypeExpression))
			throw new Error("Cannot transpile:" + this.right.getClass().getName());
		IType type = ((TypeExpression)this.right).getType();
	    if(type==IntegerType.instance()) {
	        transpiler.append("isAnInteger(");
	        this.left.transpile(transpiler);
	        transpiler.append(")");
	    } else if(type==DecimalType.instance()) {
	        transpiler.append("isADecimal(");
	        this.left.transpile(transpiler);
	        transpiler.append(")");
	    } else if(type==TextType.instance()) {
	        transpiler.append("isAText(");
	        this.left.transpile(transpiler);
	        transpiler.append(")");
	    } else if(type==CharacterType.instance()) {
	        transpiler.append("isACharacter(");
	        this.left.transpile(transpiler);
	        transpiler.append(")");
	    } else {
	        this.left.transpile(transpiler);
	        transpiler.append(" instanceof ");
	        this.right.transpile(transpiler);
	    }
	}

	private void transpileRoughly(Transpiler transpiler) {
	    transpiler.append("removeAccents(");
	    this.left.transpile(transpiler);
	    transpiler.append(").toLowerCase() === removeAccents(");
	    this.right.transpile(transpiler);
	    transpiler.append(").toLowerCase()");
	}

	private void transpileIsNot(Transpiler transpiler) {
	    this.left.transpile(transpiler);
	    transpiler.append(" !== ");
	    this.right.transpile(transpiler);
	}

	private void transpileIs(Transpiler transpiler) {
	    this.left.transpile(transpiler);
	    transpiler.append(" === ");
	    this.right.transpile(transpiler);
	}
	
	

	private void transpileEquals(Transpiler transpiler) {
	    IType lt = this.left.check(transpiler.getContext());
	    if(lt == BooleanType.instance() || lt == IntegerType.instance() || lt == DecimalType.instance() || lt == CharacterType.instance() || lt == TextType.instance()) {
	        this.left.transpile(transpiler);
	        transpiler.append(" === ");
	        this.right.transpile(transpiler);
	    } else {
	        this.left.transpile(transpiler);
	        transpiler.append(".equals(");
	        this.right.transpile(transpiler);
	        transpiler.append(")");
	    }
	}

	private void transpileNotEquals(Transpiler transpiler) {
	    IType lt = this.left.check(transpiler.getContext());
	    if(lt == BooleanType.instance() || lt == IntegerType.instance() || lt == DecimalType.instance() || lt == CharacterType.instance() || lt == TextType.instance()) {
	        this.left.transpile(transpiler);
	        transpiler.append(" !== ");
	        this.right.transpile(transpiler);
	    } else {
	    	transpiler.append("!");
	        this.left.transpile(transpiler);
	        transpiler.append(".equals(");
	        this.right.transpile(transpiler);
	        transpiler.append(")");
	    }
	}
	
	@Override
	public void declareQuery(Transpiler transpiler) {
	    transpiler.require("MatchOp");
	    this.left.declare(transpiler);
	    this.right.declare(transpiler);
	}
	
	@Override
	public void transpileQuery(Transpiler transpiler, String builderName) {
	    IExpression value = null;
	    String name = this.readFieldName(this.left);
	    if (name != null)
	        value = this.right;
	    else {
	        name = this.readFieldName(this.right);
	        if (name != null)
	            value = this.left;
	        else
	            throw new SyntaxError("Unable to interpret predicate");
	    }
	    AttributeDeclaration decl = transpiler.getContext().findAttribute(name);
	    AttributeInfo info = decl.getAttributeInfo(transpiler.getContext());
	    MatchOp matchOp = this.getMatchOp();
	    // TODO check for dbId field of instance value
	    transpiler.append(builderName).append(".verify(").append(info.toTranspiled()).append(", MatchOp.").append(matchOp.name()).append(", ");
	    value.transpile(transpiler);
	    transpiler.append(");").newLine();
	    if (this.operator == EqOp.NOT_EQUALS)
	        transpiler.append(builderName).append(".not();").newLine();
	}
	
	@Override
	public void transpileFound(Transpiler transpiler, Dialect dialect) {
	    transpiler.append("(");
	    this.left.transpile(transpiler);
	    transpiler.append(") + ' ").append(this.operator.toString(dialect)).append(" ' + (");
	    this.right.transpile(transpiler);
	    transpiler.append(")");
	}

}
