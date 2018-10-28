package prompto.grammar;

import java.util.LinkedList;

import prompto.declaration.IMethodDeclaration;

public class MethodDeclarationList extends LinkedList<IMethodDeclaration> {

	private static final long serialVersionUID = 1L;

	public MethodDeclarationList() {
	}

	public MethodDeclarationList(IMethodDeclaration method) {
		this.add(method);
	}

}
