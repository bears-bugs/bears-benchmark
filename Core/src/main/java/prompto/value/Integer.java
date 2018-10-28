package prompto.value;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoType;
import prompto.compiler.ResultInfo;
import prompto.compiler.ShortOperand;
import prompto.compiler.StackState;
import prompto.declaration.CategoryDeclaration;
import prompto.error.DivideByZeroError;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoChar;
import prompto.intrinsic.PromptoString;
import prompto.runtime.Context;
import prompto.type.CharacterType;
import prompto.type.DecimalType;
import prompto.type.EnumeratedNativeType;
import prompto.type.INumberType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.TextType;

import com.fasterxml.jackson.core.JsonGenerator;

public class Integer extends BaseValue implements INumber, Comparable<INumber>, IMultiplyable {
	
	public static Integer Parse(String text) {
		return new Integer(Long.parseLong(text));
	}

	long value;

	public Integer(long value) {
		super(IntegerType.instance());
		this.value = value;
	}

	@Override
	public Long getStorableData() {
		return value;
	}
	
	public long longValue() {
		return value;
	}

	public double doubleValue() {
		return value;
	}

	@Override
	public IValue plus(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer)
			return new Integer(this.longValue() + ((Integer) value).longValue());
		else if (value instanceof Decimal)
			return new Decimal(((Decimal) value).doubleValue() + this.value);
		else
			throw new SyntaxError("Illegal: Integer + " + value.getClass().getSimpleName());
	}

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		boolean isDecimal = isDecimal(context, exp);
		CompilerUtils.numberToPrimitive(method, left, isDecimal);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(isDecimal));
		CompilerUtils.numberToPrimitive(method, right, isDecimal);
		if(isDecimal) {
			method.addInstruction(Opcode.DADD);
			if(flags.toPrimitive())
				return new ResultInfo(double.class);
			else
				return CompilerUtils.doubleToDouble(method);
		} else {
			method.addInstruction(Opcode.LADD);
			if(flags.toPrimitive())
				return new ResultInfo(long.class);
			else
				return CompilerUtils.longToLong(method);
		}
	}

	private static boolean isDecimal(Context context, IExpression exp) {
		IType other = exp.check(context);
		if(other instanceof EnumeratedNativeType)
			other = ((EnumeratedNativeType)other).getDerivedFrom();
		return other==DecimalType.instance();
	}

	@Override
	public IValue minus(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer)
			return new Integer(this.longValue() - ((Integer) value).longValue());
		else if (value instanceof Decimal)
			return new Decimal(this.doubleValue() - ((Decimal) value).doubleValue());
		else
			throw new SyntaxError("Illegal: Integer - " + value.getClass().getSimpleName());
	}

	public static ResultInfo compileMinus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		boolean isDecimal = isDecimal(context, exp);
		CompilerUtils.numberToPrimitive(method, left, isDecimal);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(isDecimal));
		CompilerUtils.numberToPrimitive(method, right, isDecimal);
		if(isDecimal) {
			method.addInstruction(Opcode.DSUB);
			if(flags.toPrimitive())
				return new ResultInfo(double.class);
			else
				return CompilerUtils.doubleToDouble(method);
		} else {
			method.addInstruction(Opcode.LSUB);
			if(flags.toPrimitive())
				return new ResultInfo(long.class);
			else
				return CompilerUtils.longToLong(method);
		}
	}

	@Override
	public IValue multiply(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer)
			return new Integer(this.longValue() * ((Integer) value).longValue());
		else if (value instanceof Decimal)
			return new Decimal(((Decimal) value).doubleValue() * this.longValue());
		else if (value instanceof IMultiplyable)
			return value.multiply(context, this);
		else
			throw new SyntaxError("Illegal: Integer * " + value.getClass().getSimpleName());
	}

	public static ResultInfo compileMultiply(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		IType type = exp.check(context);
		if(type instanceof INumberType)
			return compileMultiplyNumber(context, method, flags, left, exp);
		else if(type==CharacterType.instance())
			return compileMultiplyCharacter(context, method, flags, left, exp);
		else if(type==TextType.instance())
			return compileMultiplyText(context, method, flags, left, exp);
		else if(type.getJavaType(context) instanceof PromptoType)
			return compileMultiplyCategory(context, method, flags, left, exp);
		else if(IMultiplyable.class.isAssignableFrom((Class<?>)type.getJavaType(context)))
			return compileMultiplyMultiplyable(context, method, flags, left, exp);
		else
			throw new SyntaxError("Illegal: Integer * " + type.getClass().getSimpleName());
	}

	private static ResultInfo compileMultiplyCategory(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		method.addInstruction(Opcode.SWAP);
		return CategoryDeclaration.compileMultiply(context, method, flags, right, left);
	}

	private static ResultInfo compileMultiplyCharacter(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		if(Long.class==left.getType())
			CompilerUtils.LongToint(method);
		else
			CompilerUtils.longToint(method);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		if(java.lang.Character.class==right.getType())
			CompilerUtils.CharacterTochar(method);
		// stack is int, char, need char, int
		method.addInstruction(Opcode.SWAP);
		MethodConstant oper = new MethodConstant(PromptoChar.class, 
				"multiply", 
				char.class, int.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(String.class);
	}
	
	private static ResultInfo compileMultiplyText(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		if(Long.class==left.getType())
			CompilerUtils.LongToint(method);
		else
			CompilerUtils.longToint(method);
		exp.compile(context, method, flags);
		// stack is int, String, need String int
		method.addInstruction(Opcode.SWAP);
		MethodConstant oper = new MethodConstant(PromptoString.class, 
				"multiply", 
				String.class, int.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(String.class);
	}

	private static ResultInfo compileMultiplyMultiplyable(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		if(Long.class==left.getType())
			CompilerUtils.LongToint(method);
		else
			CompilerUtils.longToint(method);
		ResultInfo rval = exp.compile(context, method, flags);
		method.addInstruction(Opcode.SWAP);
		try {
			Class<?> klass = (Class<?>)rval.getType();
			Class<?> resultType = klass.getMethod("multiply", int.class).getReturnType();
			IOperand oper = new MethodConstant(rval.getType(), "multiply", 
					int.class, resultType);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(resultType);
		} catch(NoSuchMethodException e) {
			throw new SyntaxError(e.getMessage());
		}
	}

	private static ResultInfo compileMultiplyNumber(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		boolean isDecimal = isDecimal(context, exp);
		CompilerUtils.numberToPrimitive(method, left, isDecimal);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(isDecimal));
		CompilerUtils.numberToPrimitive(method, right, isDecimal);
		if(isDecimal) {
			method.addInstruction(Opcode.DMUL);
			if(flags.toPrimitive())
				return new ResultInfo(double.class);
			else
				return CompilerUtils.doubleToDouble(method);
		} else {
			method.addInstruction(Opcode.LMUL);
			if(flags.toPrimitive())
				return new ResultInfo(long.class);
			else
				return CompilerUtils.longToLong(method);
		}
	}

	@Override
	public IValue divide(Context context, IValue value) throws PromptoError {
		if (value instanceof INumber) {
			if (((INumber) value).doubleValue() == 0.0)
				throw new DivideByZeroError();
			else
				return new Decimal(this.doubleValue() / ((INumber) value).doubleValue());
		} else
			throw new SyntaxError("Illegal: Integer / " + value.getClass().getSimpleName());
	}

	public static ResultInfo compileDivide(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		CompilerUtils.numberToPrimitive(method, left, true);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(true));
		CompilerUtils.numberToPrimitive(method, right, true);
		method.addInstruction(Opcode.DDIV);
		if(flags.toPrimitive())
			return new ResultInfo(double.class);
		else
			return CompilerUtils.doubleToDouble(method);
	}

	@Override
	public IValue intDivide(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer) {
			if (((Integer) value).longValue() == 0)
				throw new DivideByZeroError();
			else
				return new Integer(this.longValue() / ((Integer) value).longValue());
		} else
			throw new SyntaxError("Illegal: Integer \\ " + value.getClass().getSimpleName());
	}
	
	public static ResultInfo compileIntDivide(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		CompilerUtils.numberToPrimitive(method, left, false);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(false));
		CompilerUtils.numberToPrimitive(method, right, false);
		method.addInstruction(Opcode.LDIV);
		if(flags.toPrimitive())
			return new ResultInfo(long.class);
		else
			return CompilerUtils.longToLong(method);
	}

	@Override
	public IValue modulo(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer) {
			long mod = ((Integer) value).longValue();
			if (mod == 0)
				throw new DivideByZeroError();
			return new Integer(this.longValue() % mod);
		} else
			throw new SyntaxError("Illegal: Integer % " + value.getClass().getSimpleName());
	}

	public static ResultInfo compileModulo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		CompilerUtils.numberToPrimitive(method, left, false);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(false));
		CompilerUtils.numberToPrimitive(method, right, false);
		method.addInstruction(Opcode.LREM);
		if(flags.toPrimitive())
			return new ResultInfo(long.class);
		else
			return CompilerUtils.longToLong(method);
	}

	public int compareTo(INumber obj) {
		return Long.compare(value, obj.longValue());
	}

	@Override
	public int compareTo(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer)
			return Long.compare(this.value, ((Integer) value).longValue());
		else if (value instanceof Decimal)
			return Double.compare(this.doubleValue(), ((Decimal) value).doubleValue());
		else
			throw new SyntaxError("Illegal comparison: Integer and " + value.getClass().getSimpleName());

	}

	public static ResultInfo compileCompareTo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		boolean isDecimal = isDecimal(context, exp);
		CompilerUtils.numberToPrimitive(method, left, isDecimal);
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true).withDecimal(isDecimal));
		CompilerUtils.numberToPrimitive(method, right, isDecimal);
		if(isDecimal)
			method.addInstruction(Opcode.DCMPG);
		else
			method.addInstruction(Opcode.LCMP);
		return BaseValue.compileCompareToEpilogue(method, flags);
	}

	@Override
	public Object convertTo(Context context, Type type) {
		return value;
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}
	
	@Override
	public int hashCode() {
		return Long.valueOf(value).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Integer)
			return value == ((Integer) obj).value;
		else if (obj instanceof Decimal)
			return value == ((Decimal) obj).value;
		else
			return false;
	}
	
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		left = CompilerUtils.numberTolong(method, left);
		ResultInfo right = exp.compile(context, method, flags);
		right = CompilerUtils.numberTolong(method, right);
		method.addInstruction(Opcode.LCMP);
		Opcode opcode = flags.isReverse() ? Opcode.IFNE : Opcode.IFEQ;
		method.addInstruction(opcode, new ShortOperand((short)7));
		StackState branchState = method.captureStackState();
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		method.restoreFullStackState(branchState);
		method.placeLabel(branchState);
		method.addInstruction(Opcode.ICONST_1);
		StackState lastState = method.captureStackState();
		method.placeLabel(lastState);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			generator.writeNumber(value);
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}

	public IValue negate() {
		return new Integer(-value);
	}
	
	public static ResultInfo compileNegate(Context context, MethodInfo method, Flags flags, 
			ResultInfo value) {
		CompilerUtils.numberToPrimitive(method, value, false);
		method.addInstruction(Opcode.LNEG);
		if(flags.toPrimitive())
			return new ResultInfo(long.class);
		else
			return CompilerUtils.longToLong(method);
	}

}
