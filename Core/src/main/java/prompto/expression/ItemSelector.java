package prompto.expression;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import prompto.compiler.ClassConstant;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.IOperatorFunction;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.intrinsic.PromptoAny;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoRange;
import prompto.intrinsic.PromptoSet;
import prompto.intrinsic.PromptoTuple;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Dictionary;
import prompto.value.IValue;
import prompto.value.ListValue;
import prompto.value.NullValue;
import prompto.value.RangeBase;
import prompto.value.SetValue;
import prompto.value.Text;
import prompto.value.TupleValue;

public class ItemSelector extends SelectorExpression {

	IExpression item;
	
	public ItemSelector(IExpression item) {
		this.item = item;
	}
	
	public ItemSelector(IExpression parent, IExpression item) {
		super(parent);
		this.item = item;
	}

	public IExpression getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		return parent.toString() + '[' + item.toString() + ']';
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append("[");
		item.toDialect(writer);
		writer.append("]");
	}
	
	@Override
	public IType check(Context context) {
		IType parentType = parent.check(context);
		IType itemType = item.check(context);
		return parentType.checkItem(context,itemType);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue o = parent.interpret(context);
        if (o == null || o==NullValue.instance())
            throw new NullReferenceError();
        IValue i = item.interpret(context);
        if (i == null || i==NullValue.instance())
            throw new NullReferenceError();
        return o.getItem(context, i);
	}
	
	static Map<Class<?>, IOperatorFunction> getters = createGetters();
	
	private static Map<Class<?>, IOperatorFunction> createGetters() {
		Map<Class<?>, IOperatorFunction> map = new HashMap<>(); 
		map.put(String.class, Text::compileItem); 
		map.put(PromptoRange.Character.class, RangeBase::compileItem);
		map.put(PromptoRange.Date.class, RangeBase::compileItem);
		map.put(PromptoRange.Time.class, RangeBase::compileItem);
		map.put(PromptoRange.Long.class, RangeBase::compileItem);
		map.put(PromptoDict.class, Dictionary::compileItem);
		map.put(PromptoTuple.class, TupleValue::compileItem);
		map.put(PromptoSet.class, SetValue::compileItem);
		map.put(PromptoList.class, ListValue::compileItem);
		map.put(PromptoAny.class, ItemSelector::compileAnyItem);
		map.put(PromptoDocument.class, ItemSelector::compileAnyItem);
		return map;
	}
	
	public static ResultInfo compileAnyItem(Context context, MethodInfo method, Flags flags, ResultInfo parentInfo, IExpression expression) {
		expression.compile(context, method, flags);
		IOperand oper = new MethodConstant(PromptoAny.class, "getItem", Object.class, Object.class, Object.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoAny.class);
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IType type = check(context);
		ResultInfo parentInfo = parent.compile(context, method, flags);
		ResultInfo itemInfo = compileGetItem(context, method, flags, parentInfo, item);
		if(Object.class==itemInfo.getType()) {
			// need to downcast
			Type klass = type.getJavaType(context);
			ClassConstant c = new ClassConstant(klass);
			method.addInstruction(Opcode.CHECKCAST, c);
			return new ResultInfo(klass);
		} else
			return itemInfo;
	}
	
	public static ResultInfo compileGetItem(Context context, MethodInfo method, Flags flags, ResultInfo parentInfo, IExpression item) {
		IOperatorFunction getter = getters.get(parentInfo.getType());
		if(getter==null) {
			System.err.println("Missing IOperatorFunction for get item " + parentInfo.getType().getTypeName());
			throw new SyntaxError("Cannot get item from " + parentInfo.getType().getTypeName());
		}
		return getter.compile(context, method, flags, parentInfo, item);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		IType parentType = this.parent.check(transpiler.getContext());
		IType itemType = this.item.check(transpiler.getContext());
		parentType.declareItem(transpiler, itemType, this.item);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.parent.transpile(transpiler);
	    IType parentType = this.parent.check(transpiler.getContext());
	    IType itemType = this.item.check(transpiler.getContext());
	    parentType.transpileItem(transpiler, itemType, this.item);
	    return false;
	}
	

}
