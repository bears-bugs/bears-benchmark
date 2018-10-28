package prompto.statement;

import java.io.IOException;

import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.InvalidResourceError;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.runtime.Context.ResourceContext;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.ResourceType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IResource;
import prompto.value.IValue;

public class WriteStatement extends SimpleStatement {

	IExpression content;
	IExpression resource;
	
	public WriteStatement(IExpression content, IExpression resource) {
		this.content = content;
		this.resource = resource;
	}
	

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("write ");
		switch(writer.getDialect()) {
		case E:
		case M:
			content.toDialect(writer);
			break;
		case O:
			writer.append("(");
			content.toDialect(writer);
			writer.append(")");
			break;
		}
		writer.append(" to ");
		resource.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		context = context instanceof ResourceContext ? context : context.newResourceContext();
		IType resourceType = resource.check(context);
		if(!(resourceType instanceof ResourceType))
			throw new SyntaxError("Not a resource!");
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		Context resContext = context instanceof ResourceContext ? context : context.newResourceContext();
		Object o = resource.interpret(resContext);
		if(o==null)
			throw new NullReferenceError();
		if(!(o instanceof IResource))
			throw new InternalError("Illegal write source: " + o);
		IResource res = (IResource)o;
		if(!res.isWritable())
			throw new InvalidResourceError("Not writable");
		try {
			String text = content.interpret(resContext).toString();
			if(context==resContext)
				res.writeLine(text);
			else
				res.writeFully(text);
			return null;
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		} finally {
			if(resContext!=context)
				res.close();
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		Context resContext = context instanceof ResourceContext ? context : context.newResourceContext();
		/*ResultInfo info = */resource.compile(resContext, method, flags);
		if(resContext!=context)
			method.addInstruction(Opcode.DUP);
		content.compile(resContext, method, flags);
		InterfaceConstant c = new InterfaceConstant(IResource.class, "writeFully", String.class, void.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, c);
		if(resContext!=context) {
			c = new InterfaceConstant(IResource.class, "close", void.class);
			method.addInstruction(Opcode.INVOKEINTERFACE, c);
		}
		return new ResultInfo(void.class);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    if(!(transpiler.getContext() instanceof ResourceContext))
	        transpiler = transpiler.newResourceTranspiler();
	    this.resource.declare(transpiler);
	    this.content.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    if (transpiler.getContext() instanceof ResourceContext)
	        this.transpileLine(transpiler);
	    else
	        this.transpileFully(transpiler);
	    return false;
	}


	private void transpileFully(Transpiler transpiler) {
	    transpiler = transpiler.newResourceTranspiler();
	    transpiler.append("var $res = ");
	    this.resource.transpile(transpiler);
	    transpiler.append(";").newLine();
	    transpiler.append("try {").indent();
	    transpiler.append("$res.writeFully(");
	    this.content.transpile(transpiler);
	    transpiler.append(");");
	    transpiler.dedent().append("} finally {").indent();
	    transpiler.append("$res.close();").newLine();
	    transpiler.dedent().append("}");
	    transpiler.flush();
	}


	private void transpileLine(Transpiler transpiler) {
	    this.resource.transpile(transpiler);
	    transpiler.append(".writeLine(");
	    this.content.transpile(transpiler);
	    transpiler.append(")");
	}
}
