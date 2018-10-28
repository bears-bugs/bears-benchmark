package prompto.type;

import java.lang.reflect.Type;

import prompto.expression.IExpression;
import prompto.intrinsic.PromptoPeriod;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;



public class PeriodType extends NativeType {

	static PeriodType instance = new PeriodType();
	
	public static PeriodType instance() {
		return instance;
	}
	
	private PeriodType() {
		super(Family.PERIOD);
	}

	@Override
	public Type getJavaType(Context context) {
		return PromptoPeriod.class;
	}
	

	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		if(other instanceof PeriodType)
			return this;
		return super.checkAdd(context, other, tryReverse);
	}
	
	@Override
	public IType checkSubstract(Context context, IType other) {
		if(other instanceof PeriodType)
			return this;
		return super.checkSubstract(context, other);
	}

	@Override
	public IType checkMultiply(Context context, IType other, boolean tryReverse) {
		if(other instanceof IntegerType)
			return this;
		return super.checkMultiply(context, other, tryReverse);
	}

	@Override
	public IType checkCompare(Context context, IType other, ISection section) {
		if(other instanceof PeriodType)
			return BooleanType.instance();
		return super.checkCompare(context, other, section);
	}
	
	@Override
	public void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	   if(other == PeriodType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else {
	        super.declareAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	@Override
	public void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	   if(other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".add(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else {
	        super.transpileAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	@Override
	public void declareMinus(Transpiler transpiler, IExpression expression) {
		 // nothing to do
	}
	
	@Override
	public void transpileMinus(Transpiler transpiler, IExpression expression) {
		expression.transpile(transpiler);
	    transpiler.append(".minus()");
	}
	
	@Override
	public void declareMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if(other == IntegerType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareMultiply(transpiler, other, tryReverse, left, right);
	}
	
	@Override
	public void transpileMultiply(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if(other == IntegerType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".multiply(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileMultiply(transpiler, other, tryReverse, left, right);	
    }
	
	@Override
	public void declareSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	   if(other == PeriodType.instance()) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else
	        super.declareSubtract(transpiler, other, left, right);
	}
	
	@Override
	public void transpileSubtract(Transpiler transpiler, IType other, IExpression left, IExpression right) {
	   if(other == PeriodType.instance()) {
	        left.transpile(transpiler);
	        transpiler.append(".subtract(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        super.transpileSubtract(transpiler, other, left, right);
	}
}
