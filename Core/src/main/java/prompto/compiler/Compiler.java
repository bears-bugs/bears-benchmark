package prompto.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import prompto.argument.IArgument;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.declaration.AbstractMethodDeclaration;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.EnumeratedCategoryDeclaration;
import prompto.declaration.EnumeratedNativeDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoCallSite;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.type.IType;
import prompto.utils.IOUtils;
import prompto.verifier.ClassVerifier;

public class Compiler {

	File classDir;
	
	Compiler(File classDir) throws Exception {
		this.classDir = classDir;
		String clean = System.getProperty("prompto.compiler.clean"); // for testing
		if("true".equals(clean))
			IOUtils.deleteFilesRecursively(classDir, false);
	}

	public void compileClass(Context context, String fullName) throws ClassNotFoundException {
		ClassFile classFile = createClassFile(context, fullName);
		writeClassFile(classFile);
	}
	
	private ClassFile createClassFile(Context context, String fullName) throws ClassNotFoundException {
		if(fullName.startsWith(CompilerUtils.ATTRIBUTE_PACKAGE_PREFIX)) 
			return createAttributeClassFile(context, fullName);
		else if(fullName.startsWith(CompilerUtils.GLOBAL_METHOD_PACKAGE_PREFIX)) 
			return createGlobalMethodsClassFile(context, fullName);
		else if(fullName.startsWith(CompilerUtils.CATEGORY_PACKAGE_PREFIX))
			return createCategoryClassFile(context, fullName);
		else if(fullName.startsWith(CompilerUtils.CATEGORY_ENUM_PACKAGE_PREFIX))
			return createEnumeratedCategoryClassFile(context, fullName);
		else if(fullName.startsWith(CompilerUtils.NATIVE_ENUM_PACKAGE_PREFIX))
			return createEnumeratedNativeClassFile(context, fullName);
		else if(fullName.startsWith(CompilerUtils.TEST_METHOD_PACKAGE_PREFIX))
			return createTestClassFile(context, fullName);
		else
			throw new ClassNotFoundException(fullName);
	}

	private ClassFile createAttributeClassFile(Context context, String fullName) throws ClassNotFoundException {
		String simpleName = CompilerUtils.attributeSimpleNameFrom(fullName);
		AttributeDeclaration decl = context.getRegisteredDeclaration(AttributeDeclaration.class, new Identifier(simpleName));
		if(decl==null)
			throw new ClassNotFoundException(simpleName);
		else 
			return decl.compile(context, fullName);
	}

	private ClassFile createTestClassFile(Context context, String fullName) throws ClassNotFoundException {
		String simpleName = CompilerUtils.testSimpleNameFrom(fullName);
		TestMethodDeclaration decl = context.getTest(new Identifier(simpleName), true);
		if(decl==null)
			throw new ClassNotFoundException(simpleName);
		else 
			return decl.compile(context, fullName);
	}
	
	private ClassFile createEnumeratedNativeClassFile(Context context, String fullName) throws ClassNotFoundException {
		String simpleName = CompilerUtils.nativeEnumSimpleNameFrom(fullName);
		EnumeratedNativeDeclaration decl = 
				context.getRegisteredDeclaration(EnumeratedNativeDeclaration.class, new Identifier(simpleName));
		if(decl==null)
			throw new ClassNotFoundException(simpleName);
		else 
			return decl.compile(context, fullName);
	}

	private ClassFile createEnumeratedCategoryClassFile(Context context, String fullName) throws ClassNotFoundException {
		String simpleName = CompilerUtils.categoryEnumSimpleNameFrom(fullName);
		EnumeratedCategoryDeclaration decl = 
				context.getRegisteredDeclaration(EnumeratedCategoryDeclaration.class, new Identifier(simpleName));
		if(decl==null)
			throw new ClassNotFoundException(simpleName);
		else 
			return decl.compile(context, fullName);
	}

	private ClassFile createCategoryClassFile(Context context, String fullName) throws ClassNotFoundException {
		String simpleName = CompilerUtils.categorySimpleNameFrom(fullName);
		if(simpleName.indexOf('%')>=0)
			return createExtendedClassFile(context, simpleName, fullName);
		else
			return createRegularClassFile(context, simpleName, fullName);
	}
	
	
	private ClassFile createExtendedClassFile(Context context, String simpleName, String fullName) throws ClassNotFoundException {
		ClassFile classFile = new ClassFile(new PromptoType(fullName));
		classFile.addModifier(Modifier.ABSTRACT | Modifier.INTERFACE);
		String[] interfaces = simpleName.split("%");
		for(String s : interfaces) {
			if("any".equals(s))
				continue;
			IDeclaration decl = context.getRegisteredDeclaration(IDeclaration.class, new Identifier(s));
			if(decl instanceof CategoryDeclaration)
				classFile.addInterface(new ClassConstant(CompilerUtils.getCategoryInterfaceType(s)));
			else if(decl instanceof AttributeDeclaration)
				classFile.addInterface(new ClassConstant(CompilerUtils.getAttributeInterfaceType(s)));
			else
				throw new UnsupportedOperationException();
		}
		return classFile;
	}
	
	private ClassFile createRegularClassFile(Context context, String simpleName, String fullName) throws ClassNotFoundException {
		CategoryDeclaration decl = context.getRegisteredDeclaration(CategoryDeclaration.class, new Identifier(simpleName));
		if(decl==null)
			throw new ClassNotFoundException(simpleName);
		else
			return decl.compile(context, fullName);
	}

	private ClassFile createGlobalMethodsClassFile(Context context, String fullName) throws ClassNotFoundException {
		Type abstractType = CompilerUtils.abstractTypeFrom(fullName);
		String simpleName = fullName.substring(fullName.indexOf(".µ.") + 3);
		int idx = simpleName.indexOf('$');
		if(idx>0)
			simpleName = simpleName.substring(0, idx);
		MethodDeclarationMap methods = context.getRegisteredDeclaration(MethodDeclarationMap.class, new Identifier(simpleName), true);
		if(methods==null)
			throw new ClassNotFoundException(simpleName);
		else
			return createGlobalMethodsClassFile(context, methods, abstractType);
	}

	private ClassFile createGlobalMethodsClassFile(Context context, MethodDeclarationMap methods, Type type) {
		Collection<IMethodDeclaration> decls = methods.globalConcreteMethods();
		if(decls.isEmpty())
			return createGlobalMethodsInterfaceFile(context, methods, type);
		else {
			ClassFile classFile = new ClassFile(type);
			classFile.addModifier(Modifier.ABSTRACT);
			decls.forEach((m) -> 
				m.compile(context, true, classFile));
			if(decls.size()>1) {
				createGlobalMethodHandles(classFile);
				populateGlobalMethodHandles(context, classFile, decls);
				createGlobalCheckParamsMethods(context, classFile, decls);
				createGlobalBootstrapMethod(classFile);
			}
			return classFile;
		}
	}


	private ClassFile createGlobalMethodsInterfaceFile(Context context, MethodDeclarationMap methods, Type type) {
		IMethodDeclaration method = methods.values().iterator().next();
		if(method instanceof AbstractMethodDeclaration)
			return ((AbstractMethodDeclaration)method).compileInterface(context, type);
		else
			throw new UnsupportedOperationException();
	}

	private void createGlobalMethodHandles(ClassFile classFile) {
		FieldInfo field = new FieldInfo("methodHandles", MethodHandle[].class);
		field.addModifier(Modifier.STATIC);
		classFile.addField(field);
	}

	private void populateGlobalMethodHandles(Context context, ClassFile classFile, Collection<IMethodDeclaration> decls) {
		Descriptor.Method proto = new Descriptor.Method(void.class);
		MethodInfo method = classFile.newMethod("<clinit>", proto);
		method.addModifier(Modifier.STATIC);
		method.addInstruction(Opcode.LDC, new IntConstant(decls.size()));
		method.addInstruction(Opcode.ANEWARRAY, new ClassConstant(MethodHandle.class));
		// initialize counter
		method.registerLocal("i", VerifierType.ITEM_Integer, null);
		method.addInstruction(Opcode.ICONST_0); 
		method.addInstruction(Opcode.ISTORE_0);
		for(IMethodDeclaration decl : decls) {
			method.addInstruction(Opcode.DUP); // the array
			method.addInstruction(Opcode.ILOAD_0); // the index
			createGlobalMethodHandle(context,true,  method, decl); // the value
			method.addInstruction(Opcode.AASTORE);
			method.addInstruction(Opcode.IINC, new ByteOperand((byte)0), new ByteOperand((byte)1)); 
		}
		FieldConstant fc = new FieldConstant(classFile.getThisClass(), "methodHandles", MethodHandle[].class);
		method.addInstruction(Opcode.PUTSTATIC, fc);
		method.addInstruction(Opcode.RETURN);
	}
	
	private void createGlobalMethodHandle(Context context, boolean isStart, MethodInfo method, IMethodDeclaration decl) {
		// MethodHandles.lookup().findStatic(klass, "print",  MethodType.methodType(void.class, k1))
		MethodConstant mc = new MethodConstant(MethodHandles.class, "lookup", Lookup.class);
		method.addInstruction(Opcode.INVOKESTATIC, mc);
		// klass
		method.addInstruction(Opcode.LDC, method.getClassFile().getThisClass());
		// name
		method.addInstruction(Opcode.LDC, new StringConstant(decl.getName()));
		// descriptor
		IType returnType = decl.check(context, isStart);
		String descriptor = CompilerUtils.createMethodDescriptor(context, decl.getArguments(), returnType).toString();
		method.addInstruction(Opcode.LDC, new StringConstant(descriptor));
		// loader
		mc = new MethodConstant(PromptoClassLoader.class, "getInstance", PromptoClassLoader.class);
		method.addInstruction(Opcode.INVOKESTATIC, mc);
		// type
		mc = new MethodConstant(MethodType.class, "fromMethodDescriptorString", 
				String.class, ClassLoader.class, MethodType.class);
		method.addInstruction(Opcode.INVOKESTATIC, mc);
		// handle
		mc = new MethodConstant(Lookup.class, "findStatic", 
				Class.class, String.class, MethodType.class, MethodHandle.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, mc);
	}

	private void createGlobalBootstrapMethod(ClassFile classFile) {
		Descriptor.Method proto = new Descriptor.Method(Lookup.class, String.class, MethodType.class, CallSite.class);
		MethodInfo method = classFile.newMethod("bootstrap", proto);
		method.addModifier(Modifier.STATIC);
		StackLocal lookup = method.registerLocal("lookup", VerifierType.ITEM_Object, new ClassConstant(Lookup.class));
		method.registerLocal("name", VerifierType.ITEM_Object, new ClassConstant(String.class));
		StackLocal type = method.registerLocal("type", VerifierType.ITEM_Object, new ClassConstant(MethodType.class));
		// return PromptoCallSite.bootstrap(lookup, TestCallSite.class, methods, type);
		CompilerUtils.compileALOAD(method, lookup);
		method.addInstruction(Opcode.LDC, classFile.getThisClass());
		FieldConstant fc = new FieldConstant(classFile.getThisClass(), "methodHandles", MethodHandle[].class);
		method.addInstruction(Opcode.GETSTATIC, fc);
		CompilerUtils.compileALOAD(method, type);
		MethodConstant mc = new MethodConstant(PromptoCallSite.class, "bootstrap", 
				Lookup.class, Class.class, MethodHandle[].class, MethodType.class, CallSite.class);
		method.addInstruction(Opcode.INVOKESTATIC, mc);
		method.addInstruction(Opcode.ARETURN);
	}

	private void createGlobalCheckParamsMethods(Context context, ClassFile classFile, Collection<IMethodDeclaration> decls) {
		for(IMethodDeclaration decl : decls)
			createGlobalCheckParamsMethod(context, classFile, decl.getArguments());
	}

	private void createGlobalCheckParamsMethod(Context context, ClassFile classFile, ArgumentList arguments) {
		String name = buildGlobalCheckParamsMethodName(context, arguments);
		Type[] types = buildGlobalCheckParamsMethodTypes(arguments.size());
		Descriptor.Method desc = new Descriptor.Method(types, boolean.class);
		MethodInfo method = classFile.newMethod(name, desc);
		method.addModifier(Modifier.STATIC);
		StackState state = method.captureStackState();
		List<IInstructionListener> listeners = new ArrayList<IInstructionListener>();
		for(int i=0;i<arguments.size();i++) {
			method.registerLocal("p" + i, VerifierType.ITEM_Object, new ClassConstant(Object.class));
			method.addInstruction(Opcode.LDC, new ClassConstant(arguments.get(i).getJavaType(context)));
			CompilerUtils.compileALOAD(method, "p" + i);
			MethodConstant mc = new MethodConstant(Class.class, "isInstance", Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, mc);
			OffsetListenerConstant listener = method.addOffsetListener(new OffsetListenerConstant());
			listeners.add(listener);
			method.activateOffsetListener(listener);
			method.addInstruction(Opcode.IFEQ, listener);
		}
		// success
		method.addInstruction(Opcode.ICONST_1);
		method.addInstruction(Opcode.IRETURN);
		// failure
		listeners.forEach((l)->
			method.inhibitOffsetListener(l));
		method.restoreFullStackState(state);
		method.placeLabel(state);
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.IRETURN);
	}

	private Type[] buildGlobalCheckParamsMethodTypes(int size) {
		Type[] types = new Type[size];
		while(size-->0)
			types[size] = Object.class;
		return types;
	}

	private String buildGlobalCheckParamsMethodName(Context context, ArgumentList arguments) {
		StringBuilder sb = new StringBuilder();
		sb.append("checkParams");
		for(IArgument arg : arguments) {
			String typeName = arg.getJavaType(context).getTypeName();
			String name = typeName.substring(typeName.lastIndexOf('.') + 1);
			sb.append(name);
		}
		return sb.toString();
	}

	private void writeClassFile(ClassFile classFile) throws CompilerException {
		try {
			Type type = classFile.getThisClass().getType();
			String fullName = type.getTypeName().replace('.', '/');
			File parent = new File(classDir, fullName.substring(0, fullName.lastIndexOf('/')+1));
			if(!parent.exists() && !parent.mkdirs())
				throw new IOException("Could not create " + parent.getAbsolutePath());
			File file = new File(parent, fullName.substring(fullName.lastIndexOf('/')+1) + ".class");
			if(DumpLevel.current().ordinal()>0)
				System.err.println("Writing class file: " + file.getAbsolutePath());
			try(OutputStream out = new FileOutputStream(file)) {
				classFile.writeTo(out);
			}
			if("true".equals(System.getProperty("prompto-verify-class"))) {
				ClassVerifier verifier = new ClassVerifier(classFile);
				verifier.verify();
			}
			for(ClassFile inner : classFile.getInnerClasses())
				writeClassFile(inner);
		} catch(Exception e) {
			throw new CompilerException(e);
		}
	}

}
