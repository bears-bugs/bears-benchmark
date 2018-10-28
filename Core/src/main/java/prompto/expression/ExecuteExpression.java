package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.parser.ISection;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.CodeValue;
import prompto.value.IValue;

public class ExecuteExpression extends Section implements IExpression, ISection {

	Identifier id;
	
	public ExecuteExpression(Identifier id) {
		this.id = id;
	}

	public Identifier getName() {
		return id;
	}
		
	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			writer.append("execute: ");
			writer.append(id);
			break;
		case O:
		case M:
			writer.append("execute(");
			writer.append(id);
			writer.append(")");
			break;
		}
	}
	
	@Override
	public IType check(Context context) {
		try {
			IValue value = context.getValue(id);
			if(value instanceof CodeValue)
				return ((CodeValue) value).check(context);
			else
				throw new SyntaxError("Expected code, got:" + value.toString());
		} catch(PromptoError e) {
			throw new SyntaxError(e.getMessage());
		}
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue value = context.getValue(id);
		if(value instanceof CodeValue)
			return ((CodeValue) value).interpret(context);
		else
			throw new SyntaxError("Expected code, got:" + value.toString());
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IValue value = context.getValue(id);
		if(value instanceof CodeValue)
			return ((CodeValue) value).compile(context, method, flags);
		else
			throw new SyntaxError("Expected code, got:" + value.toString());
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		CodeValue value = (CodeValue)transpiler.getContext().getValue(this.id);
		value.declareCode(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("(");
	    CodeValue value = (CodeValue)transpiler.getContext().getValue(this.id);
	    value.transpileCode(transpiler);
	    transpiler.append(")");
		return false;
	}
	
}
