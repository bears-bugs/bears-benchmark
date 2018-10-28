package prompto.declaration;

import prompto.grammar.Identifier;
import prompto.statement.StatementList;

public class NativeSetterMethodDeclaration extends SetterMethodDeclaration {

	public NativeSetterMethodDeclaration(Identifier id, StatementList statements) {
		super(id, statements);
	}

}
