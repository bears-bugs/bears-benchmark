package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.ClosureValue;
import prompto.value.IValue;

public class DeclarationStatement<T extends IDeclaration> extends BaseStatement {

	T declaration;
	
	@SuppressWarnings("unchecked")
	public DeclarationStatement(T declaration) {
		this.declaration = declaration;
		if(declaration instanceof IMethodDeclaration)
			((IMethodDeclaration)declaration).setDeclarationOf((DeclarationStatement<IMethodDeclaration>)this);
	}
	
	public T getDeclaration() {
		return declaration;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(declaration instanceof ConcreteMethodDeclaration) try {
			ConcreteMethodDeclaration method = (ConcreteMethodDeclaration)declaration;
			writer.getContext().registerDeclaration(method);
		} catch(SyntaxError e) {
			// ok
		}
		declaration.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		if(declaration instanceof ConcreteMethodDeclaration) {
			ConcreteMethodDeclaration method = (ConcreteMethodDeclaration)declaration;
			method.checkChild(context);
			context.registerDeclaration(method);
		} else
			throw new SyntaxError("Unsupported:" + declaration.getClass().getSimpleName());
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(declaration instanceof IMethodDeclaration) {
			IMethodDeclaration decl = (IMethodDeclaration)declaration;
			context.registerDeclarationIfMissing(decl);
			MethodType type = new MethodType(decl);
			context.registerValue(new Variable(decl.getId(), type)); 
			context.setValue(decl.getId(), new ClosureValue(context, type));
			return null;
		} else
			throw new SyntaxError("Unsupported:" + declaration.getClass().getSimpleName());
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		if(declaration instanceof ConcreteMethodDeclaration) {
			ConcreteMethodDeclaration decl = (ConcreteMethodDeclaration)declaration;
			context.registerDeclarationIfMissing(decl);
			decl.compileClosureClass(context, method);
			return new ResultInfo(void.class);		
		} else
			throw new SyntaxError("Unsupported:" + declaration.getClass().getSimpleName());
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		if(declaration instanceof ConcreteMethodDeclaration) {
			this.declaration.declareChild(transpiler);
		    transpiler.getContext().registerDeclaration((ConcreteMethodDeclaration)this.declaration);
		} else
			throw new SyntaxError("Unsupported:" + declaration.getClass().getSimpleName());
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		if(declaration instanceof ConcreteMethodDeclaration) {
		    this.declaration.transpile(transpiler);
		    transpiler.getContext().registerDeclaration((ConcreteMethodDeclaration)this.declaration);
		    if(transpiler.getContext().getClosestInstanceContext()!=null) {
		        String name = this.declaration.getTranspiledName(transpiler.getContext());
		        transpiler.append(name).append(" = ").append(name).append(".bind(this);").newLine();
		    }
		    return true;
		} else
			throw new SyntaxError("Unsupported:" + declaration.getClass().getSimpleName());
	}

	
}
