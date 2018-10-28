package prompto.type;

import prompto.declaration.IDeclaration;
import prompto.declaration.IEnumeratedDeclaration;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.value.IValue;


public class EnumeratedCategoryType extends CategoryType {

	public EnumeratedCategoryType(Identifier id) {
		super(Family.ENUMERATED, id); 
	}
	
	@Override
	public IType checkMember(Context context, Identifier id) {
		String name = id.toString();
		if ("symbols".equals(name))
			return new ListType(this);
		else if ("name".equals(name))
			return TextType.instance();
		else
			return super.checkMember(context, id);
	}

	@Override
	public IValue getMemberValue(Context context, Identifier id) {
		String name = id.toString();
		IDeclaration decl = context.getRegisteredDeclaration(IDeclaration.class, typeNameId);
		if(!(decl instanceof IEnumeratedDeclaration))
			throw new SyntaxError(name + " is not an enumerated type!");
		if ("symbols".equals(name))
			return ((IEnumeratedDeclaration<?>)decl).getSymbolsList();
		else
			throw new SyntaxError("No such member:" + name);
	}

}
