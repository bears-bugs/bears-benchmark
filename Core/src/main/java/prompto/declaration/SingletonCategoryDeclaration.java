package prompto.declaration;

import java.lang.reflect.Modifier;

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
import prompto.compiler.ResultInfo;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.intrinsic.PromptoRoot;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;

public class SingletonCategoryDeclaration extends ConcreteCategoryDeclaration {

	public SingletonCategoryDeclaration(Identifier name, IdentifierList attributes, MethodDeclarationList methods) {
		super(name, attributes, null, methods);
	}
	
	@Override
	protected void categoryTypeToEDialect(CodeWriter writer) {
		writer.append("singleton");
	}

	@Override
	protected void categoryTypeToODialect(CodeWriter writer) {
		writer.append("singleton");
	}

	@Override
	protected void categoryTypeToMDialect(CodeWriter writer) {
		writer.append("singleton");
	}
	
	@Override
	public ClassFile compile(Context context, String fullName) {
		try {
			java.lang.reflect.Type concreteType = CompilerUtils.singletonTypeFrom(fullName);
			ClassFile classFile = new ClassFile(concreteType);
			classFile.addModifier(Modifier.ABSTRACT);
			classFile.setSuperClass(new ClassConstant(PromptoRoot.class));
			compileFields(context, classFile, new Flags());
			compileEmptyConstructor(context, classFile, new Flags());
			compileMethods(context, classFile, new Flags());
			return classFile;
		} catch(SyntaxError e) {
			throw new CompilerException(e);
		}
	}
	
	@Override
	protected void compileField(Context context, ClassFile classFile, Flags flags, Identifier id) {
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, id);
		FieldInfo field = decl.toFieldInfo(context);
		field.addModifier(Modifier.STATIC);
		classFile.addField(field);
		compileStaticSetterMethod(context, classFile, flags, id, field);
		compileStaticGetterMethod(context, classFile, flags, id, field);
	}
	
	private void compileStaticGetterMethod(Context context, ClassFile classFile, Flags flags,Identifier id, FieldInfo field) {
		GetterMethodDeclaration getter = findGetter(context, id);
		if(getter!=null)
			getter.compile(context, classFile, flags, getType(context), field);
		else
			compileStaticFieldGetter(context, classFile, flags, id, field);
	}

	private void compileStaticFieldGetter(Context context, ClassFile classFile, Flags flags, Identifier id, FieldInfo field) {
		String name = CompilerUtils.getterName(id.toString());
		Descriptor.Method proto = new Descriptor.Method(field.getType());
		MethodInfo method = classFile.newMethod(name, proto);
		method.addModifier(Modifier.STATIC);
		FieldConstant f = new FieldConstant(classFile.getThisClass(), id.toString(), field.getType());
		method.addInstruction(Opcode.GETSTATIC, f);
		method.addInstruction(Opcode.ARETURN, new ClassConstant(field.getType()));
	}

	private void compileStaticSetterMethod(Context context, ClassFile classFile, Flags flags, 
			Identifier id, FieldInfo field) {
		SetterMethodDeclaration setter = findSetter(context, id);
		if(setter!=null)
			setter.compile(context, classFile, flags, getType(context), field);
		else
			compileStaticFieldSetter(context, classFile, flags, id, field);
	}
	
	private void compileStaticFieldSetter(Context context, ClassFile classFile, Flags flags, 
			Identifier id, FieldInfo field) {
		String name = CompilerUtils.setterName(field.getName().getValue());
		Descriptor.Method proto = new Descriptor.Method(field.getType(), void.class);
		MethodInfo method = classFile.newMethod(name, proto);
		method.addModifier(Modifier.STATIC);
		ClassConstant fc = new ClassConstant(field.getType());
		method.registerLocal("%value%", VerifierType.ITEM_Object, fc);
		// store data in field
		method.addInstruction(Opcode.ALOAD_0, fc);
		FieldConstant f = new FieldConstant(classFile.getThisClass(), field.getName().getValue(), field.getType());
		method.addInstruction(Opcode.PUTSTATIC, f);
		// done
		method.addInstruction(Opcode.RETURN);
	}

	public ResultInfo compileGetMember(Context context, MethodInfo method, Flags flags, IExpression parent, Identifier id) {
		java.lang.reflect.Type concreteType = CompilerUtils.getCategorySingletonType(getId());
		String getterName = CompilerUtils.getterName(id.toString());
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, id);
		FieldInfo field = decl.toFieldInfo(context);
		MethodConstant m = new MethodConstant(concreteType, getterName, field.getType());
		method.addInstruction(Opcode.INVOKESTATIC, m);
		return new ResultInfo(field.getType());
	}

	public ResultInfo compileSetStaticMember(Context context, MethodInfo method, Flags flags, IExpression value, Identifier id) {
		value.compile(context, method, flags);
		java.lang.reflect.Type concreteType = CompilerUtils.getCategorySingletonType(getId());
		String setterName = CompilerUtils.setterName(id.toString());
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, id);
		FieldInfo field = decl.toFieldInfo(context);
		MethodConstant m = new MethodConstant(concreteType, setterName, field.getType(), void.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		return new ResultInfo(void.class);
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("function ").append(this.getName()).append("() {").indent();
	    transpiler.append("$Root.call(this);").newLine();
	    transpiler.append("this.mutable = true;").newLine();
	    transpiler.append("return this;").dedent();
	    transpiler.append("};").newLine();
	    transpiler.append(this.getName()).append(".prototype = Object.create($Root.prototype);").newLine();
	    transpiler.append(this.getName()).append(".prototype.constructor = ").append(this.getName()).append(";").newLine();
	    transpiler.append(this.getName()).append(".instance = new ").append(this.getName()).append("();").newLine();
	    if(this.attributes!=null) {
	        this.attributes.forEach(attr -> transpiler.append(this.getName()).append(".instance.").append(attr.toString()).append(" = null;").newLine());
	    }
	    CategoryType type = new CategoryType(this.getId());
	    this.methods.forEach(method -> {
	    	Transpiler m = transpiler.newMemberTranspiler(type);
	        method.transpile(m);
	        m.flush();
	    });
		return true;
	}

}
