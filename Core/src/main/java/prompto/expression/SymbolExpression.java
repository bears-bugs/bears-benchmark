package prompto.expression;

import java.lang.reflect.Type;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class SymbolExpression implements IExpression {

	Identifier id;
	
	public SymbolExpression(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}
	
	public String getName() {
		return id.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(id);
	}
	
	@Override
	public IType check(Context context) {
		Symbol symbol = context.getRegisteredSymbol(id, true);
		if(symbol==null)
			throw new SyntaxError("Unknown symbol:" + id);
		return symbol.check(context);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		Context gc = context.getGlobalContext();
		return gc.getValue(id, ()-> {
			Symbol symbol = gc.getRegisteredSymbol(id, true);
			if(symbol==null)
				throw new SyntaxError("Unknown symbol:" + id);
			return symbol.interpret(context);	
		});
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		Symbol symbol = context.getRegisteredSymbol(id, true);
		if(symbol==null)
			throw new SyntaxError("Unknown symbol:" + id);
		return symbol.compile(context, method, flags);			
	}

	public Type getJavaType(Context context) {
		Symbol symbol = context.<Symbol>getRegisteredValue(Symbol.class, id);
		if(symbol==null)
			throw new SyntaxError("Unknown symbol:" + id);
		return symbol.getJavaType(context);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		Symbol symbol = transpiler.getContext().getRegisteredValue(Symbol.class, this.id);
	    symbol.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		Symbol symbol = transpiler.getContext().getRegisteredValue(Symbol.class, this.id);
	    return symbol.transpile(transpiler);
	}

}
