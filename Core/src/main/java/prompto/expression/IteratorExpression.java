package prompto.expression;

import java.lang.reflect.Type;

import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.Descriptor;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoType;
import prompto.compiler.ResultInfo;
import prompto.compiler.Tags;
import prompto.error.PromptoError;
import prompto.grammar.Identifier;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoIterable;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.statement.ReturnStatement;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.IteratorType;
import prompto.utils.CodeWriter;
import prompto.value.IIterable;
import prompto.value.IValue;
import prompto.value.IterableValue;

public class IteratorExpression implements IExpression {

	Identifier id;
	IExpression source;
	IExpression expression;
	
	public IteratorExpression(Identifier id, IExpression source, IExpression exp) {
		this.id = id;
		this.source = source;
		this.expression = exp;
	}
	
	@Override
	public String toString() {
		return expression.toString() +
			" for each " +
			id.toString() +
			" in " + 
			source.toString();
	}

	@Override
	public IteratorType check(Context context) {
		IType srcType = source.check(context).checkIterator(context);
		Context child = context.newChildContext();
		child.registerValue(new Variable(id, srcType));
		IType resultType = expression.check(child);
		return new IteratorType(resultType);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IType srcType = source.check(context).checkIterator(context);
		Context child = context.newChildContext();
		child.registerValue(new Variable(id, srcType));
		IType resultType = expression.check(child);
		IValue items = source.interpret(context);
		IterableWithCounts<IValue> iterable = getIterable(context, items);
		return new IterableValue(context, id, srcType, iterable, expression, resultType);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		Type innerClassType = compileInnerClass(context, method.getClassFile());
		// instantiate inner class
		ClassConstant innerClass = new ClassConstant(innerClassType);
		method.addInstruction(Opcode.NEW, innerClass);
		method.addInstruction(Opcode.DUP);
		// get iterable
		ResultInfo srcinfo = source.compile(context, method, flags);
		// get the length
		method.addInstruction(Opcode.DUP);
		if(srcinfo.isInterface()) {
			InterfaceConstant c = new InterfaceConstant(srcinfo.getType(), "getNativeCount", long.class);
			method.addInstruction(Opcode.INVOKEINTERFACE, c);
		} else {
			MethodConstant c = new MethodConstant(srcinfo.getType(), "getNativeCount", long.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, c);
		}
		// call the constructor
		Descriptor.Method proto = new Descriptor.Method(Iterable.class, long.class, void.class);
		MethodConstant m = new MethodConstant(innerClass, "<init>", proto);
		method.addInstruction(Opcode.INVOKESPECIAL, m);
		// done
		return new ResultInfo(IterableWithCounts.class);
	}

	private Type compileInnerClass(Context context, ClassFile parentClass) {
		int innerClassIndex = 1 + parentClass.getInnerClasses().size();
		String innerClassName = parentClass.getThisClass().getType().getTypeName() + '$' + innerClassIndex;
		Type innerClassType = new PromptoType(innerClassName); 
		ClassFile classFile = new ClassFile(innerClassType);
		classFile.setSuperClass(new ClassConstant(PromptoIterable.class));
		compileInnerClassConstructor(classFile);
		compileInnerClassExpression(context, classFile);
		parentClass.addInnerClass(classFile);;
		return innerClassType;
	}

	private MethodInfo compileInnerClassConstructor(ClassFile classFile) {
		Descriptor.Method proto = new Descriptor.Method(Iterable.class, long.class, void.class);
		MethodInfo method = classFile.newMethod("<init>", proto);
		method.registerLocal("this", VerifierType.ITEM_UninitializedThis, classFile.getThisClass());
		method.registerLocal("iterable", VerifierType.ITEM_Object, new ClassConstant(Iterable.class));
		method.registerLocal("count", VerifierType.ITEM_Long, null);
		method.addInstruction(Opcode.ALOAD_0, classFile.getThisClass());
		method.addInstruction(Opcode.ALOAD_1, new ClassConstant(Iterable.class));
		method.addInstruction(Opcode.LLOAD_2, new ClassConstant(long.class));
		MethodConstant m = new MethodConstant(classFile.getSuperClass(), "<init>", proto);
		method.addInstruction(Opcode.INVOKESPECIAL, m);
		method.addInstruction(Opcode.RETURN);
		return method;
	}

	private void compileInnerClassExpression(Context context, ClassFile classFile) {
		IType paramIType = source.check(context).checkIterator(context);
		context = context.newChildContext();
		context.registerValue(new Variable(id, paramIType));
		Type paramType = paramIType.getJavaType(context);
		Type resultType = expression.check(context).getJavaType(context);
		compileInnerClassBridgeMethod(classFile, paramType, resultType);
		compileInnerClassApplyMethod(context, classFile, paramType, resultType);
	}

	private void compileInnerClassApplyMethod(Context context, ClassFile classFile, Type paramType, Type resultType) {
		// create the "apply" method itself
		Descriptor.Method proto = new Descriptor.Method(paramType, resultType);
		MethodInfo method = classFile.newMethod("apply", proto);
		method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		method.registerLocal(id.toString(), VerifierType.ITEM_Object, new ClassConstant(paramType));
		ReturnStatement stmt = new ReturnStatement(expression);
		stmt.compile(context, method, new Flags());
	}

	private void compileInnerClassBridgeMethod(ClassFile classFile, Type paramType, Type resultType) {
		// create a bridge "apply" method to convert Object -> paramType
		Descriptor.Method proto = new Descriptor.Method(Object.class, Object.class);
		MethodInfo method = classFile.newMethod("apply", proto);
		method.addModifier(Tags.ACC_BRIDGE | Tags.ACC_SYNTHETIC);
		method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		method.registerLocal(id.toString(), VerifierType.ITEM_Object, new ClassConstant(Object.class));
		method.addInstruction(Opcode.ALOAD_0, classFile.getThisClass());
		method.addInstruction(Opcode.ALOAD_1, new ClassConstant(Object.class));
		method.addInstruction(Opcode.CHECKCAST, new ClassConstant(paramType));
		proto = new Descriptor.Method(paramType, resultType);
		MethodConstant c = new MethodConstant(classFile.getThisClass(), "apply", proto);
		method.addInstruction(Opcode.INVOKEVIRTUAL, c);
		method.addInstruction(Opcode.ARETURN, new ClassConstant(resultType));
	}

	@SuppressWarnings("unchecked")
	private IterableWithCounts<IValue> getIterable(Context context, Object src) {
		if (src instanceof IIterable) 
			return ((IIterable<IValue>) src).getIterable(context);
		else if(src instanceof IterableWithCounts)
			return (IterableWithCounts<IValue>)src;
		else
			throw new InternalError("Should never get there!");
	}

	@Override
	public void toDialect(CodeWriter writer) {
		IType srcType = source.check(writer.getContext()).checkIterator(writer.getContext());
		writer = writer.newChildWriter();
		writer.getContext().registerValue(new Variable(id, srcType));
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
		expression.toDialect(writer);
		writer.append(" for ");
		writer.append(id.toString());
		writer.append(" in ");
		source.toDialect(writer);
	}

	private void toODialect(CodeWriter writer) {
		expression.toDialect(writer);
		writer.append(" for each ( ");
		writer.append(id.toString());
		writer.append(" in ");
		source.toDialect(writer);
		writer.append(" )");
	}

	private void toEDialect(CodeWriter writer) {
		expression.toDialect(writer);
		writer.append(" for each ");
		writer.append(id.toString());
		writer.append(" in ");
		source.toDialect(writer);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    IType sourceType = this.source.check(transpiler.getContext());
	    sourceType.declareIterator(transpiler, this.id, this.expression);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		IType sourceType = this.source.check(transpiler.getContext());
	    this.source.transpile(transpiler);
	    sourceType.transpileIterator(transpiler, this.id, this.expression);
	    return false;
	}

}
