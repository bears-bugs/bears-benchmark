package prompto.declaration;

import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.NativeAttributeBindingListMap;
import prompto.grammar.NativeCategoryBindingList;
import prompto.utils.IdentifierList;

public class AnyNativeCategoryDeclaration extends NativeCategoryDeclaration {
	
	static private AnyNativeCategoryDeclaration instance = new AnyNativeCategoryDeclaration();
	
	static public AnyNativeCategoryDeclaration getInstance() {
		return instance;
	}
	
	private AnyNativeCategoryDeclaration() {
		super(new Identifier("Any"), new IdentifierList(), 
				new NativeCategoryBindingList(), 
				new NativeAttributeBindingListMap(),
				new MethodDeclarationList());
	}

}
