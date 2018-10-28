package prompto.expression;

import java.util.HashMap;
import java.util.Map;

import prompto.compiler.Flags;
import prompto.compiler.ISlicerFunction;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoRange;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.utils.CodeWriter;
import prompto.value.ISliceable;
import prompto.value.IValue;
import prompto.value.Integer;
import prompto.value.ListValue;
import prompto.value.RangeBase;
import prompto.value.Text;

public class SliceSelector extends SelectorExpression {

	IExpression first;
	IExpression last;
	
	public SliceSelector(IExpression first, IExpression last) {
		this.first = first;
		this.last = last;
	}
	
	public SliceSelector(IExpression parent, IExpression first, IExpression last) {
		super(parent);
		this.first = first;
		this.last = last;
	}

	public IExpression getFirst() {
		return first;
	}
	
	public IExpression getLast() {
		return last;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append('[');
		if(first!=null)
			first.toDialect(writer);
		writer.append(':');
		if(last!=null)
			last.toDialect(writer);
		writer.append(']');
	}
	
	@Override
	public IType check(Context context) {
		IType firstType = first!=null ? first.check(context) : null;
		IType lastType = last!=null ? last.check(context) : null;
		if(firstType!=null && !(firstType instanceof IntegerType))
			throw new SyntaxError(firstType.toString() + " is not an integer");
		if(lastType!=null && !(lastType instanceof IntegerType))
			throw new SyntaxError(lastType.toString() + " is not an integer");
		IType parentType = parent.check(context);
		return parentType.checkSlice(context);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
        IValue o = parent.interpret(context);
        if (o == null)
            throw new NullReferenceError();
        ISliceable<IValue> sliceable = o.asSliceable(context);
        if (sliceable!=null)
        {
            Object fi = first != null ? first.interpret(context) : null;
            if (fi != null && !(fi instanceof Integer))
                throw new SyntaxError("Illegal sliced type: " + fi);
            Object li = last != null ? last.interpret(context) : null;
            if (li != null && !(li instanceof Integer))
                throw new SyntaxError("Illegal sliced type: " + li);
            return sliceable.slice((Integer)fi, (Integer)li);
        }
        else
			throw new SyntaxError("Illegal sliced object: " + parent);
	}

	static Map<Class<?>, ISlicerFunction> slicers = createSlicers();
	
	private static Map<Class<?>, ISlicerFunction> createSlicers() {
		Map<Class<?>, ISlicerFunction> map = new HashMap<>(); 
		map.put(String.class, Text::compileSlice); 
		map.put(PromptoRange.Character.class, RangeBase::compileSlice);
		map.put(PromptoRange.Date.class, RangeBase::compileSlice);
		map.put(PromptoRange.Time.class, RangeBase::compileSlice);
		map.put(PromptoRange.Long.class, RangeBase::compileSlice);
		/*map.put(PromptoTuple.class, RangeBase::compileSlice)*/;
		map.put(PromptoList.class, ListValue::compileSlice);
		return map;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo pinfo = parent.compile(context, method, flags.withPrimitive(false));
		ISlicerFunction slicer = slicers.get(pinfo.getType());
		if(slicer==null) {
			System.err.println("Missing ISlicerFunction for slice " + pinfo.getType().getTypeName());
			throw new SyntaxError("Cannot slice " + pinfo.getType().getTypeName());
		}
		return slicer.compile(context, method, flags, pinfo, first, last);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.parent.declare(transpiler);
	    IType parentType = this.parent.check(transpiler.getContext());
	    parentType.declareSlice(transpiler, this.first, this.last);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.parent.transpile(transpiler);
	    IType parentType = this.parent.check(transpiler.getContext());
	    parentType.transpileSlice(transpiler, this.first, this.last);
	    return false;
	}

}
