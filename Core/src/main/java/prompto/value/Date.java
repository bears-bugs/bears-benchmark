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
import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoPeriod;
import prompto.runtime.Context;
import prompto.type.DateType;

import com.fasterxml.jackson.core.JsonGenerator;

public class Date extends BaseValue implements Comparable<Date> {

	public static Date Parse(String text) {
		PromptoDate value = PromptoDate.parse(text);
		return new Date(value);
	}

	PromptoDate value;

	public Date(PromptoDate date) {
		super(DateType.instance());
		this.value = date;

	}

	public Date(int year, int month, int day) {
		super(DateType.instance());
		value = new PromptoDate(year, month, day);
	}

	@Override
	public PromptoDate getStorableData() {
		return value;
	}

	@Override
	public IValue plus(Context context, IValue value) throws PromptoError {
		if (value instanceof Period)
			return new Date(this.value.plus(((Period)value).value));
		else
			throw new SyntaxError("Illegal: Date + " + value.getClass().getSimpleName());
	}

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags);
		if(right.getType()!=PromptoPeriod.class)
			throw new SyntaxError("Illegal: Date + " + exp.getClass().getSimpleName());
		MethodConstant oper = new MethodConstant(PromptoDate.class, "plus", PromptoPeriod.class, PromptoDate.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(PromptoDate.class);
	}
	

	@Override
	public IValue minus(Context context, IValue value) throws PromptoError {
		if (value instanceof Date) {
			PromptoDate other = ((Date) value).value;
			PromptoPeriod result = this.value.minus(other);
			return new Period(result);
		} else if (value instanceof Period)
			return new Date(this.value.minus(((Period)value).value));
		else
			throw new SyntaxError("Illegal: Date - "
					+ value.getClass().getSimpleName());
	}

	public static ResultInfo compileMinus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags);
		if(right.getType()==PromptoDate.class) {
			MethodConstant oper = new MethodConstant(PromptoDate.class, "minus", 
					PromptoDate.class, PromptoPeriod.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(PromptoPeriod.class);
		} else if(right.getType()==PromptoPeriod.class) {
			MethodConstant oper = new MethodConstant(PromptoDate.class, "minus", 
					PromptoPeriod.class, PromptoDate.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(PromptoDate.class);
		} else
			throw new SyntaxError("Illegal: Date - " + exp.getClass().getSimpleName());
	}
	
	@Override
	public int compareTo(Context context, IValue value) throws PromptoError {
		if (value instanceof Date)
			return this.value.compareTo(((Date) value).value);
		else
			throw new SyntaxError("Illegal comparison: Date - "
					+ value.getClass().getSimpleName());

	}
	
	public static ResultInfo compileCompareTo(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(PromptoDate.class, 
				"compareTo", PromptoDate.class, int.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return BaseValue.compileCompareToEpilogue(method, flags);
	}


	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) throws PromptoError {
		String name = id.toString();
		if ("year".equals(name))
			return new Integer(this.value.getNativeYear());
		else if ("month".equals(name))
			return new Integer(this.value.getNativeMonth());
		else if ("dayOfMonth".equals(name))
			return new Integer(this.value.getNativeDayOfMonth());
		else if ("dayOfYear".equals(name))
			return new Integer(this.value.getNativeDayOfYear());
		else
			return super.getMember(context, id, autoCreate);
	}

	@Override
	public Object convertTo(Context context, Type type) {
		return value;
	}

	public Date toDateMidnight() {
		return this;
	}

	public int compareTo(Date other) {
		return value.compareTo(other.value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Date)
			return value.equals(((Date) obj).value);
		else
			return value.equals(obj);
	}
	
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				PromptoDate.class, 
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
		return value.format("yyyy-MM-dd");
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				generator.writeString(DateType.instance().getTypeName());
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
