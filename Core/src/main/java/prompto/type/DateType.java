package prompto.type;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;

import prompto.expression.IExpression;
import prompto.grammar.CmpOp;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDate;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.Date;
import prompto.value.DateRange;
import prompto.value.IValue;
import prompto.value.RangeBase;

import com.fasterxml.jackson.databind.JsonNode;


public class DateType extends NativeType {

	static DateType instance = new DateType();

	public static DateType instance() {
		return instance;
	}

	private DateType() {
		super(Family.DATE);
	}

	@Override
	public Type getJavaType(Context context) {
		return PromptoDate.class;
	}
	
	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other) ||
				other==DateTimeType.instance();
	}

	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		if (other instanceof PeriodType)
			return this; // ignore time section
		return super.checkAdd(context, other, tryReverse);
	}

	@Override
	public IType checkSubstract(Context context, IType other) {
		if (other instanceof PeriodType)
			return this; // ignore time section
		else if (other instanceof DateType)
			return PeriodType.instance();
		else 
			return super.checkSubstract(context, other);
	}

	@Override
	public IType checkCompare(Context context, IType other, ISection section) {
		if (other instanceof DateType)
			return BooleanType.instance();
		if (other instanceof DateTimeType)
			return BooleanType.instance();
		return super.checkCompare(context, other, section);
	}

	@Override
	public IType checkRange(Context context, IType other) {
		if (other instanceof DateType)
			return new RangeType(this);
		return super.checkRange(context, other);
	}

	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
		if ("year".equals(name))
			return IntegerType.instance();
		else if ("month".equals(name))
			return IntegerType.instance();
		else if ("dayOfMonth".equals(name))
			return IntegerType.instance();
		else if ("dayOfYear".equals(name))
			return IntegerType.instance();
		else
			return super.checkMember(context, id);
	}

	@Override
	public RangeBase<?> newRange(Object left, Object right) {
		if (left instanceof Date && right instanceof Date)
			return new DateRange((Date) left, (Date) right);
		return super.newRange(left, right);
	}

	@Override
	public Comparator<Date> getComparator(boolean descending) {
		return descending ?
				new Comparator<Date>() {
					@Override
					public int compare(Date o1, Date o2) {
						return o2.getStorableData().compareTo(o1.getStorableData());
					}
				} :
				new Comparator<Date>() {
					@Override
					public int compare(Date o1, Date o2) {
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
		PromptoDate date = PromptoDate.parse(value.asText());
		return new Date(date);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("LocalDate");
	}
	
	@Override
	public void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if (other == PeriodType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareAdd(transpiler, other, tryReverse, left, right);
	}
	
	@Override
	public void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if (other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".addPeriod(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileAdd(transpiler, other, tryReverse, left, right);
	}
	
	@Override
	public void declareSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	    if (other == PeriodType.instance() || other == DateType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareSubtract(transpiler, other, left, right);
	}
	
	@Override
	public void transpileSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	    if (other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".subtractPeriod(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else if (other == DateType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".subtractDate(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileSubtract(transpiler, other, left, right);
	}
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
		switch(name) {
		case "year":
		case "month":
		case "dayOfMonth":
		case "dayOfYear":
			break;
		default:
			super.declareMember(transpiler, name);
	    }
	}
	
	@Override
	public void transpileMember(Transpiler transpiler, String name) {
		switch(name) {
		case "year":
	        transpiler.append("getYear()");
			break;
		case "month":
	        transpiler.append("getMonth()");
			break;
		case "dayOfMonth":
	        transpiler.append("getDayOfMonth()");
			break;
		case "dayOfYear":
	        transpiler.append("getDayOfYear()");
			break;
		default:
	        super.transpileMember(transpiler, name);
	    }
	}
	
	@Override
	public void declareRange(Transpiler transpiler, IType other) {
	    if(other == DateType.instance()) {
	        transpiler.require("Range");
	        transpiler.require("DateRange");
	    } else {
	        super.declareRange(transpiler, other);
	    }
	}
	
	@Override
	public void transpileRange(Transpiler transpiler, IExpression first, IExpression last) {
	    transpiler.append("new DateRange(");
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
	
	@Override
	public void transpileSorted(Transpiler transpiler, boolean descending, IExpression key) {
	    if(descending)
	        transpiler.append("function(o1, o2) { return o1.equals(o2) ? 0 : o1.gt(o2) ? -1 : 1; }");
	    else
	        transpiler.append("function(o1, o2) { return o1.equals(o2) ? 0 : o1.gt(o2) ? 1 : -1; }");
	}
}
