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
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoPeriod;
import prompto.intrinsic.PromptoTime;
import prompto.runtime.Context;
import prompto.type.TimeType;

import com.fasterxml.jackson.core.JsonGenerator;


public class Time extends BaseValue implements Comparable<Time> {
	public static Time Parse(String text) {
		return new Time(PromptoTime.parse(text));
	}

	PromptoTime value;

	public Time(PromptoTime time) {
		super(TimeType.instance());
		this.value = time;
	}

	public Time(int hours, int minutes, int seconds, int millis) {
		super(TimeType.instance());
		this.value = new PromptoTime(hours, minutes, seconds, millis);
	}

	@Override
	public PromptoTime getStorableData() {
		return value;
	}

	@Override
	public IValue plus(Context context, IValue value) {
		if (value instanceof Period)
			return new Time(this.value.plus(((Period)value).value));
		else
			throw new SyntaxError("Illegal: Time + " + value.getClass().getSimpleName());
	}

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags);
		if(right.getType()!=PromptoPeriod.class)
			throw new SyntaxError("Illegal: Date + " + exp.getClass().getSimpleName());
		MethodConstant oper = new MethodConstant(PromptoTime.class, "plus", PromptoPeriod.class, PromptoTime.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(PromptoTime.class);
	}
	
	@Override
	public IValue minus(Context context, IValue value) throws PromptoError {
		if (value instanceof Time)
			return new Period(this.value.minus(((Time)value).value));
		else if (value instanceof Period)
			return new Time(this.value.minus(((Period)value).value));
		else
			throw new SyntaxError("Illegal: Time - " + value.getClass().getSimpleName());
	}

	public static ResultInfo compileMinus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags);
		if(right.getType()==PromptoTime.class) {
			MethodConstant oper = new MethodConstant(PromptoTime.class, "minus", 
					PromptoTime.class, PromptoPeriod.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(PromptoPeriod.class);
		} else if(right.getType()==PromptoPeriod.class) {
			MethodConstant oper = new MethodConstant(PromptoTime.class, "minus", PromptoPeriod.class, PromptoTime.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(PromptoTime.class);
			
		} else
			throw new SyntaxError("Illegal: Date + " + exp.getClass().getSimpleName());
	}

	@Override
	public int compareTo(Context context, IValue value) {
		if (value instanceof Time)
			return this.value.compareTo(((Time) value).value);
		else
			throw new SyntaxError("Illegal comparison: Time + " + value.getClass().getSimpleName());
	}
	
	public static ResultInfo compileCompareTo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(PromptoTime.class, 
				"compareTo", PromptoTime.class, int.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return BaseValue.compileCompareToEpilogue(method, flags);
	}


	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) throws PromptoError {
		String name = id.toString();
		if ("hour".equals(name))
			return new Integer(this.value.getNativeHour());
		else if ("minute".equals(name))
			return new Integer(this.value.getNativeMinute());
		else if ("second".equals(name))
			return new Integer(this.value.getNativeSecond());
		else if ("millisecond".equals(name))
			return new Integer(this.value.getNativeMillis());
		else
			return super.getMember(context, id, autoCreate);
	}

	@Override
	public Object convertTo(Context context, Type type) {
		return value;
	}

	public long getMillisOfDay() {
		return value.getNativeMillisOfDay();
	}

	@Override
	public int compareTo(Time other) {
		return value.compareTo(other.value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Time)
			return value.equals(((Time) obj).value);
		else
			return value.equals(obj);
	}

	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				PromptoTime.class, 
				"equals",
				Object.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		if(flags.isReverse()) 
			CompilerUtils.reverseBoolean(method);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				generator.writeString(TimeType.instance().getTypeName());
				generator.writeFieldName("value");
				generator.writeString(this.toString());
				generator.writeEndObject();
			} else
				generator.writeString(this.toString());
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}
	
}
