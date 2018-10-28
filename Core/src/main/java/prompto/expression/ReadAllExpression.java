package prompto.expression;

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
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.ResourceType;
import prompto.type.TextType;
import prompto.utils.CodeWriter;
import prompto.value.IResource;
import prompto.value.IValue;
import prompto.value.Text;

public class ReadAllExpression implements IExpression {

	IExpression resource;
	
	public ReadAllExpression(IExpression resource) {
		this.resource = resource;
	}
	

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("read all from ");
		resource.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		context = context.newResourceContext();
		IType sourceType = resource.check(context);
		if(!(sourceType instanceof ResourceType))
			throw new SyntaxError("Not a readable resource!");
		return TextType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		context = context.newResourceContext();
		IValue o = resource.interpret(context);
		if(o==null)
			throw new NullReferenceError();
		if(!(o instanceof IResource))
			throw new InternalError("Illegal read source: " + o);
		IResource res = (IResource)o;
		if(!res.isReadable())
			throw new InvalidResourceError("Not readable");
		try {
			return new Text(res.readFully());
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		} finally {
			res.close();
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		context = context.newResourceContext();
		/*ResultInfo info = */resource.compile(context, method, flags);
		method.addInstruction(Opcode.DUP);
		InterfaceConstant c = new InterfaceConstant(IResource.class, "readFully", String.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, c);
		method.addInstruction(Opcode.SWAP); // string <-> resource
		c = new InterfaceConstant(IResource.class, "close", void.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, c);
		return new ResultInfo(String.class);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.resource.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.resource.transpile(transpiler);
	    transpiler.append(".readFully()");
		return false;
	}
}
