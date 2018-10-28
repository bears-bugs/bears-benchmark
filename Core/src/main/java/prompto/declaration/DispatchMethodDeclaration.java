package prompto.declaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prompto.argument.AttributeArgument;
import prompto.argument.CategoryArgument;
import prompto.argument.IArgument;
import prompto.argument.UnresolvedArgument;
import prompto.compiler.ClassFile;
import prompto.grammar.ArgumentList;
import prompto.runtime.Context;
import prompto.statement.MethodCall;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;


public class DispatchMethodDeclaration extends BaseMethodDeclaration {

	Context context;
	MethodCall call;
	IMethodDeclaration declaration;
	List<IMethodDeclaration> declarations;
	
	public DispatchMethodDeclaration(Context context, MethodCall call, IMethodDeclaration declaration, List<IMethodDeclaration> declarations) {
		super(declaration.getId(), declaration.getArguments(), declaration.getReturnType());
		this.context = context;
		this.call = call;
		this.declaration = declaration;
		this.declarations = declarations;
	}

	@Override
	public String getTranspiledName(Context context) {
		return "$dispatch$" + this.declaration.getTranspiledName(context);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.registerArguments(transpiler.getContext());
	    this.transpileProlog(transpiler);
	    this.transpileDispatch(transpiler);
	    this.transpileEpilog(transpiler);
		return true;
	}

	private void transpileDispatch(Transpiler transpiler) {
		Set<IArgument> common = this.collectCommonArguments();
	    for(int i=0; i<this.declarations.size(); i++) {
	        if(i>0)
	            transpiler.append("else ");
	        if(i<this.declarations.size()-1) {
	            transpiler.append("if(");
	            this.transpileTest(transpiler, common, this.declarations.get(i));
	            transpiler.append(")");
	        }
	        transpiler.indent();
	        this.transpileCall(transpiler, this.declarations.get(i));
	        transpiler.dedent();
	    }
	}

	private void transpileCall(Transpiler transpiler, IMethodDeclaration declaration) {
	    call.transpileSelector(transpiler, declaration);
	    transpiler.append("(");
	    this.arguments.forEach(arg -> {
	        transpiler.append(arg.getName());
	        transpiler.append(", ");
	    });
	    transpiler.trimLast(2);
	    transpiler.append(")");
	}

	private void transpileTest(Transpiler transpiler, Set<IArgument> common, IMethodDeclaration declaration) {
	    for(int i = 0, count = 0;i<this.call.getAssignments().size(); i++) {
	        IArgument incoming = this.call.getAssignments().get(i).getArgument();
	        if(common.contains(incoming))
	            continue;
	        if(count>0)
	            transpiler.append(" && ");
	        count++;
	        if(incoming instanceof UnresolvedArgument)
	            incoming = ((UnresolvedArgument)incoming).getResolved();
	        IArgument outgoing = incoming==null ? declaration.getArguments().get(0) : findCorrespondingArg(transpiler.getContext(), declaration.getArguments(), common, incoming);
	        if(outgoing instanceof UnresolvedArgument)
	        	outgoing = ((UnresolvedArgument)incoming).getResolved();
	        if(incoming==null)
	            incoming = this.declaration.getArguments().get(0);
	        if(incoming instanceof UnresolvedArgument)
	            incoming = ((UnresolvedArgument)incoming).getResolved();
	        if(incoming instanceof CategoryArgument && outgoing instanceof CategoryArgument) {
	            transpiler.append(incoming.getName()).append(".instanceOf(").append(((CategoryArgument)outgoing).getType().getTypeName()).append(")");
	        } else if(incoming instanceof CategoryArgument && outgoing instanceof AttributeArgument) {
	            transpiler.append(incoming.getName()).append(".hasOwnProperty('").append(outgoing.getName()).append("')");
	        } else
	            throw new Error("Unsupported: " + incoming.getClass().getSimpleName() + " and " + outgoing.getClass().getSimpleName());
	    }
	}

	private IArgument findCorrespondingArg(Context context2, ArgumentList arguments, Set<IArgument> common, IArgument incoming) {
	    for(int i=0;i<arguments.size();i++) {
	    	IArgument outgoing = arguments.get(i);
	        if (common.contains(outgoing))
	            continue;
	        if (outgoing.equals(incoming))
	            return outgoing;
	        if (incoming instanceof CategoryArgument && outgoing instanceof CategoryArgument) {
	            if(((CategoryArgument)incoming).getType().isAssignableFrom(context, ((CategoryArgument)outgoing).getType()) || ((CategoryArgument)outgoing).getType().isAssignableFrom(context, ((CategoryArgument)incoming).getType()))
	                return outgoing;
	        }
	    }
	    throw new Error("Could not find matching argument for: " + incoming + " in " + arguments);
	}

	private Set<IArgument> collectCommonArguments() {
		Set<IArgument> common = null;
	    for(int i=0; i<this.declarations.size(); i++) {
	    	IMethodDeclaration declaration = this.declarations.get(i);
	        if(i==0)
	            common = new HashSet<>(declaration.getArguments());
	        else {
	        	Set<IArgument> current = new HashSet<>(declaration.getArguments());
	            common.retainAll(current);
	            if(common.isEmpty())
	                break;
	        }
	    }
	    return common;
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
		throw new UnsupportedOperationException("Should never get there!");
	}

	@Override
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		throw new UnsupportedOperationException("Should never get there!");
	}

	@Override
	public String compileTemplate(Context context, boolean isStart, ClassFile classFile) {
		throw new UnsupportedOperationException("Should never get there!");
	}

	@Override
	public IType check(Context context, boolean isStart) {
		throw new UnsupportedOperationException("Should never get there!");
	}

	@Override
	public void toDialect(CodeWriter writer) {
		throw new UnsupportedOperationException("Should never get there!");
	}


}
