package prompto.declaration;

import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.NativeAttributeBindingListMap;
import prompto.grammar.NativeCategoryBindingList;
import prompto.runtime.Context;
import prompto.runtime.Context.ResourceContext;
import prompto.type.ResourceType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.value.IInstance;
import prompto.value.NativeResource;

public class NativeResourceDeclaration extends NativeCategoryDeclaration {
	
	public NativeResourceDeclaration(Identifier name, IdentifierList attributes, 
			NativeCategoryBindingList categoryMappings, 
			NativeAttributeBindingListMap attributeMappings,
			MethodDeclarationList methods) {
		super(name,attributes, categoryMappings, attributeMappings, methods);
	}
	
	@Override
	public ResourceType getType(Context context) {
		return new ResourceType(getId());
	}
	
	@Override
	public IInstance newInstance(Context context) throws PromptoError {
		return new NativeResource(context, this);
	}
	
	@Override
	public void checkConstructorContext(Context context) {
		if(!(context instanceof ResourceContext))
			throw new SyntaxError("Not a resource context!");
	}
	
	@Override
	protected void categoryTypeToEDialect(CodeWriter writer) {
		writer.append("native resource");
	}
	
	@Override
	protected void categoryTypeToODialect(CodeWriter writer) {
		writer.append("native resource");
	}

	@Override
	protected void categoryTypeToMDialect(CodeWriter writer) {
		writer.append("native resource");
	}
}
