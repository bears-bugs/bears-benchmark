package prompto.type;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;

import prompto.expression.IExpression;
import prompto.grammar.CmpOp;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoTime;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.IValue;
import prompto.value.RangeBase;
import prompto.value.Time;
import prompto.value.TimeRange;

import com.fasterxml.jackson.databind.JsonNode;


public class TimeType extends NativeType {

	static TimeType instance = new TimeType();

	public static TimeType instance() {
		return instance;
	}

	private TimeType() {
		super(Family.TYPE);
	}

	@Override
	public Type getJavaType(Context context) {
		return PromptoTime.class;
	}

	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other) ||
				other==DateTimeType.instance();
	}

	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		if (other instanceof PeriodType)
			return TimeType.instance();
		return super.checkAdd(context, other, tryReverse);
	}

	@Override
	public IType checkSubstract(Context context, IType other) {
		if (other instanceof TimeType)
			return PeriodType.instance();
		else if (other instanceof PeriodType)
			return DateTimeType.instance();
		else
			return super.checkSubstract(context, other);
	}

	@Override
	public IType checkCompare(Context context, IType other, ISection section) {
		if (other instanceof TimeType)
			return BooleanType.instance();
		return super.checkCompare(context, other, section);
	}

	@Override
	public IType checkRange(Context context, IType other) {
		if (other instanceof TimeType)
			return new RangeType(this);
		return super.checkRange(context, other);
	}

	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
		if ("hour".equals(name))
			return IntegerType.instance();
		else if ("minute".equals(name))
			return IntegerType.instance();
		else if ("second".equals(name))
			return IntegerType.instance();
		else if ("millisecond".equals(name))
			return IntegerType.instance();
		else
			return super.checkMember(context, id);
	}

	@Override
	public RangeBase<?> newRange(Object left, Object right) {
		if (left instanceof Time && right instanceof Time)
			return new TimeRange((Time) left, (Time) right);
		return super.newRange(left, right);
	}

	@Override
	public Comparator<Time> getComparator(boolean descending) {
		return descending ?
				new Comparator<Time>() {
					@Override
					public int compare(Time o1, Time o2) {
						return o2.getStorableData().compareTo(o1.getStorableData());
					}
				} :
				new Comparator<Time>() {
					@Override
					public int compare(Time o1, Time o2) {
						return o1.getStorableData().compareTo(o2.getStorableData());
					}
				};
	}

	@Override
	public String toString(Object value) {
		return "'" + value.toString() + "'";
	}
	
	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		PromptoTime time = PromptoTime.parse(value.asText());
		return new Time(time);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("LocalTime");
	}
	
	@Override
	public void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if (other == PeriodType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else {
	        super.declareAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	@Override
	public void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if (other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".addPeriod(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else {
	        super.transpileAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	
	@Override
	public void declareSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	    if (other == TimeType.instance() || other == PeriodType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareSubtract(transpiler, other, left, right);
	}

	@Override
	public void transpileSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	   if (other == TimeType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".subtractTime(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else if (other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".subtractPeriod(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileSubtract(transpiler, other, left, right);
	}
	
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
		switch(name) {
		case "hour":
		case "minute":
		case "second":
		case "millisecond":
			break;
		default:
			super.declareMember(transpiler, name);
	    }
	}
	
	@Override
	public void transpileMember(Transpiler transpiler, String name) {
		switch(name) {
		case "hour":
	        transpiler.append("getHour()");
			break;
		case "minute":
	        transpiler.append("getMinute()");
			break;
		case "second":
	        transpiler.append("getSecond()");
			break;
		case "millisecond":
	        transpiler.append("getMillisecond()");
			break;
		default:
			super.transpileMember(transpiler, name);
	    }
	}
	
	@Override
	public void declareRange(Transpiler transpiler, IType other) {
	    if(other == TimeType.instance()) {
	        transpiler.require("Range");
	        transpiler.require("TimeRange");
	    } else {
	        super.declareRange(transpiler, other);
	    }
	}
	
	@Override
	public void transpileRange(Transpiler transpiler, IExpression first, IExpression last) {
	    transpiler.append("new TimeRange(");
	    first.transpile(transpiler);
	    transpiler.append(",");
	    last.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void declareCompare(Transpiler transpiler, IType other) {
		// nothing to do
	}
	
	@Override
	public void transpileCompare(Transpiler transpiler, IType other, CmpOp operator, IExpression left, IExpression right) {
	    left.transpile(transpiler);
	    transpiler.append(".");
	    operator.transpile(transpiler);
	    transpiler.append("(");
	    right.transpile(transpiler);
	    transpiler.append(")");
	}
}
