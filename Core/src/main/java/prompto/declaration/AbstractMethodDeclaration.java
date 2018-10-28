package prompto.declaration;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import prompto.compiler.ClassFile;
import prompto.compiler.InterfaceType;
import prompto.error.SyntaxError;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class AbstractMethodDeclaration extends BaseMethodDeclaration implements IMethodDeclaration {

	public AbstractMethodDeclaration(Identifier name, ArgumentList arguments, IType returnType) {
		super(name, arguments, returnType!=null ? returnType : VoidType.instance());
	}
	
	@Override
	public boolean isAbstract() {
		return true;
	}
	
	@Override
	public boolean isTemplate() {
		return false;
	}

	@Override
	public IType check(Context context, boolean isStart) {
		if(arguments!=null)
			arguments.check(context);
		if(isStart) {
			Context local = context.newLocalContext();
			registerArguments(local); // will check them
		}
		return returnType;
	}
	
	@Override
	public void check(ConcreteCategoryDeclaration declaration, Context context) {
		if(arguments!=null)
			arguments.check(context);
	}	

	@Override
	public IValue interpret(Context context) {
		throw new SyntaxError("Should never get there !");
	}
	
	@Override
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		throw new SyntaxError("Should never get there !");
	}

	@Override
	public String compileTemplate(Context context, boolean isStart, ClassFile classFile) {
		throw new SyntaxError("Should never get there !");
	}

	public ClassFile compileInterface(Context context, Type type) {
		ClassFile classFile = new ClassFile(type);
		classFile.addModifier(Modifier.ABSTRACT | Modifier.INTERFACE);
		InterfaceType intf = new InterfaceType(arguments, returnType);
		classFile.addAttribute(intf.computeSignature(context, Object.class));
		classFile.addInterface(intf.getInterfaceType());
		return classFile;
	}
	
	
	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	private void toMDialect(CodeWriter writer) {
		writer.append("abstract def ");
		writer.append(getName());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(")");
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("->");
			returnType.toDialect(writer);
		}
	}

	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as abstract method ");
		arguments.toDialect(writer);
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("returning ");
			returnType.toDialect(writer);
		}
	}
	
	protected void toODialect(CodeWriter writer) {
		writer.append("abstract ");
		if(returnType!=null && returnType!=VoidType.instance()) {
			returnType.toDialect(writer);
			writer.append(" ");
		}
		writer.append("method ");
		writer.append(getName());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(");");
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.declareArguments(transpiler);
	}

}
