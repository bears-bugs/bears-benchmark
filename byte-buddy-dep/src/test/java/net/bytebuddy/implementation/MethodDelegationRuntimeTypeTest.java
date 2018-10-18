package net.bytebuddy.implementation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.Test;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodDelegationRuntimeTypeTest {

    private static final String FOO = "FOO";

    @Test
    public void testRuntimeType() throws Exception {
        DynamicType.Loaded<Foo> loaded = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        Foo instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        assertThat(instance.foo(FOO), is(FOO));
    }

    public static class Foo {

        public String foo(Object o) {
            return (String) o;
        }
    }

    public static class Bar {

        @RuntimeType
        public static Object foo(@RuntimeType String s) {
            return s;
        }
    }
}
