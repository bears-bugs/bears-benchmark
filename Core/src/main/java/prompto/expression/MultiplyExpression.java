package prompto.expression;

import java.util.HashMap;
import java.util.Map;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.IOperatorFunction;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.declaration.CategoryDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Character;
import prompto.value.Decimal;
import prompto.value.IMultiplyable;
import prompto.value.IValue;
import prompto.value.Integer;
import prompto.value.Text;

public class MultiplyExpression implements IExpression {

	IExpression left;
	IExpression right;
	
	public MultiplyExpression(IExpression left, IExpression right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		left.toDialect(writer);
		writer.append(" * ");
		right.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		IType lt = left.check(context);
		IType rt = right.check(context);
		return lt.checkMultiply(context, rt, true);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
        return lval.multiply(context, rval);
	}

	static Map<Class<?>, IOperatorFunction> multipliers = createMultipliers();
	
	private static Map<Class<?>, IOperatorFunction> createMultipliers() {
		Map<Class<?>, IOperatorFunction> map = new HashMap<>(); 
		map.put(String.class, Text::compileMultiply); 
		map.put(java.lang.Character.class, Character::compileMultiply); 
		map.put(double.class, Decimal::compileMultiply);
		map.put(Double.class, Decimal::compileMultiply); 
		map.put(long.class, Integer::compileMultiply);
		map.put(Long.class, Integer::compileMultiply); 
		return map;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo lval = left.compile(context, method, flags);
		IOperatorFunction multiplier = multipliers.get(lval.getType());
		if(multiplier==null && lval.getType().getTypeName().startsWith("π.χ."))
			multiplier = CategoryDeclaration::compileMultiply;
		if(multiplier!=null)
			return multiplier.compile(context, method, flags, lval, right);
		else if(IMultiplyable.class.isAssignableFrom((Class<?>)lval.getType())) // TODO for now
			return compileMultiplyable(context, method, lval, flags);
		else {
			System.err.println("Missing IOperatorFunction for multiply " + lval.getType().getTypeName());
			throw new SyntaxError("Cannot multiply " + lval.getType().getTypeName() + " with " + right.check(context).getFamily());
		}
	}

	private ResultInfo compileMultiplyable(Context context, MethodInfo method, ResultInfo lval, Flags flags) {
		try {
			ResultInfo rval = right.compile(context, method, flags);
			// for now we only support multiply by Integer
			if(Long.class==rval.getType())
				CompilerUtils.LongToint(method);
			else
				CompilerUtils.longToint(method);
			Class<?> klass = (Class<?>)lval.getType();
			Class<?> resultType = klass.getMethod("multiply", int.class).getReturnType();
			IOperand oper = new MethodConstant(lval.getType(), "multiply", 
					int.class, resultType);
			method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
			return new ResultInfo(resultType);
		} catch(NoSuchMethodException e) {
			throw new SyntaxError(e.getMessage());
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    IType lt = this.left.check(transpiler.getContext());
	    IType rt = this.right.check(transpiler.getContext());
	    lt.declareMultiply(transpiler, rt, true, this.left, this.right);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		IType lt = this.left.check(transpiler.getContext());
		IType rt = this.right.check(transpiler.getContext());
	    lt.transpileMultiply(transpiler, rt, true, this.left, this.right);
	    return false;
	}
}
