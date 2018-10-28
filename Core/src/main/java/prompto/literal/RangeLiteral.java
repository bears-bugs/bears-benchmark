package prompto.literal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoRange;
import prompto.intrinsic.PromptoTime;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class RangeLiteral implements IExpression {

	IExpression first;
	IExpression last;
	
	public RangeLiteral(IExpression first, IExpression last) {
		this.first = first;
		this.last = last;
	}

	@Override
	public String toString() {
		return "[" + first.toString() + ".." + last.toString() + "]";
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("[");
		first.toDialect(writer);
		writer.append("..");
		last.toDialect(writer);
		writer.append("]");
	}
	
	static Map<Type,Type> rangeClassMap = createRangeClassMap();
	
	private static Map<Type, Type> createRangeClassMap() {
		Map<Type,Type> map = new HashMap<>();
		map.put(char.class, PromptoRange.Character.class);
		map.put(Character.class, PromptoRange.Character.class);
		map.put(long.class, PromptoRange.Long.class);
		map.put(Long.class, PromptoRange.Long.class);
		map.put(PromptoDate.class, PromptoRange.Date.class);
		map.put(PromptoTime.class, PromptoRange.Time.class);
		return map;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IType itemType = checkType(context, first);
		Type itemKlass = itemType.getJavaType(context);
		Type rangeKlass = rangeClassMap.get(itemKlass);
		if(rangeKlass==null) {
			System.err.println("Missing PromptoRange for = " + itemType.getFamily());
			throw new SyntaxError("Cannot build Range of " + itemType.getFamily());
		}
		CompilerUtils.compileNewRawInstance(method, rangeKlass);
		method.addInstruction(Opcode.DUP); // need to keep a reference on top of stack
		first.compile(context, method, flags.withPrimitive(false));
		last.compile(context, method, flags.withPrimitive(false));
		return CompilerUtils.compileCallConstructor(method, rangeKlass, itemKlass, itemKlass);
	}

	public IExpression getFirst() {
		return first;
	}
	
	public IExpression getLast() {
		return last;
	}
	
	@Override
	public IType check(Context context) {
		return checkType(context, first).checkRange(context, checkType(context, last));
	}
	
	private static IType checkType(Context context, IExpression exp) {
		IType type = exp.check(context);
		if(!"IntegerLimits".equals(type.getTypeName()))
			return type;
		else
			return IntegerType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IType type = checkType(context, first);
		Object of = first.interpret(context);
		Object ol = last.interpret(context);
		return type.newRange(of,ol);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.first.declare(transpiler);
	    IType firstType = this.first.check(transpiler.getContext());
	    firstType.declare(transpiler);
	    this.last.declare(transpiler);
	    IType lastType = this.last.check(transpiler.getContext());
	    lastType.declare(transpiler);
	    firstType.declareRange(transpiler, lastType);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		IType firstType = this.first.check(transpiler.getContext());
	    firstType.transpileRange(transpiler, this.first, this.last);
	    return false;
	}

}
