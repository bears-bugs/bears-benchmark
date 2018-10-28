package prompto.compiler;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.SecureClassLoader;

import org.junit.Test;

import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.runtime.utils.Out;

public class TestClassFile {

	static class ByteClassLoader extends SecureClassLoader {
		
		public static Class<?> defineAndResolveClass(String name, byte[] bytes) {
			ByteClassLoader loader = new ByteClassLoader();
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			CodeSource source = createCodeSource();
			Class<?> klass = loader.defineClass(name,  buffer, source);
			loader.resolveClass(klass);
			return klass;
		}

		private static CodeSource createCodeSource() {
			return null;
		}
		
	}
	
	@Test
	public void testTranscode() throws Exception {
		byte[] expected = { (byte)0xCF, (byte)0x80, 0x2F, (byte)0xCF, (byte)0x87, 0x2F, (byte)0xC2, (byte)0xB5, 0x2F, 0x70, 0x72, 0x69, 0x6E, 0x74 };
		byte[] transcoded = Utf8Constant.toModifiedUtf8("π/χ/µ/print");
		assertArrayEquals(expected, transcoded);	
	}
	
	
	@Test
	public void testConstantsPool() {
		ConstantsPool pool = new ConstantsPool();
		assertEquals(1, pool.nextIndex);
		IConstantOperand c = new Utf8Constant("abc");
		c.register(pool);
		assertEquals(2, pool.nextIndex);
		c = new Utf8Constant("abc");
		c.register(pool);
		assertEquals(2, pool.nextIndex);
		c = new LongConstant(123);
		c.register(pool);
		assertEquals(4, pool.nextIndex);
		NameAndTypeConstant ntc = new NameAndTypeConstant("xyw", new Descriptor.Field(new PromptoType("hkp")));
		ntc.register(pool);
		assertEquals(4, ntc.getIndexInConstantPool());
		assertEquals(5, ntc.name.getIndexInConstantPool());
		assertEquals(6, ntc.type.getIndexInConstantPool());
		assertEquals(7, pool.nextIndex);
	}
	
	@Test
	public void testDefineClassForGlobalMethod() throws Exception {
		String name = "π/χ/µ/print";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(String.class, void.class);
		MethodInfo m = c.newMethod("printAbstract", proto);
		m.addModifier(Modifier.ABSTRACT);
		m = c.newMethod("printStatic", proto);
		m.addModifier(Modifier.STATIC);
		m.registerLocal("value", VerifierType.ITEM_Object, new ClassConstant(String.class));
		m.addInstruction(Opcode.RETURN);
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
		assertTrue(Modifier.isAbstract(klass.getModifiers()));
		java.lang.reflect.Method mm = klass.getMethod("printAbstract", String.class);
		assertTrue(Modifier.isAbstract(mm.getModifiers()));
		mm = klass.getMethod("printStatic", String.class);
		assertTrue(Modifier.isStatic(mm.getModifiers()));
		mm.invoke(null, "Hello");
	}
	
	@Test
	public void testCallGlobalMethod() throws Exception {
		Out.init();
		String name = "π/χ/µ/print";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(String.class, void.class);
		MethodInfo m = c.newMethod("print", proto);
		m.addModifier(Modifier.STATIC);
		m.registerLocal("value", VerifierType.ITEM_Object, new ClassConstant(String.class));
		m.addInstruction(Opcode.GETSTATIC, new FieldConstant(System.class, "out", PrintStream.class));
		m.addInstruction(Opcode.ALOAD_0); // the parameter
		m.addInstruction(Opcode.INVOKEVIRTUAL, new MethodConstant(PrintStream.class, "print", String.class, void.class));
		m.addInstruction(Opcode.RETURN);
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
		assertTrue(Modifier.isAbstract(klass.getModifiers()));
		Method mm = klass.getMethod("print", String.class);
		assertTrue(Modifier.isStatic(mm.getModifiers()));
		mm.invoke(null, "Hello");
		String read = Out.read();
		Out.restore();
		assertEquals("Hello", read);
	}
	
	@Test
	public void testClassWithLongConstant() throws Exception {
		String name = "k1";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(Long.class);
		MethodInfo m = c.newMethod("m3", proto);
		m.addModifier(Modifier.STATIC);
		m.addInstruction(Opcode.LDC2_W, new LongConstant(9876543210L));
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
	}
		
	@Test
	public void testClassWithStackLabel_FULL() throws Exception {
		String name = "k1";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(void.class);
		MethodInfo m = c.newMethod("m", proto);
		m.addModifier(Modifier.STATIC);
		m.addInstruction(Opcode.ICONST_1);
		m.addInstruction(Opcode.ICONST_1);
		m.addInstruction(Opcode.IF_ICMPNE, new ShortOperand((short)7));
		StackState branchState = m.captureStackState();
		m.addInstruction(Opcode.ICONST_1);
		m.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		m.restoreFullStackState(branchState);
		m.placeLabel(branchState);
		m.addInstruction(Opcode.ICONST_0);
		StackState lastState = m.captureStackState();
		m.placeLabel(lastState);
		m.addInstruction(Opcode.POP);
		m.addInstruction(Opcode.RETURN);
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
		Method mm = klass.getDeclaredMethod("m");
		mm.invoke(null);
	}
	
	@Test
	public void testInterfaceWithInnerClass() throws Exception {
		File dir = Files.createTempDirectory("prompto_").toFile();
		String root = "Root";
		ClassFile c = new ClassFile(new PromptoType(root));
		c.addModifier(Modifier.INTERFACE | Modifier.ABSTRACT);
		try(OutputStream o = new FileOutputStream(new File(dir, root + ".class"))) {
			c.writeTo(o);
		}
		String derived = "Derived";
		String inner = "%Inner";
		ClassFile i = new ClassFile(new PromptoType(derived + '$' + inner));
		i.addInterface(new PromptoType(derived));
		try(OutputStream o = new FileOutputStream(new File(dir, derived + '$' + inner + ".class"))) {
			i.writeTo(o);
		}
		ClassFile d = new ClassFile(new PromptoType(derived));
		d.addModifier(Modifier.INTERFACE | Modifier.ABSTRACT);
		d.addInterface(new PromptoType(root));
		d.addInnerClass(i);
		try(OutputStream o = new FileOutputStream(new File(dir, derived + ".class"))) {
			d.writeTo(o);
		}
		URLClassLoader loader = URLClassLoader.newInstance(new URL[] { dir.toURI().toURL() }, Thread.currentThread().getContextClassLoader());
		Class<?> klass = loader.loadClass(derived);
		assertNotNull(klass);
		assertEquals(derived, klass.getSimpleName());
		assertTrue(klass.isInterface());
		klass = loader.loadClass(derived + '$' + inner);
		assertNotNull(klass);
		assertEquals(derived + '$' + inner, klass.getSimpleName());
		assertFalse(klass.isInterface());
	}
	
	@Test
	public void testMethodWithException() throws Exception {
		String name = "π/χ/µ/print";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(Object.class, String.class);
		MethodInfo m = c.newMethod("stringValueOf", proto);
		m.addModifier(Modifier.STATIC);
		m.registerLocal("%value%", VerifierType.ITEM_Object, new ClassConstant(Object.class));
		ExceptionHandler handler = m.registerExceptionHandler(NullPointerException.class);
		m.activateOffsetListener(handler);
		m.addInstruction(Opcode.ALOAD_0, new ClassConstant(Object.class)); // the parameter
		m.addInstruction(Opcode.INVOKEVIRTUAL, new MethodConstant(Object.class, "toString", String.class));
		m.addInstruction(Opcode.ARETURN, new ClassConstant(String.class));
		m.inhibitOffsetListener(handler);
		m.placeExceptionHandler(handler);
		StackLocal error = m.registerLocal("%error%", VerifierType.ITEM_Object, new ClassConstant(NullPointerException.class));
		m.addInstruction(Opcode.LDC, new StringConstant("Caught!"));
		m.addInstruction(Opcode.ARETURN, new ClassConstant(String.class));
		m.unregisterLocal(error);
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
		assertTrue(Modifier.isAbstract(klass.getModifiers()));
		Method mm = klass.getMethod("stringValueOf", Object.class);
		assertTrue(Modifier.isStatic(mm.getModifiers()));
		Object s = mm.invoke((Object)null, (Object)null);
		assertEquals("Caught!", s);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testMethodWithThrow() throws Throwable {
		String name = "π/χ/µ/print";
		ClassFile c = new ClassFile(new PromptoType(name));
		c.addModifier(Modifier.ABSTRACT);
		Descriptor.Method proto = new Descriptor.Method(Object.class, String.class);
		MethodInfo m = c.newMethod("stringValueOf", proto);
		m.addModifier(Modifier.STATIC);
		m.registerLocal("%value%", VerifierType.ITEM_Object, new ClassConstant(Object.class));
		CompilerUtils.compileNewInstance(m, UnsupportedOperationException.class);
		m.addInstruction(Opcode.ATHROW, new ClassConstant(UnsupportedOperationException.class));
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		c.writeTo(o);
		byte[] gen = o.toByteArray();
		Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
		assertNotNull(klass);
		assertTrue(Modifier.isAbstract(klass.getModifiers()));
		Method mm = klass.getMethod("stringValueOf", Object.class);
		assertTrue(Modifier.isStatic(mm.getModifiers()));
		try {
			mm.invoke((Object)null, (Object)null);
		} catch(InvocationTargetException e) {
			throw e.getTargetException();
		}
	}
	
	public static void print(String value) {
		System.out.print(value);
	}
	
	public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
		MethodHandle mh = lookup.findStatic(TestClassFile.class, "print", MethodType.methodType(void.class, String.class));
		return new ConstantCallSite(mh);
	}
	
	@Test
	public void testDynamicMethod() throws Throwable {
		Out.init();
		try {
			String name = "π/χ/µ/print";
			ClassFile c = new ClassFile(new PromptoType(name));
			c.addModifier(Modifier.ABSTRACT);
			Descriptor.Method proto = new Descriptor.Method(String.class, void.class);
			MethodInfo m = c.newMethod("test", proto);
			m.addModifier(Modifier.STATIC);
			m.registerLocal("%value%", VerifierType.ITEM_Object, new ClassConstant(String.class));
			CompilerUtils.compileALOAD(m, "%value%");
			MethodConstant mc = new MethodConstant(this.getClass(), "bootstrap", 
					Lookup.class, String.class, MethodType.class, CallSite.class);
			MethodHandleConstant mhc = new MethodHandleConstant(mc);
			BootstrapMethod bsm = new BootstrapMethod(mhc);
			c.addBootstrapMethod(bsm);
			NameAndTypeConstant nameAndType = new NameAndTypeConstant("print", proto);
			CallSiteConstant constant = new CallSiteConstant(bsm, nameAndType);
			m.addInstruction(Opcode.INVOKEDYNAMIC, constant);
			m.addInstruction(Opcode.RETURN);
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			c.writeTo(o);
			byte[] gen = o.toByteArray();
			Class<?> klass = ByteClassLoader.defineAndResolveClass(name.replace("/", "."), gen);
			assertNotNull(klass);
			assertTrue(Modifier.isAbstract(klass.getModifiers()));
			Method mm = klass.getMethod("test", String.class);
			assertTrue(Modifier.isStatic(mm.getModifiers()));
			mm.invoke((Object)null, "Hello");
			assertEquals("Hello", Out.read());
		} finally {
			Out.restore();
		}
	}
}
