package prompto.declaration;

import java.util.Collection;

import prompto.code.ICodeStore;
import prompto.grammar.Annotation;
import prompto.grammar.INamed;
import prompto.parser.ISection;
import prompto.runtime.Context;
import prompto.statement.CommentStatement;
import prompto.transpiler.ITranspilable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public interface IDeclaration extends ITranspilable, INamed, ISection {
	
	public static enum DeclarationType {
		ATTRIBUTE,
		CATEGORY,
		METHOD,
		ENUMERATED, 
		TEST;
	}
	
	void register(Context context);
	IType check(Context context, boolean isStart);
	void toDialect(CodeWriter writer);
	void setOrigin(ICodeStore origin);
	ICodeStore getOrigin();
	DeclarationType getDeclarationType();
	void setComments(Collection<CommentStatement> comments);
	Collection<CommentStatement> getComments();
	void setAnnotations(Collection<Annotation> annotations);
	Collection<Annotation> getAnnotations();
	default boolean hasAnnotation(Context context, String name) {
		return hasLocalAnnotation(name) || hasInheritedAnnotation(context, name);
	}
	boolean hasLocalAnnotation(String name);
	boolean hasInheritedAnnotation(Context context, String name);
	default void setClosureOf(IMethodDeclaration declaration) { throw new UnsupportedOperationException(); }
	default IMethodDeclaration getClosureOf() { throw new UnsupportedOperationException(); }
	default boolean isStorable() { return false; }
	default String getTranspiledName(Context context) { throw new UnsupportedOperationException("getTranspiledName " + this.getClass().getName()); }
	default void declare(Transpiler transpiler) { throw new UnsupportedOperationException("declare " + this.getClass().getName()); }
	default void declareChild(Transpiler transpiler) { throw new UnsupportedOperationException("declareChild " + this.getClass().getName()); }
	default boolean transpile(Transpiler transpiler) { throw new UnsupportedOperationException("transpile " + this.getClass().getName()); }
}
