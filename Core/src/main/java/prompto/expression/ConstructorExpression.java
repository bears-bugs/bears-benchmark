package prompto.expression;

import java.lang.reflect.Type;
import java.util.Set;

import prompto.argument.AttributeArgument;
import prompto.compiler.CompilerUtils;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.ConcreteWidgetDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.declaration.NativeWidgetDeclaration;
import prompto.error.NotMutableError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.intrinsic.IMutable;
import prompto.intrinsic.PromptoDocument;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.DocumentType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Document;
import prompto.value.IInstance;
import prompto.value.IValue;
import prompto.value.NullValue;

public class ConstructorExpression implements IExpression {
	
	CategoryType type;
	boolean checked; // if coming from UnresolvedCall, need to check homonyms
	IExpression copyFrom = null;
	ArgumentAssignmentList assignments;
	
	public ConstructorExpression(CategoryType type, IExpression copyFrom, ArgumentAssignmentList assignments, boolean checked) {
		this.type = type;
		this.copyFrom = copyFrom;
		this.assignments = assignments;
		this.checked = checked;
	}
	
	public CategoryType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		this.toDialect(writer);
		return writer.toString();
	}
	
	public ArgumentAssignmentList getAssignments() {
		return assignments;
	}
	
	public void setCopyFrom(IExpression copyFrom) {
		this.copyFrom = copyFrom;
	}

	public IExpression getCopyFrom() {
		return copyFrom;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		Context context = writer.getContext();
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd==null)
			throw new SyntaxError("Unknown category " + type.getTypeName());
		checkFirstHomonym(context, cd);
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	private void toMDialect(CodeWriter writer) {
		toODialect(writer);
	}

	private void toODialect(CodeWriter writer) {
		type.toDialect(writer);
		ArgumentAssignmentList assignments = new ArgumentAssignmentList();
		if (copyFrom != null)
			assignments.add(new ArgumentAssignment(new AttributeArgument(new Identifier("from")), copyFrom));
		if(this.assignments!=null)
			assignments.addAll(this.assignments);
		assignments.toDialect(writer);
	}

	private void toEDialect(CodeWriter writer) {
		type.toDialect(writer);
		if (copyFrom != null) {
			writer.append(" from ");
			writer.append(copyFrom.toString());
			if (assignments != null && assignments.size()>0)
				writer.append(",");
		}
		if (assignments != null)
			assignments.toDialect(writer);
	}
	
	
	public void checkFirstHomonym(Context context, CategoryDeclaration decl) {
		if(checked)
			return;
		if(assignments!=null && assignments.size()>0)
			checkFirstHomonym(context, decl, assignments.get(0));
		checked = true;
	}
	

	private void checkFirstHomonym(Context context, CategoryDeclaration decl, ArgumentAssignment assignment) {
		if(assignment.getArgument()==null) {
			IExpression exp = assignment.getExpression();
			// when coming from UnresolvedCall, could be an homonym
			Identifier name = null;
			if(exp instanceof UnresolvedIdentifier) 
				name = ((UnresolvedIdentifier)exp).getId();
			else if(exp instanceof InstanceExpression)
				name = ((InstanceExpression)exp).getId();
			if(name!=null && decl.hasAttribute(context, name)) {
				// convert expression to name to avoid translation issues
				assignment.setArgument(new AttributeArgument(name));
				assignment.setExpression(null);
			}
		}
	}

	@Override
	public IType check(Context context) {
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd==null)
			throw new SyntaxError("Unknown category " + type.getTypeName());
		checkFirstHomonym(context, cd);
		cd.checkConstructorContext(context);
		if(copyFrom!=null) {
			IType cft = copyFrom.check(context);
			if(!(cft instanceof CategoryType) && (cft!=DocumentType.instance()))
				throw new SyntaxError("Cannot copy from " + cft.getTypeName());
		}
		if(assignments!=null) {
			context = context.newChildContext();
			for(ArgumentAssignment assignment : assignments) {
				if(!cd.hasAttribute(context, assignment.getArgumentId()))
					throw new SyntaxError("\"" + assignment.getArgumentId() + 
						"\" is not an attribute of " + type.getTypeName());	
				assignment.check(context);
			}
		}
		return cd.getType(context); // could be a resource rather than a category;
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd==null)
			throw new SyntaxError("Unknown category " + type.getTypeName());
		checkFirstHomonym(context, cd);
		IInstance instance = type.newInstance(context);
		instance.setMutable(true);
		try {
			if(copyFrom!=null) {
				Object copyObj = copyFrom.interpret(context);
				if(copyObj instanceof IInstance) {
					IInstance copyFrom = (IInstance)copyObj;
					for(Identifier name : copyFrom.getMemberNames()) {
						if(cd.hasAttribute(context, name)) {
							IValue value = copyFrom.getMember(context, name, false);
							if(value!=null && value.isMutable() && !type.isMutable())
								throw new NotMutableError();
							instance.setMember(context, name, value);
						}
					}
				} else if (copyObj instanceof Document) {
					Document copyFrom = (Document)copyObj;
					for(Identifier name : copyFrom.getMemberNames()) {
						if(cd.hasAttribute(context, name)) {
							IValue value = copyFrom.getMember(context, name, false);
							if(value!=null && value.isMutable() && !type.isMutable())
								throw new NotMutableError();
							if(value!=NullValue.instance()) {
								AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, name);
								value = decl.getType(context).convertIValueToIValue(context, value);
							}
							instance.setMember(context, name, value);
						}
					}
				}
			}
			if(assignments!=null) {
				for(ArgumentAssignment assignment : assignments) {
					Identifier argId = assignment.getArgumentId();
					if(cd.hasAttribute(context, argId)) {
						IValue value = assignment.getExpression().interpret(context);
						if(value!=null && value.isMutable() && !type.isMutable())
							throw new NotMutableError();
						instance.setMember(context, argId, value);
					} else 
						context.getProblemListener().reportIllegalMember(argId.toString(), argId);
				}
			}
		} finally {
			instance.setMutable(type.isMutable());
		}
		return instance;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd==null)
			throw new SyntaxError("Unknown category " + type.getTypeName());
		checkFirstHomonym(context, cd);
		Type klass = getConcreteType(context);
		ResultInfo result = CompilerUtils.compileNewInstance(method, klass);
		compileSetMutable(context, method, flags, result, true);
		compileCopyFrom(context, method, flags, result);
		compileAssignments(context, method, flags, result);
		compileSetMutable(context, method, flags, result, type.isMutable());
		return new ResultInfo(getInterfaceType(context));
	}

	private void compileSetMutable(Context context, MethodInfo method, Flags flags, ResultInfo thisInfo, boolean set) {
		if(thisInfo.isPromptoCategory()) {
			method.addInstruction(Opcode.DUP); // this
			method.addInstruction(set ? Opcode.ICONST_1 : Opcode.ICONST_0); 
			MethodConstant m = new MethodConstant(thisInfo.getType(), "setMutable", boolean.class, void.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		}
	}

	private void compileAssignments(Context context, MethodInfo method, Flags flags, ResultInfo thisInfo) {
		if(assignments!=null) 
			assignments.forEach((a)->
				compileAssignment(context, method, flags, thisInfo, a));
	}

	private void compileAssignment(Context context, MethodInfo method, Flags flags, ResultInfo thisInfo, ArgumentAssignment assignment) {
		// keep a copy of new instance on top of the stack
		method.addInstruction(Opcode.DUP);
		// get value
		ResultInfo valueInfo = assignment.getExpression().compile(context, method, flags);
		// check immutable member
		compileCheckImmutable(context, method, flags, valueInfo);
		// call setter
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, assignment.getArgumentId());
		FieldInfo field = decl.toFieldInfo(context);
		// cast if required
		if(field.getType()==Boolean.class && !valueInfo.isPromptoAttribute())
			CompilerUtils.booleanToBoolean(method, valueInfo);
		else if(field.getType()==Double.class && !valueInfo.isPromptoAttribute())
			CompilerUtils.numberToDouble(method, valueInfo);
		else if(field.getType()==Long.class && !valueInfo.isPromptoAttribute())
			CompilerUtils.numberToLong(method, valueInfo);
		// call setter
		MethodConstant m = new MethodConstant(thisInfo.getType(), CompilerUtils.setterName(field.getName().getValue()), field.getType(), void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
	}

	private void compileCheckImmutable(Context context, MethodInfo method, Flags flags, ResultInfo valueInfo) {
		if(!type.isMutable() && valueInfo.isPromptoCategory()) {
			StackState stackState = method.captureStackState();
			method.addInstruction(Opcode.DUP); 
			OffsetListenerConstant offsetListener = method.addOffsetListener(new OffsetListenerConstant());
			method.activateOffsetListener(offsetListener);
			method.addInstruction(Opcode.IFNULL, offsetListener);
			method.addInstruction(Opcode.DUP); 
			InterfaceConstant m = new InterfaceConstant(IMutable.class, "checkImmutable", void.class);
			method.addInstruction(Opcode.INVOKEINTERFACE, m);
			method.inhibitOffsetListener(offsetListener);
			method.restoreFullStackState(stackState);
			method.placeLabel(stackState);
		}
	}

	private void compileCopyFrom(Context context, MethodInfo method, Flags flags, ResultInfo thisInfo) {
		if(copyFrom==null)
			return;
		CategoryDeclaration thisCd = context.getRegisteredDeclaration(CategoryDeclaration.class, this.type.getTypeNameId());
		IType otherType = copyFrom.check(context);
		if(otherType==DocumentType.instance())
			compileCopyFromDocument(context, method, flags, thisCd, thisInfo);
		else {
			CategoryDeclaration otherCd = context.getRegisteredDeclaration(CategoryDeclaration.class, otherType.getTypeNameId());
			compileCopyFromInstance(context, method, flags, thisCd, otherCd, thisInfo);
		}
	}

	private void compileCopyFromDocument(Context context, MethodInfo method, Flags flags, 
			CategoryDeclaration thisCd, ResultInfo thisInfo) {
		ResultInfo copyFromInfo = copyFrom.compile(context, method, flags.withPrimitive(false));
		Set<Identifier> attrIds = thisCd.getAllAttributes(context);
		for(Identifier attrId : attrIds)
			compileCopyAttributeFromDocument(context, method, flags, thisCd, attrId, thisInfo, copyFromInfo);
		method.addInstruction(Opcode.POP);
	}

	
	private void compileCopyAttributeFromDocument(Context context, MethodInfo method, Flags flags, 
			CategoryDeclaration thisCd, Identifier attrId, ResultInfo thisInfo, ResultInfo copyFromInfo) {
		if(willBeAssigned(attrId))
			return;
		// keep a copy of copyFrom on top of the stack
		method.addInstruction(Opcode.DUP); // -> new, copyFrom, copyFrom
		// call get on copyFrom document
		method.addInstruction(Opcode.LDC, new StringConstant(attrId.toString()));
		MethodConstant m = new MethodConstant(PromptoDocument.class, "get", Object.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		// convert to target type
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, attrId);
		FieldInfo field = decl.toFieldInfo(context);
		decl.getType(context).compileConvertObjectToExact(context, method, flags);
		// keep the new instance at top of the stack (currently new, copyFrom, value)
		method.addInstruction(Opcode.DUP_X2); // -> value, new, copyFrom, value
		method.addInstruction(Opcode.POP); // -> value, new, copyFrom
		method.addInstruction(Opcode.DUP_X2); // -> copyFrom, value, new, copyFrom
		method.addInstruction(Opcode.POP); // -> copyFrom, value, new
		method.addInstruction(Opcode.DUP_X2); // -> new, copyFrom, value, new
		method.addInstruction(Opcode.SWAP); // -> new, copyFrom, new, value
		// call setter on new instance (a class)
		m = new MethodConstant(thisInfo.getType(), 
				CompilerUtils.setterName(attrId.toString()), field.getType(), void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
	}
	
	private void compileCopyFromInstance(Context context, MethodInfo method, Flags flags, 
			CategoryDeclaration thisCd, CategoryDeclaration otherCd, ResultInfo thisInfo) {
		ResultInfo copyFromInfo = copyFrom.compile(context, method, flags.withPrimitive(false));
		Set<Identifier> attrIds = thisCd.getAllAttributes(context);
		for(Identifier attrId : attrIds)
			compileCopyAttributeFromInstance(context, method, flags, thisCd, otherCd, attrId, thisInfo, copyFromInfo);
		method.addInstruction(Opcode.POP);
	}

	private void compileCopyAttributeFromInstance(Context context, MethodInfo method, Flags flags, 
			CategoryDeclaration thisCd, CategoryDeclaration otherCd, Identifier attrId, ResultInfo thisInfo, ResultInfo copyFromInfo) {
		if(willBeAssigned(attrId) || !otherCd.hasAttribute(context, attrId))
			return;
		// keep a copy of copyFrom on top of the stack
		method.addInstruction(Opcode.DUP); // -> new, copyFrom, copyFrom
		// call getter on copyFrom instance (an interface)
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, attrId);
		FieldInfo field = decl.toFieldInfo(context);
		InterfaceConstant i = new InterfaceConstant(copyFromInfo.getType(), 
				CompilerUtils.getterName(attrId.toString()), field.getType());
		method.addInstruction(Opcode.INVOKEINTERFACE, i);
		// keep the new instance at top of the stack (currently new, copyFrom, value)
		method.addInstruction(Opcode.DUP_X2); // -> value, new, copyFrom, value
		method.addInstruction(Opcode.POP); // -> value, new, copyFrom
		method.addInstruction(Opcode.DUP_X2); // -> copyFrom, value, new, copyFrom
		method.addInstruction(Opcode.POP); // -> copyFrom, value, new
		method.addInstruction(Opcode.DUP_X2); // -> new, copyFrom, value, new
		method.addInstruction(Opcode.SWAP); // -> new, copyFrom, new, value
		// call setter on new instance (a class)
		MethodConstant m = new MethodConstant(thisInfo.getType(), 
				CompilerUtils.setterName(attrId.toString()), field.getType(), void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
	}

	private boolean willBeAssigned(Identifier name) {
		if(assignments!=null) 
			for(ArgumentAssignment assignment : assignments) 
				if(name.equals(assignment.getArgumentId()))
					return true;
		return false;
	}

	private Type getInterfaceType(Context context) {
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd instanceof NativeCategoryDeclaration)
			return ((NativeCategoryDeclaration)cd).getBoundClass(false);
		else 
			return CompilerUtils.getCategoryInterfaceType(cd.getId());
	}

	private Type getConcreteType(Context context) {
		CategoryDeclaration cd = context.getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
		if(cd instanceof NativeCategoryDeclaration)
			return ((NativeCategoryDeclaration)cd).getBoundClass(false);
		else 
			return CompilerUtils.getCategoryConcreteType(cd.getId());
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		CategoryDeclaration cd = transpiler.getContext().getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
	    cd.declare(transpiler);
	    if(this.copyFrom!=null)
	        this.copyFrom.declare(transpiler);
	    if(this.assignments!=null)
	        this.assignments.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		CategoryDeclaration decl = transpiler.getContext().getRegisteredDeclaration(CategoryDeclaration.class, type.getTypeNameId());
	    if (decl instanceof NativeWidgetDeclaration)
	        this.transpileNativeWidget(transpiler, (NativeWidgetDeclaration)decl);
	    else if (decl instanceof ConcreteWidgetDeclaration)
	        this.transpileConcreteWidget(transpiler, (ConcreteWidgetDeclaration)decl);
	    else if (decl instanceof NativeCategoryDeclaration)
	        this.transpileNative(transpiler, (NativeCategoryDeclaration)decl);
	    else
	        this.transpileConcrete(transpiler);
	    return false;
	}

	private void transpileConcrete(Transpiler transpiler) {
	    transpiler = transpiler.newInstanceTranspiler(this.type);
	    transpiler.append("new ").append(this.type.getTypeName()).append("(");
	    if(this.copyFrom!=null)
	        this.copyFrom.transpile(transpiler);
	    else
	        transpiler.append("null");
	    transpiler.append(", ");
	    this.transpileAssignments(transpiler);
	    transpiler.append(", ");
	    transpiler.append(this.type.isMutable());
	    transpiler.append(")");
	    transpiler.flush();
	}

	private void transpileConcreteWidget(Transpiler transpiler, ConcreteWidgetDeclaration decl) {
	    transpiler = transpiler.newInstanceTranspiler(this.type);
	    transpiler.append("new ").append(this.type.getTypeName()).append("()");
	    transpiler.flush();
	}

	private void transpileAssignments(Transpiler transpiler) {
	    if(this.assignments!=null) {
	        transpiler.append("{");
	        this.assignments.forEach(assignment -> {
	            transpiler.append(assignment.getArgument().getName()).append(":");
	            assignment.getExpression().transpile(transpiler);
	            transpiler.append(", ");
	        });
	        transpiler.trimLast(2);
	        transpiler.append("}");
	    } else
	        transpiler.append("null");
	}

	private void transpileNative(Transpiler transpiler, NativeCategoryDeclaration decl) {
	    String bound = decl.getTranspiledBoundClass();
	    transpiler.append("new_").append(bound).append("(");
	    this.transpileAssignments(transpiler);
	    transpiler.append(")");
	}
	
	private void transpileNativeWidget(Transpiler transpiler, NativeWidgetDeclaration decl) {
	    String bound = decl.getTranspiledBoundClass();
	    transpiler.append("new ").append(bound).append("()");
	}


}
