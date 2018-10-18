package net.bytebuddy.implementation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.test.utility.CallTraceable;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class ExceptionMethodTest {

    private static final String FOO = "foo", BAR = "bar";

    @Test
    public void testWithoutMessage() throws Exception {
        DynamicType.Loaded<Foo> loaded = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(ExceptionMethod.throwing(RuntimeException.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        assertThat(loaded.getLoadedAuxiliaryTypes().size(), is(0));
        assertThat(loaded.getLoaded().getDeclaredMethods().length, is(1));
        assertThat(loaded.getLoaded().getDeclaredFields().length, is(0));
        Foo instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        assertThat(instance.getClass(), not(CoreMatchers.<Class<?>>is(Foo.class)));
        assertThat(instance, instanceOf(Foo.class));
        try {
            instance.foo();
            fail();
        } catch (RuntimeException exception) {
            assertThat(exception.getClass(), CoreMatchers.<Class<?>>is(RuntimeException.class));
            assertThat(exception.getMessage(), nullValue());
        }
        instance.assertZeroCalls();
    }

    @Test
    public void testWithMessage() throws Exception {
        DynamicType.Loaded<Foo> loaded = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(ExceptionMethod.throwing(RuntimeException.class, BAR))
                .make()
                .load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        assertThat(loaded.getLoadedAuxiliaryTypes().size(), is(0));
        assertThat(loaded.getLoaded().getDeclaredMethods().length, is(1));
        assertThat(loaded.getLoaded().getDeclaredFields().length, is(0));
        Foo instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        assertThat(instance.getClass(), not(CoreMatchers.<Class<?>>is(Foo.class)));
        assertThat(instance, instanceOf(Foo.class));
        try {
            instance.foo();
            fail();
        } catch (RuntimeException exception) {
            assertThat(exception.getClass(), CoreMatchers.<Class<?>>is(RuntimeException.class));
            assertThat(exception.getMessage(), is(BAR));
        }
        instance.assertZeroCalls();
    }

    @Test
    public void testWithNonDeclaredCheckedException() throws Exception {
        DynamicType.Loaded<Foo> loaded = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(ExceptionMethod.throwing(Exception.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        assertThat(loaded.getLoadedAuxiliaryTypes().size(), is(0));
        assertThat(loaded.getLoaded().getDeclaredMethods().length, is(1));
        assertThat(loaded.getLoaded().getDeclaredFields().length, is(0));
        Foo instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        assertThat(instance.getClass(), not(CoreMatchers.<Class<?>>is(Foo.class)));
        assertThat(instance, instanceOf(Foo.class));
        try {
            instance.foo();
            fail();
        } catch (Exception exception) {
            assertThat(exception.getClass(), CoreMatchers.<Class<?>>is(Exception.class));
            assertThat(exception.getMessage(), nullValue());
        }
        instance.assertZeroCalls();
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(ExceptionMethod.class).apply();
        ObjectPropertyAssertion.of(ExceptionMethod.ConstructionDelegate.ForDefaultConstructor.class).refine(new ObjectPropertyAssertion.Refinement<TypeDescription>() {
            @Override
            public void apply(TypeDescription mock) {
                when(mock.getDeclaredMethods()).thenReturn(new MethodList.ForLoadedMethods(Object.class));
            }
        }).apply();
        ObjectPropertyAssertion.of(ExceptionMethod.ConstructionDelegate.ForStringConstructor.class).refine(new ObjectPropertyAssertion.Refinement<TypeDescription>() {
            @Override
            public void apply(TypeDescription mock) {
                when(mock.getDeclaredMethods()).thenReturn(new MethodList.ForLoadedMethods(String.class));
            }
        }).apply();
    }

    public static class Foo extends CallTraceable {

        public void foo() {
            register(FOO);
        }
    }
}
