package prompto.declaration;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import prompto.compiler.ClassConstant;
import prompto.compiler.ClassFile;
import prompto.compiler.CompilerException;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Descriptor;
import prompto.compiler.FieldConstant;
import prompto.compiler.FieldInfo;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.StackLocal;
import prompto.constraint.IAttributeConstraint;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.store.AttributeInfo;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.type.NativeType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.value.IValue;

public class AttributeDeclaration extends BaseDeclaration {
	
	IType type;
	IAttributeConstraint constraint;
	IdentifierList indexTypes;
	boolean storable = false;
	
	public AttributeDeclaration(Identifier id, IType type) {
		this(id, type, null, null);
	}

	public AttributeDeclaration(Identifier id, IType type, IAttributeConstraint constraint) {
		this(id, type, constraint, null);
	}
	
	public AttributeDeclaration(Identifier id, IType type, IdentifierList indexTypes) {
		this(id, type, null, indexTypes);
	}
	
	public AttributeDeclaration(Identifier id, IType type, IAttributeConstraint constraint, IdentifierList indexTypes) {
		super(id);
		this.type = type;
		this.constraint = constraint;
		this.indexTypes = indexTypes;
	}

	@Override
	public DeclarationType getDeclarationType() {
		return DeclarationType.ATTRIBUTE;
	}
	
	@Override
	public String toString() {
		return this.type.toString() + " " + this.getName();
	}
	
	public AttributeInfo getAttributeInfo(Context context) {
		return getAttributeInfo(id->context.getRegisteredDeclaration(IDeclaration.class, id));
	}
	
	
	public AttributeInfo getAttributeInfo(Function<Identifier, IDeclaration> locator) {
		List<String> list = indexTypes==null ?  null : 
					indexTypes.stream()
						.map((id)->id.toString())
						.collect(Collectors.toList());
		boolean collection = type instanceof ContainerType;
		Family family = getFamily(locator, collection);
		return new AttributeInfo(getName(), family, collection, list);
	}
	
	private Family getFamily(Function<Identifier, IDeclaration> locator, boolean collection) {
		IType familyType = collection ? ((ContainerType)type).getItemType() : type;
		if(familyType instanceof NativeType)
			return familyType.getFamily();
		Identifier typeName = familyType.getTypeNameId();
		IDeclaration decl = locator.apply(typeName);
		return decl.getType(null).getFamily();
	}

	public IType getType() {
		return type;
	}
	
	public IAttributeConstraint getConstraint() {
		return constraint;
	}
	
	public void setStorable(boolean storable) {
		this.storable = storable;
	}
	
	@Override
	public boolean isStorable() {
		return storable;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			writer.append("define ");
			writer.append(getId());
			writer.append(" as ");
			if(storable)
				writer.append("storable ");
			type.toDialect(writer);
			writer.append(" attribute");
			if(constraint!=null)
				constraint.toDialect(writer);
			if(indexTypes!=null) {
				writer.append(" with ");
				indexTypes.toDialect(writer, true);
				writer.append(" index");
			}
			break;
		case O:
			if(storable)
				writer.append("storable ");
			writer.append("attribute ");
			writer.append(getId());
			writer.append(" : ");
			type.toDialect(writer);
			if(constraint!=null)
				constraint.toDialect(writer);
			if(indexTypes!=null) {
				writer.append(" with index");
				if(!indexTypes.isEmpty()) {
					writer.append(" (");
					indexTypes.toDialect(writer, false);
					writer.append(')');
				}
			}
			writer.append(';');
			break;
		case M:
			if(storable)
				writer.append("storable ");
			writer.append("attr ");
			writer.append(getId());
			writer.append(" (");
			type.toDialect(writer);
			writer.append("):\n");
			writer.indent();
			if(constraint!=null)
				constraint.toDialect(writer);
			if(indexTypes!=null) {
				if(constraint!=null)
					writer.newLine();
				writer.append("index (");
				indexTypes.toDialect(writer, false);
				writer.append(')');
			}
			if(constraint==null && indexTypes==null)
				writer.append("pass");
			writer.dedent();
			break;
		}
	}
	
	@Override
	public void register(Context context) {
		context.registerDeclaration(this);
	}
	
	@Override
	public IType check(Context context, boolean isStart) {
		type.checkExists(context);
		return type;
	}
	
	@Override
	public IType getType(Context context) {
		return type;
	}

	public IValue checkValue(Context context, IExpression expression) throws PromptoError {
		IValue value = expression.interpret(context);
		if(constraint==null)
			return value;
		constraint.checkValue(context, value);
		return value;
	}

	public FieldInfo toFieldInfo(Context context) {
		return new FieldInfo(getName(), type.getJavaType(context));
	}

	public ClassFile compile(Context context, String fullName) {
		java.lang.reflect.Type type = CompilerUtils.attributeInterfaceTypeFrom(fullName);
		ClassFile classFile = new ClassFile(type);
		classFile.addModifier(Modifier.ABSTRACT | Modifier.INTERFACE);
		FieldInfo field = this.toFieldInfo(context);
		compileSetterPrototype(context, classFile, field);
		compileGetterPrototype(context, classFile, field);
		compileDefaultChecker(context, classFile, field);
		ClassFile concrete = compileConcreteClass(context, classFile, fullName);
		classFile.addInnerClass(concrete);
		return classFile;
	}

	private void compileDefaultChecker(Context context, ClassFile classFile, FieldInfo field) {
		if(constraint!=null) {
			String checkerName = CompilerUtils.checkerName(field.getName().getValue());
			Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
			MethodInfo method = classFile.newMethod(checkerName, proto);
			method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
			method.registerLocal("value", VerifierType.ITEM_Object, new ClassConstant(field.getType()));
			constraint.compile(context, method, new Flags());
			method.addInstruction(Opcode.RETURN);
		}
	}

	protected ClassFile compileConcreteClass(Context context, ClassFile outerClass, String fullName) {
		try {
			java.lang.reflect.Type concreteType = CompilerUtils.attributeConcreteTypeFrom(fullName);
			ClassFile classFile = new ClassFile(concreteType);
			classFile.setSuperClass(new ClassConstant(Object.class));
			classFile.addInterface(outerClass.getThisClass());
			FieldInfo field = this.toFieldInfo(context);
			classFile.addField(field);
			compileGetter(context, classFile, field);
			compileSetter(context, classFile, field);
			compileCopyConstructor(context, classFile, field);
			return classFile;
		} catch(SyntaxError e) {
			throw new CompilerException(e);
		}
	}

	private void compileCopyConstructor(Context context, ClassFile classFile, FieldInfo field) {
		Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
		MethodInfo method = classFile.newMethod("<init>", proto);
		// call super()
		StackLocal local = method.registerLocal("this", VerifierType.ITEM_UninitializedThis, classFile.getThisClass());
		CompilerUtils.compileALOAD(method, local);
		MethodConstant m = new MethodConstant(classFile.getSuperClass(), "<init>", void.class);
		method.addInstruction(Opcode.INVOKESPECIAL, m);
		// call setter
		CompilerUtils.compileALOAD(method, local);
		StackLocal value = method.registerLocal("%value%", VerifierType.ITEM_Object, new ClassConstant(field.getType()));
		CompilerUtils.compileALOAD(method, value);
		String setterName = CompilerUtils.setterName(field.getName().getValue());
		m = new MethodConstant(classFile.getThisClass(), setterName, field.getType(), void.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		// done
		method.addInstruction(Opcode.RETURN);
	}

	private void compileSetter(Context context, ClassFile classFile, FieldInfo field) {
		String setterName = CompilerUtils.setterName(field.getName().getValue());
		Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
		MethodInfo method = classFile.newMethod(setterName, proto);
		StackLocal local = method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		StackLocal value = method.registerLocal("%value%", VerifierType.ITEM_Object, new ClassConstant(field.getType()));
		if(constraint!=null) {
			CompilerUtils.compileALOAD(method, local);
			CompilerUtils.compileALOAD(method, value);
			String checkerName = CompilerUtils.checkerName(field.getName().getValue());
			MethodConstant m = new MethodConstant(classFile.getThisClass(), checkerName, field.getType(), void.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		}
		CompilerUtils.compileALOAD(method, local);
		CompilerUtils.compileALOAD(method, value);
		FieldConstant fc = new FieldConstant(classFile.getThisClass(), field.getName().getValue(), field.getType());
		method.addInstruction(Opcode.PUTFIELD, fc);
		method.addInstruction(Opcode.RETURN);
	}

	private void compileGetter(Context context, ClassFile classFile, FieldInfo field) {
		String getterName = CompilerUtils.getterName(field.getName().getValue());
		Descriptor.Method proto = new Descriptor.Method(field.getType());
		MethodInfo method = classFile.newMethod(getterName, proto);
		StackLocal local = method.registerLocal("this", VerifierType.ITEM_Object, classFile.getThisClass());
		CompilerUtils.compileALOAD(method, local);
		FieldConstant fc = new FieldConstant(classFile.getThisClass(), field.getName().getValue(), field.getType());
		method.addInstruction(Opcode.GETFIELD, fc);
		method.addInstruction(Opcode.ARETURN);
	}

	private void compileGetterPrototype(Context context, ClassFile classFile, FieldInfo field) {
		String name = CompilerUtils.getterName(field.getName().getValue());
		Descriptor.Method proto = new Descriptor.Method(field.getType());
		MethodInfo method = classFile.newMethod(name, proto);
		method.addModifier(Modifier.ABSTRACT);
	}

	private void compileSetterPrototype(Context context, ClassFile classFile, FieldInfo field) {
		String name = CompilerUtils.setterName(field.getName().getValue());
		Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
		MethodInfo method = classFile.newMethod(name, proto);
		method.addModifier(Modifier.ABSTRACT);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.type.declare(transpiler);
	    if(this.constraint!=null)
	        this.constraint.declare(transpiler, this.getName(), this.type);
	}

}
