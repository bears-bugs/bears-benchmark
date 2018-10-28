package prompto.type;

import java.lang.reflect.Type;

import prompto.compiler.CompilerUtils;
import prompto.compiler.PromptoType;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.SyntaxError;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Context.ClosureContext;
import prompto.store.Family;
import prompto.transpiler.Transpiler;

public class MethodType extends BaseType {

	IMethodDeclaration method;
	
	public MethodType(IMethodDeclaration method) {
		super(Family.METHOD);
		this.method = method;
	}
	
	public IMethodDeclaration getMethod() {
		return method;
	}
	
	@Override
	public String getTypeName() {
		return method.getName();
	}
	
	@Override
	public Identifier getTypeNameId() {
		return method.getId();
	}
	
	
	@Override
	public Type getJavaType(Context context) {
		if(method.getClosureOf()!=null && method.getMemberOf()!=null)
			return getMemberClosureJavaType(context);
		else if(method.getMemberOf()!=null)
			return getMemberJavaType(context);
		else if(method.getClosureOf()!=null)
			return getClosureJavaType(context);
		else
			return CompilerUtils.getGlobalMethodType(method.getId());
	}
	
	private Type getMemberClosureJavaType(Context context) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	private Type getMemberJavaType(Context context) {
		Type outer = CompilerUtils.getCategoryConcreteType(method.getMemberOf().getId()); 
		return new PromptoType(outer.getTypeName() + '$' + method.getName());
	}

	private Type getClosureJavaType(Context context) {
		IMethodDeclaration embedding = method.getClosureOf();
		if(embedding.getMemberOf()==null) {
			Type outer = CompilerUtils.getGlobalMethodType(embedding.getId()); 
			return new PromptoType(outer.getTypeName() + '$' + method.getName());
		} else {
			Type outer = CompilerUtils.getCategoryConcreteType(embedding.getMemberOf().getId()); 
			return new PromptoType(outer.getTypeName() + '$' + embedding.getName() + '$' + method.getName());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		else  try {
			return (obj instanceof MethodType) && 
					this.method.getProto().equals(((MethodType)obj).method.getProto()); // TODO: refine
		} catch (SyntaxError e) {
			return false;
		}
	}
	
	@Override
	public void checkUnique(Context context) {
		IDeclaration actual = context.getRegisteredDeclaration(IDeclaration.class, method.getId());
		if(actual!=null)
			throw new SyntaxError("Duplicate name: \"" + method.getId() + "\"");
	}
	
	@Override
	public void checkExists(Context context) {
		// nothing to do
	}
	
	@Override
	public boolean isMoreSpecificThan(Context context, IType other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public IType checkMember(Context context, Identifier name) {
		if(context instanceof ClosureContext) {
			INamed named = context.getRegistered(name);
			if(named!=null)
				return named.getType(context);
		}
		return super.checkMember(context, name);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}


}
