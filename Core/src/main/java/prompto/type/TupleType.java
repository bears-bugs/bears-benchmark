package prompto.type;

import java.lang.reflect.Type;

import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoTuple;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;

public class TupleType extends ContainerType {

	static TupleType instance = new TupleType();
	
	public static TupleType instance() {
		return instance;
	}
	
	private TupleType() {
		super(Family.TUPLE, AnyType.instance(), "any");
	}
	
	@Override
	public IterableType withItemType(IType itemType) {
		return this;
	}

	@Override
	public Type getJavaType(Context context) {
		return PromptoTuple.class;
	}
	
	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other) ||
				other instanceof ListType ||
				other instanceof SetType;
	}

	@Override
	public IType checkItem(Context context, IType other) {
		if(other==IntegerType.instance())
			return AnyType.instance();
		else
			return super.checkItem(context,other);
	}
	

	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
        if ("count".equals(name))
            return IntegerType.instance();
        else
            return super.checkMember(context, id);
}
	
	@Override
	public IType checkAdd(Context context, IType other, boolean tryReverse) {
		if(other instanceof TupleType || other instanceof ListType || other instanceof SetType)
			return this; 
		return super.checkAdd(context, other, tryReverse);
	}
	
	@Override
	public IType checkContains(Context context, IType other) {
		return BooleanType.instance(); 
	}
	
	@Override
	public IType checkContainsAllOrAny(Context context, IType other) {
		return BooleanType.instance(); 
	}
	
	@Override
	public void declareAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	    if(other == TupleType.instance() || other instanceof ListType || other instanceof SetType) {
	        left.declare(transpiler);
	        right.declare(transpiler);
	    } else {
	        super.declareAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	@Override
	public void transpileAdd(Transpiler transpiler, IType other, boolean tryReverse, IExpression left, IExpression right) {
	   if(other == TupleType.instance() || other instanceof ListType || other instanceof SetType) {
	        left.transpile(transpiler);
	        transpiler.append(".add(");
	        right.transpile(transpiler);
	        transpiler.append(")");
	    } else {
	        super.transpileAdd(transpiler, other, tryReverse, left, right);
	    }
	}
	
	@Override
	public void declareItem(Transpiler transpiler, IType itemType, IExpression item) {
	    if(itemType==IntegerType.instance) {
	        item.declare(transpiler);
	    } else {
	        super.declareItem(transpiler, itemType, item);
	    }
	}
	
	@Override
	public void transpileItem(Transpiler transpiler, IType itemType, IExpression item) {
	    if(itemType==IntegerType.instance()) {
	        transpiler.append("[");
	        item.transpile(transpiler);
	        transpiler.append("-1]");
	    } else {
	        super.transpileItem(transpiler, itemType, item);
	    }	
    }
	
	@Override
	public void transpileAssignItemValue(Transpiler transpiler, IExpression item, IExpression expression) {
	    transpiler.append(".setItem(");
	    item.transpile(transpiler);
	    transpiler.append(", ");
	    expression.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void declareContains(Transpiler transpiler, IType other, IExpression container, IExpression item) {
	    container.declare(transpiler);
	    item.declare(transpiler);
	}
	
	@Override
	public void transpileContains(Transpiler transpiler, IType other, IExpression container, IExpression item) {
	    container.transpile(transpiler);
	    transpiler.append(".includes(");
	    item.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void declareContainsAllOrAny(Transpiler transpiler, IType other, IExpression container, IExpression items) {
	    transpiler.require("StrictSet");
	    container.declare(transpiler);
	    items.declare(transpiler);
	}
	
	@Override
	public void transpileContainsAll(Transpiler transpiler, IType other, IExpression container, IExpression items) {
	    container.transpile(transpiler);
	    transpiler.append(".hasAll(");
	    items.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void transpileContainsAny(Transpiler transpiler, IType other, IExpression container, IExpression items) {
	    container.transpile(transpiler);
	    transpiler.append(".hasAny(");
	    items.transpile(transpiler);
	    transpiler.append(")");
	}
	
}
