package prompto.type;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDocument;
import prompto.intrinsic.PromptoList;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.IValue;


public class AnyType extends NativeType { 

	static AnyType instance = new AnyType();
	
	public static AnyType instance() {
		return instance;
	}
	
	private AnyType() {
		super(Family.ANY);
	}

	@Override
	public String getTypeName() {
		return "any";
	}
	
	@Override
	public Type getJavaType(Context context) {
		return Object.class;
	}
	
	@Override
	public IType checkItem(Context context, IType itemType) {
		return AnyType.instance(); // needed to support lists in Documents
	}
	
	@Override
	public IType checkMember(Context context, Identifier name) {
		return AnyType.instance(); // needed to support members in Documents
	}

	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return true;
	}
	
	public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

	
	static Map<Class<?>, BiFunction<Context, Object,IValue>> converters = buildConvertersMap();
	
	private static Map<Class<?>, BiFunction<Context, Object, IValue>> buildConvertersMap() {
		Map<Class<?>, BiFunction<Context, Object,IValue>> map = new HashMap<>();
		map.put(Boolean.class, (c,o)->prompto.value.Boolean.valueOf((Boolean)o));
		map.put(Long.class, (c,o)->new prompto.value.Integer((Long)o));
		map.put(Double.class, (c,o)->new prompto.value.Decimal((Double)o));
		map.put(String.class, (c,o)->new prompto.value.Text((String)o));
		map.put(PromptoDocument.class, (c,o)->new prompto.value.Document(c, (PromptoDocument<?,?>)o));
		map.put(PromptoList.class, (c,o)->new prompto.value.ListValue(c, (PromptoList<?>)o));
		return map;
	}

	@Override
	public IValue convertJavaValueToIValue(Context context, Object value) {
		if(value!=null) {
			BiFunction<Context, Object, IValue> converter = converters.get(value.getClass());
			if(converter!=null)
				return converter.apply(context, value);
		}
		return super.convertJavaValueToIValue(context, value);
	}
	
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				Object.class, 
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
	public void declare(Transpiler transpiler) {
		 // nothing to do
	}
	
	@Override
	public void declareItem(Transpiler transpiler, IType itemType, IExpression item) {
	    // required to support Document items
		itemType.declare(transpiler);
	    item.declare(transpiler);
	}
	
	@Override
	public void transpileItem(Transpiler transpiler, IType itemType, IExpression item) {
	    // required to support Document items
	    transpiler.append(".item(");
	    item.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void transpileAssignMemberValue(Transpiler transpiler, String name, IExpression expression) {
	    // required to support Document members
	    transpiler.append(".setMember('").append(name).append("', ");
	    expression.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void transpileAssignItemValue(Transpiler transpiler, IExpression item, IExpression expression) {
	    // required to support Document members
	    transpiler.append(".setItem(");
	    item.transpile(transpiler);
	    transpiler.append(", ");
	    expression.transpile(transpiler);
	    transpiler.append(")");
	}

}
