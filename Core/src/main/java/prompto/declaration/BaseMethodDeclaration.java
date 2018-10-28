package prompto.declaration;

import java.lang.reflect.Modifier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import prompto.argument.IArgument;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerException;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.grammar.Specificity;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.value.IInstance;
import prompto.value.IValue;

public abstract class BaseMethodDeclaration extends BaseDeclaration implements IMethodDeclaration {

	CategoryDeclaration memberOf;
	IMethodDeclaration closureOf;
	ArgumentList arguments;
	IType returnType;
	
	public BaseMethodDeclaration(Identifier name, ArgumentList arguments, IType returnType) {
		super(name);
		this.arguments = arguments!=null ? arguments : new ArgumentList();
		this.returnType = returnType;
	}

	@Override
	public DeclarationType getDeclarationType() {
		return DeclarationType.METHOD;
	}
	
	@Override
	public void setMemberOf(CategoryDeclaration declaration) {
		this.memberOf = declaration;
	}
	
	@Override
	public CategoryDeclaration getMemberOf() {
		return memberOf;
	}
	
	@Override
	public void setClosureOf(IMethodDeclaration declaration) {
		this.closureOf = declaration;
	}
	
	@Override
	public IMethodDeclaration getClosureOf() {
		return closureOf;
	}
	
	@Override
	public String getSignature(Dialect dialect) {
		StringBuilder sb = new StringBuilder(getId().toString());
		sb.append('(');
		for(IArgument arg : arguments) {
			sb.append(arg.getSignature(dialect));
			sb.append(", ");
		}
		if(arguments.size()>0)
			sb.setLength(sb.length()-2); // strip ", "
		sb.append(')');
		return sb.toString();
	}
	
	@Override
	public String getProto() {
		StringBuilder sb = new StringBuilder();
		for(IArgument arg : arguments) {
			if(sb.length()>0)
				sb.append('/');
			sb.append(arg.getProto());
		}
		return sb.toString();
	}
	
	@Override
	public ArgumentList getArguments() {
		return arguments;
	}
	
	@Override
	public IType getReturnType() {
		return returnType;
	}

	@Override
	public void register(Context context) {
		context.registerDeclaration(this);
	}
	
	@Override
	public void registerArguments (Context context) {
		if(arguments!=null)
			arguments.register(context);
	}
		
	@Override
	public IType getType(Context context) {
		try {
			return check(context, false);
		} catch (SyntaxError e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isAssignableTo(Context context, ArgumentAssignmentList assignments, boolean checkInstance, boolean allowDerived, Predicate<Specificity> filter) {
		try {
			Context local = context.newLocalContext();
			registerArguments(local);
			ArgumentAssignmentList assignmentsList = new ArgumentAssignmentList(assignments);
			for(IArgument argument : arguments) {
				ArgumentAssignment assignment = assignmentsList.find(argument.getId());
				if(assignment==null) {
					IExpression expression = argument.getDefaultExpression();
					if(expression!=null)
						assignment = new ArgumentAssignment(argument, expression);
				}
				if(assignment==null) // missing argument
					return false;
				if(!isAssignableTo(local, argument, assignment, checkInstance, allowDerived, filter))
					return false;
				assignmentsList.remove(assignment);
			}
			return assignmentsList.isEmpty();
		} catch (SyntaxError e) {
			return false;
		}
	}
	
	@Override
	public boolean isAssignableFrom(Context context, ArgumentAssignmentList assignments) {
		try {
			Context local = context.newLocalContext();
			registerArguments(local);
			ArgumentAssignmentList assignmentsList = new ArgumentAssignmentList(assignments);
			for(IArgument argument : arguments) {
				ArgumentAssignment assignment = assignmentsList.find(argument.getId());
				if(assignment==null) {
					IExpression expression = argument.getDefaultExpression();
					if(expression!=null)
						assignment = new ArgumentAssignment(argument, expression);
				}
				if(assignment==null) // missing argument
					return false;
				if(!isAssignableFrom(local, argument, assignment))
					return false;
				assignmentsList.remove(assignment);
			}
			return assignmentsList.isEmpty();
		} catch (SyntaxError e) {
			return false;
		}
	}
	
	boolean isAssignableTo(Context context, IArgument argument, ArgumentAssignment assignment, boolean useInstance, boolean allowDerived, Predicate<Specificity> filter) {
		Specificity spec = computeSpecificity(context, argument, assignment, useInstance, allowDerived);
		return filter.test(spec);
	}
	
	boolean isAssignableFrom(Context context, IArgument argument, ArgumentAssignment assignment) {
		try {
			IType required = argument.getType(context);
			IType actual = assignment.getExpression().check(context);
			if(actual.equals(required)
					|| actual.isAssignableFrom(context, required)
					|| required.isAssignableFrom(context, actual))
				return true;
			actual = assignment.resolve(context, this, false, false).check(context);
			return actual.equals(required)
					|| actual.isAssignableFrom(context, required)
					|| required.isAssignableFrom(context, actual);
		} catch(PromptoError error) {
			return false;
		}
	}

	@Override
	public Specificity computeSpecificity(Context context, IArgument argument, ArgumentAssignment assignment, boolean useInstance, boolean allowDerived) {
		try {
			IType required = argument.getType(context);
			IType actual = assignment.getExpression().check(context);
			// retrieve actual runtime type
			if(useInstance && actual instanceof CategoryType) {
				// TODO: potential side effects here with function called multiple times
				IValue value = assignment.getExpression().interpret(context.getCallingContext());
				if(value instanceof IInstance)
					actual = ((IInstance)value).getType();
			}
			if(actual.equals(required))
				return Specificity.EXACT;
			else if(required.isAssignableFrom(context, actual)) 
				return Specificity.INHERITED;
			else if(allowDerived && actual.isAssignableFrom(context, required)) 
				return Specificity.DERIVED;
			actual = assignment.resolve(context, this, useInstance, false).check(context);
			if(required.isAssignableFrom(context, actual))
				return Specificity.IMPLICIT;
			else if(allowDerived && actual.isAssignableFrom(context, required))
				return Specificity.IMPLICIT;
		} catch(PromptoError error) {
		}
		return Specificity.INCOMPATIBLE; 
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		throw new InternalError("Should never get there!");
	}
	
	@Override
	public boolean isEligibleAsMain() {
		return false;
	}
	
	@Override
	public void compilePrototype(Context context, boolean isStart, ClassFile classFile) {
		try {
			context = prepareContext(context, isStart);
			IType returnType = check(context, false);
			MethodInfo method = createMethodInfo(context, classFile, returnType, getName());
			method.addModifier(Modifier.ABSTRACT);
		} catch (PromptoError e) {
			throw new CompilerException(e);
		}
	}

	protected Context prepareContext(Context context, boolean isStart) {
		if(isStart) {
			// coming from nowhere, so need a clean context in which to register arguments
			context = context.newLocalContext();
			registerArguments(context);
		}
		return context;
	}
	
	protected MethodInfo createMethodInfo(Context context, ClassFile classFile, IType returnType, String methodName) {
		Descriptor.Method proto = CompilerUtils.createMethodDescriptor(context, arguments, returnType);
		MethodInfo method = classFile.newMethod(methodName, proto); 
		return method;
	}

	@Override
	public void compileAssignments(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments) {
		boolean isFirst = true;
		for(IArgument arg : arguments.stripOutTemplateArguments()) {
			arg.compileAssignment(context, method, flags, assignments, isFirst);
			isFirst = false;
		}
	}
	
	public void declareArguments(Transpiler transpiler) {
		this.arguments.declare(transpiler);
	}

	public void transpileProlog(Transpiler transpiler) {
	    if (this.memberOf!=null) {
	        transpiler.append(this.memberOf.getName());
	        if(this.hasAnnotation(transpiler.getContext(), "@Static"))
	           	transpiler.append(".");
	        else
	        	transpiler.append(".prototype.");
	        transpiler.append(this.getTranspiledName(transpiler.getContext())).append(" = function (");
	    } else
	        transpiler.append("function ").append(this.getTranspiledName(transpiler.getContext())).append(" (");
	    this.arguments.transpile(transpiler);
	    transpiler.append(") {").indent();
	}


	public void transpileEpilog(Transpiler transpiler) {
	    transpiler.dedent().append("}");
	    if(this.memberOf!=null)
	        transpiler.append(";");
	    transpiler.newLine();
	}
	
	@Override
	public String getTranspiledName(Context context) {
	    // if this is a template instance, name is already transpiled
	    if(this.getName().indexOf("$")>0)
	    	return this.getName();
	    else if(this.hasLocalAnnotation("@Callback") || this.hasInheritedAnnotation(context, "@Callback"))
	    	return this.getName();
	    else {
	    	Stream<String> name = Stream.of(this.getName());
	    	Stream<String> args = this.arguments.stream().map(arg->arg.getTranspiledName(context));
	    	return Stream.concat(name, args).collect(Collectors.joining("$"));
	    }
	};
	
	
	@Override
	public boolean hasInheritedAnnotation(Context context, String name) {
		if(memberOf==null)
			return false;
		else
			return getOverriddenMethods(context).anyMatch(m->m.hasLocalAnnotation(name));
	}
	
	protected Stream<IMethodDeclaration> getOverriddenMethods(Context context) {
		Stream<CategoryDeclaration> categories = Stream.of(memberOf);
		if(memberOf.getDerivedFrom()!=null)
			categories = Stream.concat(categories, memberOf.getDerivedFrom().stream().map(id->context.getRegisteredDeclaration(CategoryDeclaration.class, id)));
		return categories
				.map(cat->cat.getLocalMethods().stream())
				.flatMap(Function.identity())
				.filter(m->this.getName().equals(m.getName()))
				.filter(m->this.getProto().equals(m.getProto()));
	}
	


}


