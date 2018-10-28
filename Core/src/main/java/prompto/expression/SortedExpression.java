package prompto.expression;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.BiFunction;

import prompto.compiler.ByteOperand;
import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoType;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.Tags;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.NullReferenceError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoList;
import prompto.runtime.Context;
import prompto.runtime.MethodFinder;
import prompto.runtime.Variable;
import prompto.statement.MethodCall;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.type.ListType;
import prompto.type.SetType;
import prompto.utils.CodeWriter;
import prompto.value.ExpressionValue;
import prompto.value.IInstance;
import prompto.value.IValue;
import prompto.value.ListValue;
import prompto.value.SetValue;

public class SortedExpression implements IExpression {

	IExpression source;
	IExpression key;
	boolean descending;
	
	public SortedExpression(IExpression source, boolean descending) {
		this.source = source;
		this.descending = descending;
	}

	public SortedExpression(IExpression source, boolean descending, IExpression key) {
		this.source = source;
		this.descending = descending;
		this.key = key;
	}

	@Override
	public void toDialect(CodeWriter writer) {
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

	private void toEDialect(CodeWriter writer) {
		writer.append("sorted ");
		if(descending)
			writer.append("descending ");
		source.toDialect(writer);
		if(key!=null) {
			writer.append(" with ");
			IExpression keyExp = key;
			if(keyExp instanceof UnresolvedIdentifier) try {
				keyExp = ((UnresolvedIdentifier)keyExp).resolve(writer.getContext(), false, false);
			} catch (SyntaxError e) {
				// TODO add warning 
			}
			if(keyExp instanceof InstanceExpression)
				((InstanceExpression)keyExp).toDialect(writer, false);
			else
				keyExp.toDialect(writer);
			writer.append(" as key");
		}
	}	

	private void toODialect(CodeWriter writer) {
		writer.append("sorted ");
		if(descending)
			writer.append("desc ");
		writer.append("(");
		source.toDialect(writer);
		if(key!=null) {
			writer.append(", key = ");
			key.toDialect(writer);
		}
		writer.append(")");
	}
	
	private void toMDialect(CodeWriter writer) {
		toODialect(writer);
	}
	
	
	@Override
	public IType check(Context context) {
		IType type = source.check(context);
		if(!(type instanceof ListType || type instanceof SetType))
			throw new SyntaxError("Unsupported type: " + type);
		return type;
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IType type = source.check(context);
		if(type instanceof ListType)
			return interpretList(context, (ListType)type);
		else if(type instanceof SetType)
			return interpretSet(context, (SetType)type);
		else
			throw new SyntaxError("Unsupported type: " + type);
	}

	
	private IValue interpretSet(Context context, SetType type) throws PromptoError {
		IValue value = source.interpret(context);
		if(value==null)
			throw new NullReferenceError();
		if(!(value instanceof SetValue))
			throw new InternalError("Unexpected type:" + value.getClass().getName());
		IType itemType = type.getItemType();
		Comparator<? extends IValue> cmp = getInterpretedComparator(context, itemType, value);
		PromptoList<? extends IValue> sorted = ((SetValue)value).getItems().sortUsing(cmp);
		return new ListValue(itemType, sorted);
	}

	private IValue interpretList(Context context, ListType type) throws PromptoError {
		IValue value = source.interpret(context);
		if(value==null)
			throw new NullReferenceError();
		if(!(value instanceof ListValue))
			throw new InternalError("Unexpected type:" + value.getClass().getName());
		IType itemType = type.getItemType();
		Comparator<? extends IValue> cmp = getInterpretedComparator(context, itemType, value);
		PromptoList<? extends IValue> sorted = ((ListValue)value).getItems().sortUsing(cmp);
		return new ListValue(itemType, sorted);
	}

	private Comparator<? extends IValue> getInterpretedComparator(Context context, IType itemType, IValue value) throws PromptoError {
		if(itemType instanceof CategoryType)
			return getCategoryComparator(context, (CategoryType)itemType, value);
		else
			return itemType.getComparator(descending);	
	}

	private Comparator<? extends IValue> getCategoryComparator(Context context, CategoryType itemType, IValue value) throws PromptoError {
		if(key==null)
			key = new UnresolvedIdentifier(new Identifier("key"));
		Identifier keyAsId = new Identifier(key.toString());
		IDeclaration d = itemType.getDeclaration(context);
		if(d instanceof CategoryDeclaration) {
			CategoryDeclaration decl = (CategoryDeclaration)d;
			if(decl.hasAttribute(context, keyAsId))
				return getCategoryAttributeComparator(context, keyAsId);
			else if(decl.hasMethod(context, keyAsId))
				return getCategoryMethodComparator(context, keyAsId);
			else {
				MethodCall call = createGlobalMethodCall(context, itemType, keyAsId);
				if(call!=null)
					return getCategoryGlobalMethodComparator(context, itemType, call);
				else
					return getCategoryExpressionComparator(context);
			}
		} else
			throw new UnsupportedOperationException(); // TODO
	}

	private Comparator<? extends IValue> getCategoryExpressionComparator(Context context) {
		BiFunction<IValue, IValue, Integer> cmpValues =
				descending ? 
						((k1, k2) -> IValue.compareValues(k2, k1)) :
						((k1, k2) -> IValue.compareValues(k1, k2));
		return new Comparator<IInstance>() {
			@Override
			public int compare(IInstance o1, IInstance o2) {
				try {
					IValue key1 = interpret(o1);
					IValue key2 = interpret(o2);
					return cmpValues.apply(key1, key2);
				} catch(Throwable t) {
					throw new RuntimeException(t);
				}
			}

			private IValue interpret(IInstance o) throws PromptoError {
				Context co = context.newInstanceContext(o, false);
				return key.interpret(co);
			}
		};
	}

	private Comparator<? extends IValue> getCategoryMethodComparator(Context context, Identifier identifier) {
		throw new UnsupportedOperationException(); // TODO
	}

	private MethodCall createGlobalMethodCall(Context context, CategoryType itemType, Identifier methodName) {
		try {
			IExpression exp = new ExpressionValue(itemType, itemType.newInstance(context));
			ArgumentAssignment arg = new ArgumentAssignment(null, exp); // MethodCall supports first anonymous argument
			ArgumentAssignmentList args = new ArgumentAssignmentList(Collections.singletonList(arg));
			MethodCall call = new MethodCall(new MethodSelector(methodName), args);
			MethodFinder finder = new MethodFinder(context, call);
			IMethodDeclaration decl = finder.findBestMethod(true);
			if(decl==null)
				return null;
			else
				return call;
		} catch (PromptoError e) {
			return null;
		}
	}

	private Comparator<? extends IValue> getCategoryGlobalMethodComparator(Context context, CategoryType itemType, MethodCall call) throws PromptoError {
		BiFunction<IValue, IValue, Integer> cmpValues =
				descending ? 
						((k1, k2) -> IValue.compareValues(k2, k1)) :
						((k1, k2) -> IValue.compareValues(k1, k2));
		return new Comparator<IInstance>() {
			@Override
			public int compare(IInstance o1, IInstance o2) {
				try {
					IValue key1 = interpret(o1);
					IValue key2 = interpret(o2);
					return cmpValues.apply(key1,key2);
				} catch(Throwable t) {
					throw new RuntimeException(t);
				}
			}

			private IValue interpret(IInstance o) throws PromptoError {
				ArgumentAssignment assignment = call.getAssignments().getFirst();
				assignment.setExpression(new ExpressionValue(itemType, o));
				return call.interpret(context);
			}
		};
	}

	private Comparator<? extends IValue> getCategoryAttributeComparator(Context context, Identifier name) {
		BiFunction<IValue, IValue, Integer> cmpValues =
				descending ? 
						((k1, k2) -> IValue.compareValues(k2, k1)) :
						((k1, k2) -> IValue.compareValues(k1, k2));
		return new Comparator<IInstance>() {
			@Override
			public int compare(IInstance o1, IInstance o2) {
				try {
					IValue key1 = o1.getMember(context, name, false);
					IValue key2 = o2.getMember(context, name, false);
					return cmpValues.apply(key1, key2);
				} catch(Throwable t) {
					throw new RuntimeException(t);
				}
			}
		};
	}


	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IType type = source.check(context);
		IType itemType = ((ContainerType)type).getItemType();
		if(itemType instanceof CategoryType) 
			return compileSortCategory(context, method, flags, (CategoryType)itemType);
		else
			return compileSortNative(context, method, flags);
	}

	private ResultInfo compileSortNative(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = source.compile(context, method, flags);
		method.addInstruction(descending ? Opcode.ICONST_1 : Opcode.ICONST_0);
		MethodConstant m = new MethodConstant(info.getType(), "sort", boolean.class, PromptoList.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		return new ResultInfo(PromptoList.class);
	}

	private ResultInfo compileSortCategory(Context context, MethodInfo method, Flags flags, CategoryType itemType) {
		ResultInfo srcinfo = source.compile(context, method, flags);
		compileCategoryComparator(context, method, flags, itemType);
		MethodConstant m = new MethodConstant(srcinfo.getType(), "sortUsing", Comparator.class, PromptoList.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		return new ResultInfo(PromptoList.class);
	}

	private ResultInfo compileCategoryComparator(Context context, MethodInfo method, Flags flags, CategoryType itemType) {
		if(key==null)
			key = new UnresolvedIdentifier(new Identifier("key"));
		IDeclaration decl = itemType.getDeclaration(context);
		if(decl instanceof CategoryDeclaration) {
			Type cmpType = compileCategoryComparatorClass(context, method.getClassFile(), itemType, (CategoryDeclaration)decl);
			return CompilerUtils.compileNewInstance(method, cmpType);
		} else
			throw new UnsupportedOperationException(); // TODO
	}

	static interface CategoryComparatorCompiler {
		Type compile(Context context, ClassFile parentClass, CategoryType itemType);
	}

	private Type compileCategoryComparatorClass(Context context, ClassFile parentClass, CategoryType itemType, CategoryDeclaration decl) {
		CategoryComparatorCompiler compiler = buildCategoryComparatorCompiler(context, itemType, decl);
		return compiler.compile(context, parentClass, itemType);
	}

	
	private CategoryComparatorCompiler buildCategoryComparatorCompiler(Context context, CategoryType itemType, CategoryDeclaration decl) {
		Identifier keyAsId = new Identifier(key.toString());
		if(decl.hasAttribute(context, keyAsId))
			return new CategoryAttributeComparatorCompiler();
		else if(decl.hasMethod(context, keyAsId))
			return new CategoryMethodComparatorCompiler();
		else {
			MethodCall call = createGlobalMethodCall(context, itemType, keyAsId);
			if(call!=null)
				return new CategoryGlobalMethodComparatorCompiler(call);
			else
				return new CategoryExpressionComparatorCompiler();
		}
	}
	
	abstract class CategoryComparatorCompilerBase implements CategoryComparatorCompiler {
		
		CategoryType itemType;
		
		@Override
		public Type compile(Context context, ClassFile parentClass, CategoryType itemType) {
			this.itemType = itemType;
			int innerClassIndex = 1 + parentClass.getInnerClasses().size();
			String innerClassName = parentClass.getThisClass().getType().getTypeName() + '$' + innerClassIndex;
			Type innerClassType = new PromptoType(innerClassName); 
			ClassFile classFile = new ClassFile(innerClassType);
			classFile.setSuperClass(new ClassConstant(Object.class));
			classFile.addInterface(new ClassConstant(Comparator.class));
			CompilerUtils.compileEmptyConstructor(classFile);
			compileBridge(context, classFile, itemType.getJavaType(context));
			compileMethod(context, classFile, itemType.getJavaType(context));
			parentClass.addInnerClass(classFile);
			return innerClassType;
		}

		private void compileMethod(Context context, ClassFile classFile, Type paramType) {
			Descriptor.Method proto = new Descriptor.Method(paramType, paramType, int.class);
			MethodInfo method = classFile.newMethod("compare", proto);
			// use a dummy '$this', since we never use it, and we need 'this' for compiling expressions
			method.registerLocal("$this", VerifierType.ITEM_Object, classFile.getThisClass());
			method.registerLocal("o1", VerifierType.ITEM_Object, new ClassConstant(paramType));
			method.registerLocal("o2", VerifierType.ITEM_Object, new ClassConstant(paramType));
			compileMethodBody(context, method, paramType);
		
		}
		
		protected abstract void compileMethodBody(Context context, MethodInfo method, Type paramType);

		private void compileBridge(Context context, ClassFile classFile, Type paramType) {
			// create a bridge "compare" method to convert Object -> paramType
			Descriptor.Method proto = new Descriptor.Method(Object.class, Object.class, int.class);
			MethodInfo method = classFile.newMethod("compare", proto);
			method.addModifier(Tags.ACC_BRIDGE | Tags.ACC_SYNTHETIC);
			method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
			method.registerLocal("o1", VerifierType.ITEM_Object, new ClassConstant(Object.class));
			method.registerLocal("o2", VerifierType.ITEM_Object, new ClassConstant(Object.class));
			method.addInstruction(Opcode.ALOAD_0, classFile.getThisClass());
			if(descending) {
				method.addInstruction(Opcode.ALOAD_2, new ClassConstant(Object.class));
				method.addInstruction(Opcode.CHECKCAST, new ClassConstant(paramType));
				method.addInstruction(Opcode.ALOAD_1, new ClassConstant(Object.class));
				method.addInstruction(Opcode.CHECKCAST, new ClassConstant(paramType));
			} else {
				method.addInstruction(Opcode.ALOAD_1, new ClassConstant(Object.class));
				method.addInstruction(Opcode.CHECKCAST, new ClassConstant(paramType));
				method.addInstruction(Opcode.ALOAD_2, new ClassConstant(Object.class));
				method.addInstruction(Opcode.CHECKCAST, new ClassConstant(paramType));
			}
			proto = new Descriptor.Method(paramType, paramType, int.class);
			MethodConstant c = new MethodConstant(classFile.getThisClass(), "compare", proto);
			method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			method.addInstruction(Opcode.IRETURN);
		}
	}
	
	class CategoryAttributeComparatorCompiler extends CategoryComparatorCompilerBase {

		@Override
		protected void compileMethodBody(Context context, MethodInfo method, Type paramType) {
			method.addInstruction(Opcode.ALOAD_1, new ClassConstant(paramType));
			Type fieldType = context.findAttribute(key.toString()).getType().getJavaType(context);
			String getterName = CompilerUtils.getterName(key.toString());
			InterfaceConstant getter = new InterfaceConstant(paramType, getterName, fieldType);
			method.addInstruction(Opcode.INVOKEINTERFACE, getter);
			method.addInstruction(Opcode.ALOAD_2, new ClassConstant(paramType));
			method.addInstruction(Opcode.INVOKEINTERFACE, getter);
			Descriptor.Method proto = new Descriptor.Method(fieldType, int.class);
			MethodConstant c = new MethodConstant(new ClassConstant(fieldType), "compareTo", proto);
			method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			method.addInstruction(Opcode.IRETURN);
		}

	}

	class CategoryExpressionComparatorCompiler extends CategoryComparatorCompilerBase {
		
		@Override
		protected void compileMethodBody(Context context, MethodInfo method, Type paramType) {
			StackLocal tmpThis = method.registerLocal("this", VerifierType.ITEM_Object, new ClassConstant(paramType));
			ResultInfo left = compileValue(context, method, paramType, tmpThis, "o1");
			ResultInfo right = compileValue(context, method, paramType, tmpThis, "o2");
			Descriptor.Method proto = new Descriptor.Method(right.getType(), int.class);
			if(left.isInterface()) {
				InterfaceConstant c = new InterfaceConstant(new ClassConstant(left.getType()), "compareTo", proto);
				method.addInstruction(Opcode.INVOKEINTERFACE, c);
			} else {
				MethodConstant c = new MethodConstant(new ClassConstant(left.getType()), "compareTo", proto);
				method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			}
			method.addInstruction(Opcode.IRETURN);
		}

		private ResultInfo compileValue(Context context, MethodInfo method, Type paramType, StackLocal tmpThis, String paramName) {
			StackLocal param = method.getRegisteredLocal(paramName);
			Opcode opcode = Opcode.values()[Opcode.ALOAD_0.ordinal() + param.getIndex()];
			method.addInstruction(opcode, new ClassConstant(paramType));
			method.addInstruction(Opcode.ASTORE, new ByteOperand((byte)tmpThis.getIndex()));
			return key.compile(context.newInstanceContext(itemType, false), method, new Flags());
		}
	}

	class CategoryMethodComparatorCompiler extends CategoryComparatorCompilerBase {
		@Override
		protected void compileMethodBody(Context context, MethodInfo method, Type paramType) {
			throw new UnsupportedOperationException();
		}
	}
	
	class CategoryGlobalMethodComparatorCompiler extends CategoryComparatorCompilerBase {
		
		MethodCall call;
		
		public CategoryGlobalMethodComparatorCompiler(MethodCall call) {
			this.call = call;
		}

		@Override
		protected void compileMethodBody(Context context, MethodInfo method, Type paramType) {
			ResultInfo left = compileValue(context, method, paramType, "o1");
			ResultInfo right = compileValue(context, method, paramType, "o2");
			Descriptor.Method proto = new Descriptor.Method(right.getType(), int.class);
			if(left.isInterface()) {
				InterfaceConstant c = new InterfaceConstant(new ClassConstant(left.getType()), "compareTo", proto);
				method.addInstruction(Opcode.INVOKEINTERFACE, c);
			} else {
				MethodConstant c = new MethodConstant(new ClassConstant(left.getType()), "compareTo", proto);
				method.addInstruction(Opcode.INVOKEVIRTUAL, c);
			}
			method.addInstruction(Opcode.IRETURN);
		}

		private ResultInfo compileValue(Context context, MethodInfo method, Type paramType, String paramName) {
			context.registerValue(new Variable(new Identifier(paramName), itemType));
			ArgumentAssignment assignment = call.getAssignments().getFirst();
			assignment.setExpression(new UnresolvedIdentifier(new Identifier(paramName)));
			return call.compile(context, method, new Flags());
		}
	}

	@Override
	public void declare(Transpiler transpiler) {
	    transpiler.require("List");
	    this.source.declare(transpiler);
	    IType type = this.source.check(transpiler.getContext());
	    IType itemType = ((ContainerType)type).getItemType();
	    itemType.declareSorted(transpiler, this.key);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    IType type = this.source.check(transpiler.getContext());
	    this.source.transpile(transpiler);
	    transpiler.append(".sorted(");
	    IType itemType = ((ContainerType)type).getItemType();
	    itemType.transpileSorted(transpiler, this.descending, this.key);
	    transpiler.append(")");
		return false;
	}

}
