package prompto.declaration;

import java.util.function.Predicate;

import prompto.argument.IArgument;
import prompto.compiler.ClassFile;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.grammar.Specificity;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.statement.DeclarationStatement;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.value.IValue;

public interface IMethodDeclaration extends IDeclaration {

	String getProto();
	IType getReturnType();
	ArgumentList getArguments();
	String getSignature(Dialect dialect);
	boolean isAbstract();
	boolean isTemplate();
	boolean isEligibleAsMain();
	default String getNameAsKey() { return getName(); }
	void setMemberOf(CategoryDeclaration declaration);
	CategoryDeclaration getMemberOf();
	default void setDeclarationOf(DeclarationStatement<IMethodDeclaration> statement) { throw new UnsupportedOperationException("setDeclarationStatement " + this.getClass().getName()); }
	default DeclarationStatement<IMethodDeclaration> getDeclarationOf() { throw new UnsupportedOperationException("getDeclarationStatement " + this.getClass().getName()); }
	IValue interpret(Context context) throws PromptoError;
	void check(ConcreteCategoryDeclaration declaration, Context context);
	boolean isAssignableTo(Context context, ArgumentAssignmentList assignments, boolean checkInstance, boolean allowDerived, Predicate<Specificity> filter);
	boolean isAssignableFrom(Context context, ArgumentAssignmentList assignments);
	void registerArguments(Context local);
	Specificity computeSpecificity(Context context, IArgument argument, ArgumentAssignment assignment, boolean useInstance, boolean allowDerived);
	void compile(Context context, boolean isStart, ClassFile classFile);
	void compilePrototype(Context context, boolean isStart, ClassFile classFile);
	String compileTemplate(Context context, boolean isStart, ClassFile classFile);
	void compileAssignments(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments);
	String getTranspiledName(Context context);
	default void fullDeclare(Transpiler transpiler, Identifier methodName) { throw new UnsupportedOperationException("fullDeclare " + this.getClass().getName()); }
	default boolean containerHasAnnotation(Context context, String name) {
		CategoryDeclaration category = getMemberOf();
		return category!=null &&  category.hasAnnotation(context, name);
	};
	default boolean containerHasLocalAnnotation(String name) {
		CategoryDeclaration category = getMemberOf();
		return category!=null &&  category.hasLocalAnnotation(name);
	};

	
}


