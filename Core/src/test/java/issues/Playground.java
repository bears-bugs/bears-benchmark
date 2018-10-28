package issues;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Playground {

	@Test
	public void testStuff() {
		Method method = Arrays.asList(Consumer.class.getDeclaredMethods()).stream().filter(m->"accept".equals(m.getName())).findFirst().orElse(null);
		System.out.println(method);
	}
	

}
