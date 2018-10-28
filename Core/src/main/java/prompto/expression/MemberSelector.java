package prompto.expression;

import java.lang.reflect.Type;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.FieldConstant;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.IDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.declaration.NativeGetterMethodDeclaration;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoAny;
import prompto.intrinsic.PromptoDict;
import prompto.intrinsic.PromptoDocument;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.Context.ClosureContext;
import prompto.runtime.Context.InstanceContext;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.EnumeratedCategoryType;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.utils.CodeWriter;
import prompto.value.ConcreteInstance;
import prompto.value.IValue;
import prompto.value.NullValue;

public class MemberSelector extends SelectorExpression {

	Identifier id;
	
	public MemberSelector(Identifier id) {
		this.id = id;
	}
	
	public MemberSelector(IExpression parent, Identifier id) {
		super(parent);
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}
	
	public String getName() {
		return id.toString();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(writer.getDialect()==Dialect.E) {
			IType type = check(writer.getContext());
			if(type instanceof MethodType)
				writer.append("Method: ");
			
		} 
		parentAndMemberToDialect(writer);
	}
	
	protected void parentAndMemberToDialect(CodeWriter writer) {
		try {
			resolveParent(writer.getContext());
		} catch(SyntaxError e) {
			// ignore
		}
		parent.toDialect(writer);
		writer.append(".");
		writer.append(id);
	}

	@Override
	public String toString() {
		return parent.toString() + "." + id;
	}
	
	@Override
	public IType check(Context context) {
		if("this$0".equals(id.toString())) {
			InstanceContext instance = context.getClosestInstanceContext();
			return instance.getInstanceType();
		} else {
			IType parentType = checkParent(context);
			return parentType.checkMember(context, id);
		}
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
        // resolve parent to keep clarity
		IExpression parent = resolveParent(context);
        // special case for singletons 
		IValue value = interpretSingleton(context, parent);
		if(value!=null)
			return value;
		// special case for 'static' type members (like Enum.symbols, Type.name etc...)
		value = interpretTypeMember(context, parent);
		if(value!=null)
			return value;
        // finally resolve instance member
		return interpretInstanceMember(context, parent);
 	}

	private IValue interpretInstanceMember(Context context, IExpression parent) throws PromptoError {
       IValue instance = parent.interpret(context);
        if (instance == null || instance == NullValue.instance())
            throw new NullReferenceError();
        else
        	return instance.getMember(context, id, true);
	}

	private IValue interpretTypeMember(Context context, IExpression parent) throws PromptoError {
       if(parent instanceof TypeExpression)
    	   return ((TypeExpression)parent).getMember(context, id);
       else
    	   return null;
	}

	private IValue interpretSingleton(Context context, IExpression parent) throws PromptoError {
        if(parent instanceof TypeExpression) {
        	IType type = ((TypeExpression)parent).getType();
        	if(type instanceof CategoryType && !(type instanceof EnumeratedCategoryType)) {
        		ConcreteInstance instance = context.loadSingleton(context,  (CategoryType)((TypeExpression)parent).getType());
	        	if(instance!=null)
	        		return instance.getMember(context, id, false); 
        	}
        }
        return null;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
        // resolve parent to keep clarity
		IExpression parent = resolveParent(context);
		// special case for 'static' type members (like Enum.symbols, Type.name etc...)
		ResultInfo	result = compileTypeMember(context, method, flags, parent);
		if(result!=null)
			return result;
		else
			// finally resolve instance member
			return compileInstanceMember(context, method, flags, parent);		
	}

	private ResultInfo compileTypeMember(Context context, MethodInfo method, Flags flags, IExpression parent) {
	       if(parent instanceof TypeExpression) {
	    	   IType type = ((TypeExpression)parent).getType();
	    	   return type.compileGetMember(context, method, flags, parent, id);
	       } else
	    	   return null;
	}


	private ResultInfo compileInstanceMember(Context context, MethodInfo method, Flags flags, IExpression parent) {
		Type resultType = check(context).getJavaType(context);
		ResultInfo info = parent.compile(context, method, flags);
		// special case for char.codePoint() to avoid wrapping char.class for just one member
		if(Character.class==info.getType() && "codePoint".equals(getName()))
			return compileCharacterCodePoint(method, flags);
		// special case for String.length() to avoid wrapping String.class for just one member
		else if(String.class==info.getType() && "count".equals(getName()))
			return compileStringLength(method, flags);
		else if(PromptoAny.class==info.getType()) 
			return compileGetMember(context, method, flags, info, resultType);
		else if(PromptoDocument.class==info.getType())
			return compileGetOrCreate(context, method, flags, info, resultType);		
		// special case for o.text which translates to toString
		else if("text".equals(getName()))
			return compileObjectText(method, flags);
		else {
			String getterName = CompilerUtils.getterName(getName());
			if(isCompilingGetter(context, method, info, getterName))
				compileGetField(context, method, flags, info, resultType);
			else if(context instanceof ClosureContext)
				compileGetField(context, method, flags, info, resultType);
			else if(PromptoDict.Entry.class==info.getType()) // TODO manage all generics
				compileGenericGetter(context, method, flags, getterName, info, resultType);
			else if(info.isNativeCategory())
				compileNativeGetter(context, method, flags, getterName, info, resultType);
			else if(info.isInterface()) 
				compileInterfaceGetter(context, method, flags, getterName, info, resultType);
			else 
				compileBeanGetter(context, method, flags, getterName, info, resultType);
			return new ResultInfo(resultType);
		}
	}
		
	private void compileNativeGetter(Context context, MethodInfo method, Flags flags, String getterName, ResultInfo info, Type resultType) {
		NativeGetterMethodDeclaration getter = getNativeGetter(context);
		if(getter!=null) {
			StackState state = method.captureStackState();
			// can't use 'this' since it could refer to another abject than the native parent
			StackLocal local = method.registerLocal("$this$", VerifierType.ITEM_Object, new ClassConstant(info.getType())); 
			CompilerUtils.compileASTORE(method, local);
			context = context.newInstanceContext(getter.getMemberOf().getType(context), false).newChildContext(); // mimic method call
			getter.compile(context, method, new Flags());
			method.unregisterLocal(local);
			method.restoreStackLocals(state);
			state = method.captureStackState();
			method.placeLabel(state);
			return;
		} else
			compileBeanGetter(context, method, flags, getterName, info, resultType);
	}

	private NativeGetterMethodDeclaration getNativeGetter(Context context) {
		IType type = parent.check(context);
		IDeclaration declaration = type instanceof CategoryType ? ((CategoryType)type).getDeclaration(context) : null;
		if(declaration instanceof NativeCategoryDeclaration)
			return (NativeGetterMethodDeclaration)((NativeCategoryDeclaration)declaration).findGetter(context, id);
		else
			return null;
	}

	private void compileBeanGetter(Context context, MethodInfo method, Flags flags, String getterName, ResultInfo info, Type resultType) {
		IOperand oper = new MethodConstant(info.getType(), getterName, resultType);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
	}

	private void compileInterfaceGetter(Context context, MethodInfo method, Flags flags, String getterName, ResultInfo info, Type resultType) {
		IOperand oper = new InterfaceConstant(info.getType(), getterName, resultType);
		method.addInstruction(Opcode.INVOKEINTERFACE, oper);
	}

	private void compileGenericGetter(Context context, MethodInfo method, Flags flags, String getterName, ResultInfo info, Type resultType) {
		IOperand oper = new MethodConstant(info.getType(), getterName, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.CHECKCAST, new ClassConstant(resultType));
	}

	private ResultInfo compileGetOrCreate(Context context, MethodInfo method, Flags flags, ResultInfo info, Type resultType) {
		IOperand oper = new StringConstant(getName());
		method.addInstruction(Opcode.LDC_W, oper);
		oper = new ClassConstant(PromptoDocument.class);
		method.addInstruction(Opcode.LDC_W, oper);
		oper = new MethodConstant(PromptoDocument.class, "getOrCreate", Object.class, 
				Class.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(PromptoAny.class);
	}

	private ResultInfo compileGetMember(Context context, MethodInfo method, Flags flags, ResultInfo info, Type resultType) {
		IOperand oper = new StringConstant(getName());
		method.addInstruction(Opcode.LDC_W, oper);
		oper = new MethodConstant(PromptoAny.class, "getMember", Object.class, 
				Object.class, Object.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoAny.class);
	}

	private void compileGetField(Context context, MethodInfo method, Flags flags, ResultInfo info, Type resultType) {
		Type classType = CompilerUtils.categoryConcreteTypeFrom(info.getType().getTypeName());
		if("this$0".equals(id.toString()))
			resultType = CompilerUtils.categoryConcreteTypeFrom(resultType.getTypeName());
		FieldConstant f = new FieldConstant(classType, id.toString(), resultType);
		method.addInstruction(Opcode.GETFIELD, f);
	}

	private boolean isCompilingGetter(Context context, MethodInfo method, ResultInfo parent, String getterName) {
		return this.parent instanceof ThisExpression && getterName.equals(method.getName().getValue());
	}

	private ResultInfo compileCharacterCodePoint(MethodInfo method, Flags flags) {
		IOperand oper = new MethodConstant(Character.class, "charValue", char.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		if(flags.toPrimitive())
			return CompilerUtils.intTolong(method);
		else
			return CompilerUtils.intToLong(method);
	}
	
	private ResultInfo compileObjectText(MethodInfo method, Flags flags) {
		IOperand oper = new MethodConstant(Object.class, "toString", String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(String.class);
	}


	private ResultInfo compileStringLength(MethodInfo method, Flags flags) {
		IOperand oper = new MethodConstant(String.class, "length", int.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		if(flags.toPrimitive())
			return CompilerUtils.intTolong(method);
		else
			return CompilerUtils.intToLong(method);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.declareParent(transpiler);
	    IType parentType = this.checkParent(transpiler.getContext());
	    parentType.declareMember(transpiler, this.getName());
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    // resolve parent to keep clarity
	    IExpression parent = this.resolveParent(transpiler.getContext());
	    // special case for singletons
	    if(this.transpileSingleton(transpiler, parent))
	        return false;
	    // special case for 'static' type members (like Enum.symbols, Type.name etc...)
	    if(this.transpileTypeMember(transpiler, parent))
	        return false;
	    // finally resolve instance member
	    this.transpileInstanceMember(transpiler, parent);
	    return false;
	}

	private boolean transpileSingleton(Transpiler transpiler, IExpression parent) {
	   if(parent instanceof TypeExpression) {
		   IType type = ((TypeExpression)parent).getType();
		   if(type instanceof CategoryType && !(type instanceof EnumeratedCategoryType)) {
		 	   type.transpileInstance(transpiler);
		        transpiler.append(".").append(this.getName());
		        return true;
		   }
	    } 
	   return false;
	}

	private boolean transpileTypeMember(Transpiler transpiler, IExpression parent) {
		// TODO Auto-generated method stub
		return false;
	}

	private void transpileInstanceMember(Transpiler transpiler, IExpression parent) {
		parent.transpile(transpiler);
		transpiler.append(".");
		IType type = parent.check(transpiler.getContext());
		type.transpileMember(transpiler, this.getName());
	}

	private void declareParent(Transpiler transpiler) {
 	   // resolve parent to keep clarity
	    IExpression parent = this.resolveParent(transpiler.getContext());
	    parent.declare(transpiler);
	}

}
