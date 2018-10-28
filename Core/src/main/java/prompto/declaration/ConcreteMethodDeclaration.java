package prompto.declaration;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prompto.argument.CategoryArgument;
import prompto.argument.CodeArgument;
import prompto.argument.IArgument;
import prompto.argument.ValuedCodeArgument;
import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.Descriptor.Method;
import prompto.compiler.FieldConstant;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceType;
import prompto.compiler.LocalVariableTableAttribute;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.NameAndTypeConstant;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoType;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StringConstant;
import prompto.error.PromptoError;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoMethod;
import prompto.runtime.Context;
import prompto.statement.DeclarationStatement;
import prompto.statement.StatementList;
import prompto.transpiler.Transpiler;
import prompto.type.DictType;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.type.TextType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.CodeValue;
import prompto.value.IValue;

public class ConcreteMethodDeclaration extends BaseMethodDeclaration implements IMethodDeclaration {

	StatementList statements;
	DeclarationStatement<IMethodDeclaration> declarationOf;
	Map<Identifier, ValuedCodeArgument> codeArguments;
	
	@SuppressWarnings("unchecked")
	public ConcreteMethodDeclaration(Identifier name, ArgumentList arguments, IType returnType, StatementList statements) {
		super(name, arguments, returnType);
		if(statements==null)
			statements = new StatementList();
		this.statements = statements;
		statements.stream()
			.filter(s->s instanceof DeclarationStatement)
			.map(s->(DeclarationStatement<IDeclaration>)s)
			.forEach(s->s.getDeclaration().setClosureOf(this));
	}

	public StatementList getStatements() {
		return statements;
	}
	
	@Override
	public void setDeclarationOf(DeclarationStatement<IMethodDeclaration> statement) {
		declarationOf = statement;
	}
	
	@Override
	public DeclarationStatement<IMethodDeclaration> getDeclarationOf() {
		return declarationOf;
	}
	
	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(writer.isGlobalContext())
			writer = writer.newLocalWriter();
		registerArguments(writer.getContext());
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
	
	protected void toMDialect(CodeWriter writer) {
		writer.append("def ");
		writer.append(getName());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(")");
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("->");
			returnType.toDialect(writer);
		}
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	protected void toEDialect(CodeWriter writer) {
		writer.append("define ");
		writer.append(getName());
		writer.append(" as method ");
		arguments.toDialect(writer);
		if(returnType!=null && returnType!=VoidType.instance()) {
			writer.append("returning ");
			returnType.toDialect(writer);
			writer.append(" ");
		}
		writer.append("doing:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	protected void toODialect(CodeWriter writer) {
		if(returnType!=null && returnType!=VoidType.instance()) {
			returnType.toDialect(writer);
			writer.append(" ");
		}
		writer.append("method ");
		writer.append(getName());
		writer.append(" (");
		arguments.toDialect(writer);
		writer.append(") {\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		writer.append("}\n");
	}

	@Override
	public IType check(Context context, boolean isStart) {
		if(canBeChecked(context, isStart))
			return fullCheck(context, isStart);
		else
			return VoidType.instance();
	}
	
	private boolean canBeChecked(Context context, boolean isStart) {
		if(isStart)
			return !isTemplate();
		else
			return true;
	}
	
	@Override
	public boolean isTemplate() {
		// if at least one argument is 'Code'
		if(arguments==null)
			return false;
		for( IArgument arg : arguments) {
			if(arg instanceof CodeArgument)
				return true;
		}
		return false;
	}

	private IType fullCheck(Context context, boolean isStart) {
		if(isStart) {
			context = context.newLocalContext();
			registerArguments(context);
		}
		if(arguments!=null)
			arguments.check(context);
		return checkStatements(context);
	}

	protected IType checkStatements(Context context) {
		return statements.check(context, returnType);
	}

	public IType checkChild(Context context) {
		if(arguments!=null)
			arguments.check(context);
		Context child = context.newChildContext();
		registerArguments(child);
		return checkStatements(child);
	}

	@Override
	public void check(ConcreteCategoryDeclaration declaration, Context context) {
		context = context.newInstanceContext(declaration.getType(context), false);
		checkChild(context);
	}	

	@Override
	public IValue interpret(Context context) throws PromptoError {
		return statements.interpret(context);
	}
	
	@Override
	public void compile(Context context, boolean isStart, ClassFile classFile) {
		compile(context, isStart, classFile, getName());
	}
	
	public void compile(Context context, boolean isStart, ClassFile classFile, String methodName) {
		context = prepareContext(context, isStart);
		IType returnType = check(context, false);
		MethodInfo method = createMethodInfo(context, classFile, returnType, methodName);
		registerLocals(context, classFile, method);
		produceByteCode(context, method, returnType);
	}
	
	private void produceByteCode(Context context, MethodInfo method, IType returnType) {
		statements.compile(context, method, new Flags().withMember(this.memberOf!=null));
		// add return for void
		if(returnType==VoidType.instance())
			method.addInstruction(Opcode.RETURN);
	}

	protected void registerLocals(Context context, ClassFile classFile, MethodInfo method) {
		if(Modifier.isAbstract(classFile.getModifiers())) // TODO find a more accurate way
			method.addModifier(Modifier.STATIC); // otherwise it's a member method
		else 
			method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		List<IArgument> args = arguments.stripOutTemplateArguments();
		args.forEach((arg)->
			arg.registerLocal(context, method, new Flags()));
		args.forEach((arg)->
			arg.extractLocal(context, method, new Flags()));
	}

	@Override
	public String compileTemplate(Context context, boolean isStart, ClassFile classFile) {
		String methodName = computeTemplateName(classFile);
		compile(context, isStart, classFile, methodName);
		return methodName;
	}


	private String computeTemplateName(ClassFile classFile) {
		int i = 0;
		while(true) {
			String methodName = this.getName() + '$' + (++i);
			if(!classFile.hasMethod(methodName))
				return methodName;
		}
	}

	@Override
	public boolean isEligibleAsMain() {
		if(arguments.size()==0)
			return true;
		if(arguments.size()==1) {
			IArgument arg = arguments.getFirst();
			if(arg instanceof CategoryArgument) {
				IType type = ((CategoryArgument)arg).getType();
				if(type instanceof DictType)
					return ((DictType)type).getItemType()==TextType.instance();
			}
		}
		return super.isEligibleAsMain();
	}

	public Type compileClosureClass(Context context, MethodInfo method) {
		IType returnType = this.checkChild(context);
		InterfaceType intf = new InterfaceType(arguments, returnType);
		Type innerType = getClosureClassType(method);
		ClassFile classFile = new ClassFile(innerType);
		classFile.setSuperClass(new ClassConstant(Object.class));
		classFile.addAttribute(intf.computeSignature(context, Object.class));
		classFile.addInterface(intf.getInterfaceType());
		classFile.setEnclosingMethod(method);
		LocalVariableTableAttribute locals = method.getLocals();
		compileClosureFields(context, classFile, locals);
		compileClosureConstructor(context, classFile, locals);
		context = context.newClosureContext(new MethodType(this));
		registerArguments(context);
		compile(context, false, classFile, intf.getInterfaceMethodName());
		method.getClassFile().addInnerClass(classFile);
		return innerType;
	}

	private Type getClosureClassType(MethodInfo method) {
		String innerClassName = method.getClassFile().getThisClass().getType().getTypeName();
		if(closureOf!=null && closureOf.getMemberOf()!=null)
			innerClassName += "$" + closureOf.getName();
		innerClassName += "$" + this.getName();
		return new PromptoType(innerClassName); 
	}

	private void compileClosureConstructor(Context context, ClassFile classFile, LocalVariableTableAttribute locals) {
		if(locals.getEntries().isEmpty())
			CompilerUtils.compileEmptyConstructor(classFile);
		else {
			Descriptor.Method proto = getClosureConstructorProto(locals);
			MethodInfo method = classFile.newMethod("<init>", proto);
			method.registerLocal("this", VerifierType.ITEM_UninitializedThis, classFile.getThisClass());
			locals.getEntries().forEach((local)->{
				Type type = ((StackLocal.ObjectLocal)local).getClassName().getType();
				String name = "this".equals(local.getName()) ? "this$0" : local.getName();
				if("this".equals(name)) {
					name = "this$0";
					type = CompilerUtils.categoryConcreteTypeFrom(type.getTypeName());
				}
				method.registerLocal(name, VerifierType.ITEM_Object, new ClassConstant(type));
				});
			method.addInstruction(Opcode.ALOAD_0, classFile.getThisClass());
			MethodConstant m = new MethodConstant(classFile.getSuperClass(), "<init>", void.class);
			method.addInstruction(Opcode.INVOKESPECIAL, m);
			locals.getEntries().forEach((local)->{
				method.addInstruction(Opcode.ALOAD_0, classFile.getThisClass());
				Type type = ((StackLocal.ObjectLocal)local).getClassName().getType();
				String name = local.getName();
				if("this".equals(name)) {
					name = "this$0";
					type = CompilerUtils.categoryConcreteTypeFrom(type.getTypeName());
				}
				CompilerUtils.compileALOAD(method, name);
				FieldConstant field = new FieldConstant(classFile.getThisClass(), name, type);
				method.addInstruction(Opcode.PUTFIELD, field);
				});
			method.addInstruction(Opcode.RETURN);
		}
	}

	private Method getClosureConstructorProto(LocalVariableTableAttribute locals) {
		List<Type> list = new ArrayList<>();
		locals.getEntries().forEach((local)->
			list.add(((StackLocal.ObjectLocal)local).getClassName().getType()));
		return new Descriptor.Method(list.toArray(new Type[list.size()]), void.class);
	}

	private void compileClosureFields(Context context, ClassFile classFile, LocalVariableTableAttribute locals) {
		locals.getEntries().forEach((local)->
			compileClosureField(context, classFile, local));
	}

	private void compileClosureField(Context context, ClassFile classFile, StackLocal local) {
		Type type = ((StackLocal.ObjectLocal)local).getClassName().getType();
		String name = local.getName();
		if("this".equals(name)) {
			name = "this$0";
			type = CompilerUtils.categoryConcreteTypeFrom(type.getTypeName());
		}
		FieldInfo field = new FieldInfo(name, type);
		classFile.addField(field);
	}

	public ResultInfo compileMethodInstance(Context context, MethodInfo method, Flags flags) {
		if(closureOf!=null)
			return compileClosureInstance(context, method, flags);
		else
			return compileMethodReference(context, method, flags);
	}
	
	private ResultInfo compileMethodReference(Context context, MethodInfo method, Flags flags) {
		// TODO use LambdaMetaFactory 
		Type methodsClassType = this.memberOf==null ? CompilerUtils.getGlobalMethodType(this.id) : CompilerUtils.getCategoryConcreteType(this.memberOf.getId());
		method.addInstruction(Opcode.LDC, new ClassConstant(methodsClassType));
		method.addInstruction(Opcode.LDC, new StringConstant(id.toString()));
		if(this.memberOf==null)
			method.addInstruction(Opcode.ACONST_NULL, new ClassConstant(Object.class));
		else
			method.addInstruction(Opcode.ALOAD_0, new ClassConstant(Object.class)); // this
		NameAndTypeConstant nameAndType = new NameAndTypeConstant("newMethodReference", new Descriptor.Method(Class.class, String.class, Object.class, Object.class));
		MethodConstant mc = new MethodConstant(new ClassConstant(PromptoMethod.class), nameAndType);
		method.addInstruction(Opcode.INVOKESTATIC, mc);
		return new ResultInfo(methodsClassType);
	}

	public ResultInfo compileClosureInstance(Context context, MethodInfo method, Flags flags) {
		Type innerType = getClosureClassType(method);
		LocalVariableTableAttribute locals = method.getLocals(); // TODO: use a copy saved when constructor is created
		if(locals.getEntries().isEmpty())
			return CompilerUtils.compileNewInstance(method, innerType); 
		else {
			CompilerUtils.compileNewRawInstance(method, innerType);
			method.addInstruction(Opcode.DUP); 
			locals.getEntries().forEach((local)->
				CompilerUtils.compileALOAD(method, local.getName()));
			Descriptor.Method proto = getClosureConstructorProto(locals);
			IOperand c = new MethodConstant(innerType, "<init>", proto);
			method.addInstruction(Opcode.INVOKESPECIAL, c);
			return new ResultInfo(innerType);
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		if(declaring)
			return;
		declaring = true;
		try {
		    if(this.memberOf==null) {
		        transpiler = transpiler.newLocalTranspiler();
		        transpiler.declare(this);
		        this.declareArguments(transpiler);
		    }
	    	this.registerArguments(transpiler.getContext());
		    this.statements.declare(transpiler);
		} finally {
			declaring = false;
		}
	}
	
	@Override
	public void declareChild(Transpiler transpiler) {
	    this.declareArguments(transpiler);
	    transpiler = transpiler.newChildTranspiler(null);
	    this.registerArguments(transpiler.getContext());
	    this.statements.declare(transpiler);
	}
	
	@Override
	public void fullDeclare(Transpiler transpiler, Identifier methodName) {
		ConcreteMethodDeclaration declaration = new ConcreteMethodDeclaration(getId(), getArguments(), this.returnType, this.statements);
	    declaration.memberOf = this.memberOf;
	    transpiler.declare(declaration);
	    this.statements.declare(transpiler);
	    // remember code arguments
	    declaration.codeArguments = new HashMap<>();
	    getArguments().stream()
	    	.filter(arg ->arg instanceof CodeArgument )
	    	.forEach(arg -> {
	    		CodeValue value = (CodeValue)transpiler.getContext().getValue(arg.getId()); 
	    		declaration.codeArguments.put(arg.getId(), new ValuedCodeArgument(arg.getId(), value));
	    });
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.registerArguments(transpiler.getContext());
	    this.registerCodeArguments(transpiler.getContext());
	    this.transpileProlog(transpiler);
	    this.statements.transpile(transpiler);
	    this.transpileEpilog(transpiler);
	    return true;
	}

	private void registerCodeArguments(Context context) {
		if(this.isTemplate()) {
		    if(this.codeArguments==null)
		        return;
		    this.codeArguments.forEach( (k,v) -> context.setValue(v.getId(), v.getValue()));
		}
		
	}

}
