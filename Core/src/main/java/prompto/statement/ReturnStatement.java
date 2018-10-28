package prompto.statement;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.FieldConstant;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.ResultInfo.Flag;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.intrinsic.PromptoRoot;
import prompto.runtime.Context;
import prompto.runtime.VoidResult;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.NullValue;

public class ReturnStatement extends SimpleStatement {
	
	IExpression expression;
	
	public ReturnStatement(IExpression expression) {
		this.expression = expression;
	}

	public IExpression getExpression() {
		return expression;
	}
	
	@Override
	public boolean canReturn() {
		return true;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("return");
		if(expression!=null) {
			writer.append(" ");
			expression.toDialect(writer);
		}
	}
	
	@Override
	public String toString() {
		return "return " + (expression==null ? "" : expression.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof ReturnStatement))
			return false;
		ReturnStatement other = (ReturnStatement)obj;
		return this.getExpression().equals(other.getExpression());
	}
	
	@Override
	public IType check(Context context) {
		return expression==null ? VoidType.instance() : expression.check(context);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(expression==null)
			return VoidResult.instance();
		else {
			IValue value = expression.interpret(context);
			return value==null ? NullValue.instance() : value;
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		if(flags.setter()!=null)
			return compileSetter(context, method, flags);
		else if(flags.variable()!=null)
			return compileVariable(context, method, flags);
		else
			return compileReturn(context, method, flags);
	}
	
	private ResultInfo compileVariable(Context context, MethodInfo method, Flags flags) {
		String name = flags.variable();
		if(expression!=null) {
			ResultInfo info = expression.compile(context, method, flags.withPrimitive(false));
			StackLocal result = method.registerLocal(name, VerifierType.ITEM_Object, new ClassConstant(info.getType()));
			CompilerUtils.compileASTORE(method, result);
		}
		return new ResultInfo(void.class, Flag.RETURN);
	}

	private ResultInfo compileSetter(Context context, MethodInfo method, Flags flags) {
		FieldInfo field = flags.setter();
		// load 'this'
		StackLocal local = method.getRegisteredLocal("this");
		ClassConstant c = ((StackLocal.ObjectLocal)local).getClassName();
		method.addInstruction(Opcode.ALOAD_0, c); 
		// load value
		expression.compile(context, method, flags);
		// store in field
		String name = field.getName().getValue();
		FieldConstant f = new FieldConstant(c, name, field.getType());
		method.addInstruction(Opcode.PUTFIELD, f);
		// also store data in storable
		MethodConstant m = new MethodConstant(PromptoRoot.class, "setStorable", String.class, Object.class, void.class);
		method.addInstruction(Opcode.ALOAD_0, c);
		method.addInstruction(Opcode.LDC, new StringConstant(name));
		method.addInstruction(Opcode.ALOAD_1, new ClassConstant(Object.class));
		method.addInstruction(Opcode.INVOKESPECIAL, m);
		// done
		method.addInstruction(Opcode.RETURN);
		return new ResultInfo(void.class, Flag.RETURN);
	}

	private ResultInfo compileReturn(Context context, MethodInfo method, Flags flags) {
		if(expression==null) {
			method.addInstruction(Opcode.RETURN);
			return new ResultInfo(void.class, Flag.RETURN);
		} else {
			ResultInfo info = expression.compile(context, method, flags);
			if(flags.toPrimitive()) {
				if(boolean.class==info.getType()) {
					method.addInstruction(Opcode.IRETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				} else if(int.class==info.getType()) {
					method.addInstruction(Opcode.IRETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				} else if(char.class==info.getType()) {
					method.addInstruction(Opcode.IRETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				} else if(long.class==info.getType()) {
					method.addInstruction(Opcode.LRETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				} else if(double.class==info.getType()) {
					method.addInstruction(Opcode.DRETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				} else {
					method.addInstruction(Opcode.ARETURN);
					return new ResultInfo(info.getType(), Flag.RETURN);
				}
			} else {
				if(boolean.class==info.getType())
					info = CompilerUtils.booleanToBoolean(method);
				 else if(int.class==info.getType())
				 	info = CompilerUtils.intTolong(method);
				else if(char.class==info.getType())
					info = CompilerUtils.charToCharacter(method);
				else if(long.class==info.getType())
					info = CompilerUtils.longToLong(method);
				else if(double.class==info.getType())
					info = CompilerUtils.doubleToDouble(method);
				method.addInstruction(Opcode.ARETURN);
				return new ResultInfo(info.getType(), Flag.RETURN);
			}
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		if(this.expression!=null)
	        this.expression.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("return");
	    if(this.expression!=null) {
	        transpiler.append(" ");
	        this.expression.transpile(transpiler);
	    }
		return false;
	}

}
