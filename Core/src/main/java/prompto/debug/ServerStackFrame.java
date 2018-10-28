package prompto.debug;

import java.util.Collection;
import java.util.stream.Collectors;

import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.NativeMethodDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.statement.IStatement;

public class ServerStackFrame extends LeanStackFrame {
	
	Context context;
	
	public ServerStackFrame(Context context, String methodName, int index, IDeclaration method) {
		this(context, methodName, index, (ISection)method);
		if(method instanceof ConcreteMethodDeclaration) {
			IStatement stmt = ((ConcreteMethodDeclaration)method).getStatements().getFirst();
			this.endCharIndex = stmt.getStart().getIndex() - 1;
		} else if(method instanceof NativeMethodDeclaration) {
			IStatement stmt = ((NativeMethodDeclaration)method).getStatements().getFirst();
			this.endCharIndex = stmt.getStart().getIndex() - 1;
		} else if(method instanceof TestMethodDeclaration) {
			IStatement stmt = ((TestMethodDeclaration)method).getStatements().getFirst();
			this.endCharIndex = stmt.getStart().getIndex() - 1;
		} else
			this.endCharIndex = this.startCharIndex + 1;
	}

	public ServerStackFrame(Context context, String methodName, int index, ISection section) {
		this.context = context;
		this.methodName = methodName;
		this.filePath = section.getFilePath();
		this.index = index;
		this.line = section.getStart().getLine();
		this.startCharIndex = section.getStart().getIndex();
		this.endCharIndex = section.getEnd().getIndex();
	}
	
	@Override
	public boolean hasVariables() {
		return !context.getInstances().isEmpty();
	}

	@Override
	public Collection<ServerVariable> getVariables() {
		return context.getInstances().stream()
				.map((n)->new ServerVariable(context, n))
				.collect(Collectors.toList());
	}

}
