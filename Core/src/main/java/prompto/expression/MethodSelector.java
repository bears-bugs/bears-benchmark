package prompto.expression;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import prompto.compiler.BootstrapMethod;
import prompto.compiler.CallSiteConstant;
import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.FieldConstant;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.InterfaceType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodHandleConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.NameAndTypeConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.declaration.BuiltInMethodDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.ConcreteCategoryDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeMethodDeclaration;
import prompto.declaration.SingletonCategoryDeclaration;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Context.InstanceContext;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.type.NativeType;
import prompto.utils.CodeWriter;
import prompto.value.IInstance;
import prompto.value.IValue;
import prompto.value.NullValue;
import prompto.value.TypeValue;


public class MethodSelector extends MemberSelector implements IMethodSelector {

	public MethodSelector(Identifier name) {
		super(name);
	}

	public MethodSelector(IExpression parent, Identifier id) {
		super(parent, id);
	}
	
	@Override
	public String toString() {
		return parent==null ? id.toString() : super.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(parent==null)
			writer.append(id);
		else
			super.parentAndMemberToDialect(writer);
	}
	
	public Set<IMethodDeclaration> getCandidates(Context context, boolean checkInstance) {
		INamed named = context.getRegistered(id);
		if(named instanceof Variable && named.getType(context) instanceof prompto.type.MethodType)
			return Collections.singleton(((prompto.type.MethodType)named.getType(context)).getMethod());
		else if(parent==null)
			return getGlobalCandidates(context);
		else
			return getMemberCandidates(context, checkInstance);
	}
	
	private Set<IMethodDeclaration> getGlobalCandidates(Context context) {
		Set<IMethodDeclaration> methods = new HashSet<>();
		// if called from a member method, could be a member method called without this/self
		InstanceContext instance = context.getClosestInstanceContext();
		if(instance!=null) {
			IType type = instance.getInstanceType();
			ConcreteCategoryDeclaration cd = context.getRegisteredDeclaration(ConcreteCategoryDeclaration.class, type.getTypeNameId());
			if(cd!=null) {
				MethodDeclarationMap members = cd.getMemberMethods(context, id);
				if(members!=null)
					methods.addAll(members.values());
			}
		}
		MethodDeclarationMap globals = context.getRegisteredDeclaration(MethodDeclarationMap.class, id);
		if(globals!=null)
			methods.addAll(globals.values());
		return methods;
	}
	
	private Set<IMethodDeclaration> getMemberCandidates(Context context, boolean checkInstance) {
		IType parentType = checkParentType(context, checkInstance);
		return parentType.getMemberMethods(context, id);
	}
	
	private IType checkParentType(Context context, boolean checkInstance) {
		if(checkInstance)
			return checkParentInstance(context);
		else 
			return checkParent(context);
	}

	private IType checkParentInstance(Context context) {
		Identifier id = null;
		if(parent instanceof InstanceExpression)
			id = ((InstanceExpression)parent).getId();
		else if(parent instanceof UnresolvedIdentifier)
			id = ((UnresolvedIdentifier)parent).getId();
		if(id!=null) {
			// don't get Singleton values
			if(Character.isLowerCase(id.toString().charAt(0))) {
				IValue value = context.getValue(id);
				if(value!=null && value!=NullValue.instance())
					return value.getType();
			}
		}
		// TODO check result instance
		return checkParent(context);
	}

	public ResultInfo compileExact(Context context, MethodInfo method, Flags flags, 
				IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		if(parent!=null)
			return compileExactExplicitMember(context, method, flags, declaration, assignments);
		else if(declaration.getMemberOf()!=null) 
			return compileExactImplicitMember(context, method, flags, declaration, assignments);
		else if(declaration.isAbstract())
			return compileExactAbstractInstance(context, method, flags, declaration, assignments);
		else if(!id.toString().equals(declaration.getName())) 
			return compileExactMethodInstance(context, method, flags, declaration, assignments);
		else 
			return compileExactStaticMethod(context, method, flags, declaration, assignments);
	}
	
	public ResultInfo compileTemplate(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments, String methodName) {
		if(parent!=null)
			return compileTemplateExplicitMember(context, method, flags, declaration, assignments, methodName);
		else if(declaration.getMemberOf()!=null) 
			return compileTemplateImplicitMember(context, method, flags, declaration, assignments, methodName);
		else if(declaration.isAbstract())
			return compileTemplateAbstractMethod(context, method, flags, declaration, assignments, methodName);
		else
			return compileTemplateStaticMethod(context, method, flags, declaration, assignments, methodName);
	}

	private ResultInfo compileTemplateStaticMethod(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration, ArgumentAssignmentList assignments, String methodName) {
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call global method in current class
		Type classType = method.getClassFile().getThisClass().getType();
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		MethodConstant constant = new MethodConstant(classType, methodName, descriptor);
		method.addInstruction(Opcode.INVOKESTATIC, constant);
		return new ResultInfo(returnType.getJavaType(context));
	}

	private ResultInfo compileTemplateAbstractMethod(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration, ArgumentAssignmentList assignments, String methodName) {
		throw new UnsupportedOperationException();
	}

	private ResultInfo compileTemplateImplicitMember(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration, ArgumentAssignmentList assignments, String methodName) {
		throw new UnsupportedOperationException();
	}

	private ResultInfo compileTemplateExplicitMember(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration, ArgumentAssignmentList assignments, String methodName) {
		throw new UnsupportedOperationException();
	}

	public ResultInfo compileDynamic(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
	if(parent!=null)
		return compileDynamicExplicitMember(context, method, flags, declaration, assignments);
	else if(declaration.getMemberOf()!=null) 
		return compileDynamicImplicitMember(context, method, flags, declaration, assignments);
	else 
		return compileDynamicGlobalMethod(context, method, flags, declaration, assignments);
}

	private ResultInfo compileDynamicGlobalMethod(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call global method bootstrap method in its own class
		Type classType = CompilerUtils.getGlobalMethodType(declaration.getName());
		String methodName = declaration.getName();
		MethodConstant mc = new MethodConstant(classType, "bootstrap", 
				Lookup.class, String.class, MethodType.class, CallSite.class);
		MethodHandleConstant mhc = new MethodHandleConstant(mc);
		BootstrapMethod bsm = new BootstrapMethod(mhc);
		method.getClassFile().addBootstrapMethod(bsm);
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		NameAndTypeConstant nameAndType = new NameAndTypeConstant(methodName, descriptor);
		CallSiteConstant constant = new CallSiteConstant(bsm, nameAndType);
		method.addInstruction(Opcode.INVOKEDYNAMIC, constant);
		return new ResultInfo(returnType.getJavaType(context));
	}

	private ResultInfo compileDynamicImplicitMember(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		throw new UnsupportedOperationException();
	}

	private ResultInfo compileDynamicExplicitMember(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		throw new UnsupportedOperationException();
	}

	private ResultInfo compileExactMethodInstance(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// load method instance
		compileLoadMethodInstance(context, method, flags, declaration);
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call global method in its own class
		Type classType = CompilerUtils.getGlobalMethodType(declaration.getName());
		String methodName = declaration.getName();
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		InterfaceConstant constant = new InterfaceConstant(classType, methodName, descriptor);
		method.addInstruction(Opcode.INVOKEINTERFACE, constant);
		return new ResultInfo(returnType.getJavaType(context));
	}

	
	private ResultInfo compileExactAbstractInstance(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// load method instance
		compileLoadMethodInstance(context, method, flags, declaration);
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call global method through FunctionalInterface
		IType returnIType = declaration.check(context, false);
		InterfaceType intf = new InterfaceType(declaration.getArguments(), returnIType);
		Type classType = intf.getInterfaceType();
		String methodName = intf.getInterfaceMethodName();
		List<Type> argTypes = IntStream.range(0, declaration.getArguments().size()).mapToObj(i->Object.class).collect(Collectors.toList());
		Descriptor.Method descriptor = new Descriptor.Method(argTypes.toArray(new Type[argTypes.size()]), intf.isVoid() ? void.class : Object.class);
		InterfaceConstant constant = new InterfaceConstant(classType, methodName, descriptor);
		method.addInstruction(Opcode.INVOKEINTERFACE, constant);
		// cast result
		Type returnType = returnIType.getJavaType(context);
		if(!intf.isVoid())
			method.addInstruction(Opcode.CHECKCAST, new ClassConstant(returnType));
		// done
		return new ResultInfo(returnIType.getJavaType(context));
	}


	
	private Type compileLoadMethodInstance(Context context, MethodInfo method, Flags flags, IMethodDeclaration declaration) {
		StackLocal local = method.getRegisteredLocal(getName());
		if(local!=null) {
			CompilerUtils.compileALOAD(method, local);
			return ((StackLocal.ObjectLocal)local).getClassName().getType();
		}
		// if in a closure, could be a field
		FieldInfo fieldInfo = method.getClassFile().getFieldInfo(getName());
		if(fieldInfo!=null) {
			ClassConstant thisClass = method.getClassFile().getThisClass();
			method.addInstruction(Opcode.ALOAD_0, thisClass);
			FieldConstant field = new FieldConstant(method.getClassFile().getThisClass(), id.toString(), fieldInfo.getType());
			method.addInstruction(Opcode.GETFIELD, field);
			return field.getType();
		}
		// not sure how we got here...
		throw new UnsupportedOperationException("Could not find abstract method instance " + getName())	;
	}

	private ResultInfo compileExactStaticMethod(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call global method in its own class
		Type classType = CompilerUtils.getGlobalMethodType(declaration.getName());
		String methodName = declaration.getName();
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		MethodConstant constant = new MethodConstant(classType, methodName, descriptor);
		method.addInstruction(Opcode.INVOKESTATIC, constant);
		return new ResultInfo(returnType.getJavaType(context));
	}

	private ResultInfo compileExactImplicitMember(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// calling method with implicit this
		StackLocal local = method.getRegisteredLocal("this");
		ClassConstant klass = ((StackLocal.ObjectLocal)local).getClassName();
		method.addInstruction(Opcode.ALOAD_0, klass); // 'this' is always at index 0
		return compileExactInstanceMember(context, method, flags, declaration, assignments, klass);
	}

	private ResultInfo compileExactInstanceMember(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments, 
			ClassConstant parentClass) {
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call virtual method
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		if(parentClass.isInterface()) {
			InterfaceConstant constant = new InterfaceConstant(parentClass, declaration.getName(), descriptor);
			method.addInstruction(Opcode.INVOKEINTERFACE, constant);
		} else {
			MethodConstant constant = new MethodConstant(parentClass, declaration.getName(), descriptor);
			method.addInstruction(Opcode.INVOKEVIRTUAL, constant);
		}
		return new ResultInfo(returnType.getJavaType(context));
	}
	
	private ResultInfo compileExactStaticMember(Context context, MethodInfo method, Flags flags, 
			IExpression parent, IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// find class
		Type type = getSingletonType(context, parent);
		ClassConstant parentClass = new ClassConstant(type);
		// push arguments on the stack
		declaration.compileAssignments(context, method, flags, assignments);
		// call static method
		IType returnType = declaration.check(context, false);
		Descriptor.Method descriptor = CompilerUtils.createMethodDescriptor(context, declaration.getArguments(), returnType);
		MethodConstant constant = new MethodConstant(parentClass, declaration.getName(), descriptor);
		method.addInstruction(Opcode.INVOKESTATIC, constant);
		return new ResultInfo(returnType.getJavaType(context));
	}

	private Type getSingletonType(Context context, IExpression parent) {
		if(!(((TypeExpression)parent).getType() instanceof CategoryType))
			throw new SyntaxError("Expecting a category type!");
		CategoryType type = (CategoryType)((TypeExpression)parent).getType();
		CategoryDeclaration decl = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(decl instanceof SingletonCategoryDeclaration)
			return CompilerUtils.getCategorySingletonType(type.getTypeNameId());
		else
			throw new SyntaxError("Expecting a singleton type!");
	}

	public ResultInfo compileExactExplicitMember(Context context, MethodInfo method, Flags flags, 
			IMethodDeclaration declaration, ArgumentAssignmentList assignments) {
		// calling an explicit instance or singleton member method
		IExpression parent = resolveParent(context.getCallingContext());
		if(parent instanceof TypeExpression)
			return compileExactStaticMember(context, method, flags, parent, declaration, assignments);
		else {
			// push instance if any
			ResultInfo info = parent.compile(context.getCallingContext(), method, flags); 
			ClassConstant c = new ClassConstant(info.getType());
			if(declaration instanceof BuiltInMethodDeclaration) {
				BuiltInMethodDeclaration builtin = (BuiltInMethodDeclaration)declaration;
				if(builtin.hasCompileExactInstanceMember())
					return builtin.compileExactInstanceMember(context, method, flags, assignments);
			} else if(declaration instanceof NativeMethodDeclaration)
				return compileExactNativeMember (context, method, flags, (NativeMethodDeclaration)declaration, assignments, c);	
			return compileExactInstanceMember(context, method, flags, declaration, assignments, c);
		}
	}
	

	public ResultInfo compileExactNativeMember(Context context, MethodInfo method, Flags flags, 
			NativeMethodDeclaration declaration, ArgumentAssignmentList assignments, ClassConstant parentClass) {
		StackState state = method.captureStackState();
		// can't use 'this' since it could refer to another abject than the native parent
		StackLocal local = method.registerLocal("$this$", VerifierType.ITEM_Object, parentClass); 
		CompilerUtils.compileASTORE(method, local);
		context = context.newInstanceContext(declaration.getMemberOf().getType(context), false).newChildContext(); // mimic method call
		ResultInfo info = declaration.compileMember(context, method, new Flags(), assignments);
		method.unregisterLocal(local);
		method.restoreStackLocals(state);
		state = method.captureStackState();
		method.placeLabel(state);
		return info;
	}
	
	public Context newLocalContext(Context context, IMethodDeclaration declaration) throws PromptoError {
		if(parent!=null)
			return newInstanceContext(context);
		else if(declaration.getMemberOf()!=null)
			return newLocalInstanceContext(context);
		else
			return context.newLocalContext();
	}

	public Context newLocalCheckContext(Context context, IMethodDeclaration declaration) {
		if(parent!=null)
			return newInstanceCheckContext(context);
		else if(declaration.getMemberOf()!=null)
			return newLocalInstanceContext(context);
		else
			return context.newLocalContext();
	}

	private Context newInstanceCheckContext(Context context) {
		IType type = parent.check(context);
		if(type instanceof CategoryType) {
			CategoryDeclaration decl = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
			context = context.newInstanceContext((CategoryType)type, decl instanceof SingletonCategoryDeclaration);
			return context.newChildContext();
		} else if(type instanceof NativeType) {
			context = context.newBuiltInContext((NativeType)type);
			return context.newChildContext();
		} else {
			return context.newChildContext();
		}
	}

	private Context newInstanceContext(Context context) throws PromptoError {
		IValue value = parent.interpret(context);
		if(value==null || value==NullValue.instance())
			throw new NullReferenceError();
		if(value instanceof TypeValue && ((TypeValue)value).getValue() instanceof CategoryType)
			value = context.loadSingleton(context, (CategoryType)((TypeValue)value).getValue());
		if(value instanceof IInstance) {
			context = context.newInstanceContext((IInstance)value, false);
			return context.newChildContext();
		} else {
			context = context.newBuiltInContext(value);
			return context.newChildContext();
		}
	}

	private Context newLocalInstanceContext(Context context) {
		Context instance = context.getClosestInstanceContext();
		if(instance==null)
			throw new SyntaxError("Not in instance context !");
		context = context.newLocalContext();
		context.setParentContext(instance); // make local context child of the existing instance
		return context;
	}

	public IExpression toInstanceExpression() {
		if(parent==null)
			return new UnresolvedIdentifier(id);
		else
			return new MemberSelector(parent, id);
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
	    if(this.parent!=null)
	        return super.transpile(transpiler);
	    else {
	        transpiler.append(this.getName());
	        return false;
	    }
	}

	public MethodSelector newFullSelector(long counter) {
	    String name = this.id.toString() + "$" + counter;
	    return new MethodSelector(this.parent, new Identifier(name));
	}
	
	

}
