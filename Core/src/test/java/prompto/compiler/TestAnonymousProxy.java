package prompto.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import prompto.intrinsic.PromptoProxy;
import prompto.runtime.utils.Out;

public class TestAnonymousProxy {

	public static interface name {
		String getName();
	}
	
	public static interface text {
		String getText();
	}
	
	public static interface combined extends name, text {
		
		public static class Combined implements combined {
			@Override public String getName() { return "combined-name"; }
			@Override public String getText() { return "combined-text"; }
		}
	}
	
	public static interface anonymous extends name, text {
	}
	
	
	public static void method(anonymous a) {
		System.out.print("/" + a.getName() + "/" + a.getText());
	}
	
	@Test
	public void test() {
		Out.init();
		try {
			combined c = new combined.Combined();
			method(PromptoProxy.newProxy(c, anonymous.class));
			assertEquals("/" + c.getName() + "/" + c.getText(), Out.read());
		} finally {
			Out.restore();
		}
	}
}
