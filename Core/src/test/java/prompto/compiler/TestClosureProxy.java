package prompto.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import prompto.intrinsic.PromptoProxy;
import prompto.runtime.utils.Out;

public class TestClosureProxy {

	public static interface name {
		String xname();
	}
	
	public static class text {
		public String xtext() {
			return "Hello";
		}
	}
	

	public static void method(name n) {
		System.out.print(n.xname());
	}
	
	@Test
	public void test() {
		Out.init();
		try {
			text t = new text();
			method(PromptoProxy.newProxy(t, name.class, "xtext", new Class[0]));
			assertEquals("Hello", Out.read());
		} finally {
			Out.restore();
		}
	}
}
