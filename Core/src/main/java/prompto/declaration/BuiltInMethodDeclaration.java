package prompto.declaration;

import prompto.argument.IArgument;
import prompto.compiler.ClassFile;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Context.BuiltInContext;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public abstract class BuiltInMethodDeclaration extends BaseMethodDeclaration {

	public BuiltInMethodDeclaration(String name) {
		super(new Identifier(name), null, null);
	}

	public BuiltInMethodDeclaration(String name, IArgument argument) {
		super(new Identifier(name), new ArgumentList(argument), null);
	}

	public BuiltInMethodDeclaration(String name, IArgument ... arguments) {
		super(new Identifier(name), new ArgumentList(arguments), null);
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public boolean isTemplate() {
		return false;
	}

	@Override
	public void check(ConcreteCategoryDeclaration declaration, Context context) {
		// nothing to do
	}
	
	@Override
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		throw new UnsupportedOperationException("Should never get there!");
	}

	@Override
	public String compileTemplate(Context context, boolean isStart, ClassFile classFile) {
		throw new UnsupportedOperationException("Should never get there!");
	}
	
	protected IValue getValue(Context context) {
		do {
			if(context instanceof BuiltInContext)
				return ((BuiltInContext)context).getValue();
			context = context.getParentContext();
		} while(context!=null);
		throw new InternalError("Could not locate context for built-in value!");
	}

	public boolean hasCompileExactInstanceMember() {
		return false;
	}

	public ResultInfo compileExactInstanceMember(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments) {
		throw new UnsupportedOperationException("Should never get there!");
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		throw new UnsupportedOperationException();
	}

	public void declareCall(Transpiler transpiler) {
		// override only of required
	}

	public abstract void transpileCall(Transpiler transpiler, ArgumentAssignmentList assignments);



}
