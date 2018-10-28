package prompto.expression;

import java.util.HashMap;
import java.util.Map;

import prompto.compiler.Flags;
import prompto.compiler.IOperatorFunction;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.declaration.CategoryDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Decimal;
import prompto.value.IValue;
import prompto.value.Integer;

public class DivideExpression implements IExpression {

	IExpression left;
	IExpression right;
	
	public DivideExpression(IExpression left, IExpression right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		left.toDialect(writer);
		writer.append(" / ");
		right.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		IType lt = left.check(context);
		IType rt = right.check(context);
		return lt.checkDivide(context,rt);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
        return lval.divide(context, rval);
	}

	static Map<Class<?>, IOperatorFunction> dividers = createDividers();
	
	private static Map<Class<?>, IOperatorFunction> createDividers() {
		Map<Class<?>, IOperatorFunction> map = new HashMap<>();
		map.put(double.class, Decimal::compileDivide);
		map.put(Double.class, Decimal::compileDivide);
		map.put(long.class, Integer::compileDivide);
		map.put(Long.class, Integer::compileDivide);
		return map;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo lval = left.compile(context, method, flags);
		IOperatorFunction divider = dividers.get(lval.getType());
		if(divider==null && lval.getType().getTypeName().startsWith("π.χ."))
			divider = CategoryDeclaration::compileDivide;
		if(divider==null) {
			System.err.println("Missing IOperatorFunction for divide " + lval.getType().getTypeName());
			throw new SyntaxError("Cannot divide " + lval.getType().getTypeName() + " by " + right.check(context).getFamily());
		}
		return divider.compile(context, method, flags, lval, right);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    IType lt = this.left.check(transpiler.getContext());
	    IType rt = this.right.check(transpiler.getContext());
	    lt.declareDivide(transpiler, rt, this.left, this.right);
	}
	
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		IType lt = this.left.check(transpiler.getContext());
		IType rt = this.right.check(transpiler.getContext());
	    lt.transpileDivide(transpiler, rt, this.left, this.right);
	    return false;
	}
	

}
