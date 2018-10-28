package prompto.literal;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoTuple;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.TupleType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.value.IValue;
import prompto.value.TupleValue;

public class TupleLiteral extends Literal<TupleValue> {

	ExpressionList expressions = null;
	boolean mutable = false;
	
	public TupleLiteral(boolean mutable) {
		super("()",new TupleValue(mutable));
		this.mutable = mutable;
	}
	
	public TupleLiteral(ExpressionList expressions, boolean mutable) {
		super(()->toTupleString(expressions),new TupleValue(mutable));
		this.expressions = expressions;
		this.mutable = mutable;
	}
	
	
	private static String toTupleString(ExpressionList expressions) {
		return "(" + expressions.toString() + (expressions.size()==1 ? "," : "") + ")";
	}

	public boolean isMutable() {
		return mutable;
	}
	
	@Override
	public IType check(Context context) {
		return TupleType.instance(); 
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(value.isEmpty() && expressions!=null) {
			PromptoTuple<IValue> list = new PromptoTuple<IValue>(mutable);
			for(IExpression exp : expressions) 
				list.add(exp.interpret(context));
			value = new TupleValue(list);
			// don't dispose of expressions, they are required by translation 
		}
		return value;
	}

	public ExpressionList getExpressions() {
		return expressions;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(mutable)
			writer.append("mutable ");
		if(expressions!=null) {
			writer.append('(');
			expressions.toDialect(writer);
			if(expressions.size()==1)
				writer.append(',');
			writer.append(')');
		} else
			writer.append("()");
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = CompilerUtils.compileNewRawInstance(method, PromptoTuple.class);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(mutable ? Opcode.ICONST_1 : Opcode.ICONST_0);
		CompilerUtils.compileCallConstructor(method, PromptoTuple.class, boolean.class);
		if(expressions!=null)
			addItems(context, method, flags);
		return info;
	}

	private void addItems(Context context, MethodInfo method, Flags flags) {
		for(IExpression e : expressions) {
			method.addInstruction(Opcode.DUP); // need to keep a reference to the list on top of stack
			e.compile(context, method, flags.withPrimitive(false));
			IOperand c = new MethodConstant(PromptoTuple.class, "add", 
					Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			method.addInstruction(Opcode.POP); // consume the returned boolean
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("List");
	    transpiler.require("Tuple");
	    this.expressions.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("new Tuple(").append(this.mutable).append(", [");
	    this.expressions.transpile(transpiler);
	    transpiler.append("])");
	    return false;
	}
}
