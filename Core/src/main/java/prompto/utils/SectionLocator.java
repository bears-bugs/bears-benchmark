package prompto.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.parser.ISection;
import prompto.runtime.Context.MethodDeclarationMap;

public class SectionLocator {

	public static ISection findSection(Collection<IDeclaration> declarations, ISection section) {
		return getMethods(declarations)
		.filter((s)->s.isOrContains(section))
		.map((d)->{ return findSectionIn(d, section); })
		.findFirst()
		.orElse(null);
	}

	public static ISection findSection(Collection<IDeclaration> declarations, String path, int lineNumber) {
		return getMethods(declarations)
		.filter((d)->path.equals(d.getFilePath()))
		.filter((d)->lineNumber>=d.getStart().getLine())
		.filter((d)->lineNumber<=d.getEnd().getLine())
		.map((d)->{ return findSectionIn(d, lineNumber); })
		.findFirst()
		.orElse(null);
	}
	
	public static ISection findSectionInLists(Collection<List<IDeclaration>> values, ISection section) {
		return values.stream()
			.map(Collection::stream)
			.flatMap((s)->s)
			.filter((d)->d instanceof IMethodDeclaration)
			.map((d)->(IMethodDeclaration)d)
			.filter((s)->s.isOrContains(section))
			.map((d)->{ return findSectionIn(d, section); })
			.findFirst()
			.orElse(null);
	}

	private static ISection findSectionIn(IMethodDeclaration decl, int lineNumber) {
		if(decl instanceof ConcreteMethodDeclaration)
			return findSectionIn((ConcreteMethodDeclaration)decl, lineNumber, lineNumber);
		else
			return decl;
	}
	
	private static ISection findSectionIn(ConcreteMethodDeclaration decl, int startLine, int endLine) {
		return decl.getStatements().stream()
				.filter((s)->startLine>=s.getStart().getLine())
				.filter((s)->endLine<=s.getEnd().getLine())
				.map((s)->(ISection)s)
				.findFirst()
				.orElse(decl);
	}
	
	private static ISection findSectionIn(IMethodDeclaration decl, ISection section) {
		if(decl instanceof ConcreteMethodDeclaration)
			return findSectionIn((ConcreteMethodDeclaration)decl, section.getStart().getLine(), section.getEnd().getLine());
		else
			return decl;
	}
	
	private static Stream<IMethodDeclaration> getMethods(Collection<IDeclaration> declarations) {
		return declarations.stream()
		.filter((d)->d instanceof MethodDeclarationMap)
		.map((d)->(MethodDeclarationMap)d)
		.map((m)->m.values().stream())
		.flatMap((s)->s);
	}


}
