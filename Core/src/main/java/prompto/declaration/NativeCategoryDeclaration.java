package prompto.declaration;

import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.NativeAttributeBindingListMap;
import prompto.grammar.NativeCategoryBinding;
import prompto.grammar.NativeCategoryBindingList;
import prompto.java.JavaNativeCategoryBinding;
import prompto.javascript.JavaScriptNativeCategoryBinding;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.value.IInstance;
import prompto.value.NativeInstance;

public class NativeCategoryDeclaration extends ConcreteCategoryDeclaration {
	
	NativeCategoryBindingList categoryBindings;
	NativeAttributeBindingListMap attributeMappings;
	Class<?> boundClass = null;
	
	public NativeCategoryDeclaration(Identifier id, IdentifierList attributes, 
			NativeCategoryBindingList categoryBindings, 
			NativeAttributeBindingListMap attributeBindings,
			MethodDeclarationList methods) {
		super(id, attributes, null, methods);
		this.categoryBindings = categoryBindings;
		this.attributeMappings = attributeBindings;
	}

	@Override
	public void register(Context context) {
		super.register(context);
		Class<?> klass = getBoundClass(false);
		if(klass!=null)
			context.registerNativeBinding(klass, this);
	}
	
	@Override
	protected void setDbId(Context context, IInstance instance, Object dbId) {
		((NativeInstance)instance).setDbId(dbId);
	}
	
	@Override
	protected void toEDialect(CodeWriter writer) {
		protoToEDialect(writer, false, true);
		bindingsToEDialect(writer);
		methodsToEDialect(writer);
	}

	private void methodsToEDialect(CodeWriter writer) {
		if(methods!=null && methods.size()>0) {
			writer.append("and methods:");
			writer.newLine();
			methodsToEDialect(writer, methods);
		}
	}

	protected void categoryTypeToEDialect(CodeWriter writer) {
		writer.append("native category");
	}
	
	protected void bindingsToEDialect(CodeWriter writer) {
		writer.indent();
		categoryBindings.toDialect(writer);
		writer.dedent();
		writer.newLine();
	}

	@Override
	protected void toODialect(CodeWriter writer) {
		boolean hasBody = true; // always one
		toODialect(writer, hasBody); 
	}
	
	@Override
	protected void categoryTypeToODialect(CodeWriter writer) {
		writer.append("native category");
	}
	
	@Override
	protected void bodyToODialect(CodeWriter writer) {
		categoryBindings.toDialect(writer);
		if(methods!=null && methods.size()>0) {
			writer.newLine();
			writer.newLine();
			methodsToODialect(writer, methods);
		}
	}

	@Override
	protected void toMDialect(CodeWriter writer) {
		protoToMDialect(writer, null);
		writer.indent();
		writer.newLine();
		categoryBindings.toDialect(writer);
		if(methods!=null && methods.size()>0) {
			for(IDeclaration decl : methods) {
				CodeWriter w = writer.newMemberWriter();
				decl.toDialect(w);
				writer.newLine();
			}
		}
		writer.dedent();
		writer.newLine();
	}
	
	@Override
	protected void categoryTypeToMDialect(CodeWriter writer) {
		writer.append("native category");
	}

	@Override
	public IInstance newInstance(Context context) throws PromptoError {
		return new NativeInstance(context, this);
	}

	public String getBoundClassName() {
		JavaNativeCategoryBinding binding = getBinding(false);
		if(binding==null)
			return null;
		else
			return binding.getExpression().toString();
	}
	
	public Class<?> getBoundClass(boolean fail) {
		if(boundClass==null) {
			JavaNativeCategoryBinding mapping = getBinding(fail);
			if(mapping!=null) {
				boundClass = mapping.getExpression().interpret_class();
				if(boundClass==null && fail)
					throw new SyntaxError("No Java class:" + mapping.getExpression().toString());
			}
		}
		return boundClass;
	}

	private JavaNativeCategoryBinding getBinding(boolean fail) {
		for(NativeCategoryBinding mapping : categoryBindings) {
			if(mapping instanceof JavaNativeCategoryBinding)
				return (JavaNativeCategoryBinding)mapping;
		}
		if(fail)
			throw new SyntaxError("Missing JAVA mapping !");
		else
			return null;
	}
	
	protected JavaScriptNativeCategoryBinding getJavaScriptBinding() {
		for(NativeCategoryBinding mapping : categoryBindings) {
			if(mapping instanceof JavaScriptNativeCategoryBinding)
				return (JavaScriptNativeCategoryBinding)mapping;
		}
		throw new SyntaxError("Missing JAVASCRIPT mapping !");
	}

	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.declare(this);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		JavaScriptNativeCategoryBinding binding = this.getJavaScriptBinding();
	    binding.transpile(transpiler);
	    String name = binding.getBoundName();
	    transpiler.append("function ").append("new_").append(this.getName()).append("(values) {").indent();
	    transpiler.append("values = values || {};").newLine();
	    transpiler.append("var value = new ").append(name).append("();").newLine();
	    if(this.attributes!=null) {
	        this.attributes.forEach(attr -> transpiler.append("value.").append(attr.toString()).append(" = values.hasOwnProperty('").append(attr.toString()).append("') ? values.").append(attr.toString()).append(" : null;").newLine());
	    }
	    transpiler.append("return value;").newLine();
	    transpiler.dedent().append("}").newLine();
	    Transpiler instance = transpiler.newInstanceTranspiler(new CategoryType(this.getId()));
	    this.transpileMethods(instance);
	    this.transpileGetterSetters(instance);
	    instance.flush();
	    return true;
	}

	public String getTranspiledBoundClass() {
		JavaScriptNativeCategoryBinding binding = this.getJavaScriptBinding();
	    return binding.getBoundName();
	}

	
}
