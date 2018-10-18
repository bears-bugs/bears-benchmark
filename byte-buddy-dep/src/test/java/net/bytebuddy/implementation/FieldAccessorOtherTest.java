package net.bytebuddy.implementation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.test.utility.CallTraceable;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import java.lang.reflect.Field;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FieldAccessorOtherTest {

    private static final String FOO = "foo", BAR = "bar", QUX = "qux", BAZ = "baz";

    @Test
    public void testArgumentSetter() throws Exception {
        Class<? extends SampleArgumentSetter> loaded = new ByteBuddy()
                .subclass(SampleArgumentSetter.class)
                .method(named(FOO))
                .intercept(FieldAccessor.ofField(FOO).setsArgumentAt(0))
                .make()
                .load(SampleArgumentSetter.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        SampleArgumentSetter sampleArgumentSetter = loaded.getDeclaredConstructor().newInstance();
        sampleArgumentSetter.foo(FOO);
        assertThat(sampleArgumentSetter.foo, is((Object) FOO));
    }

    @Test
    public void testArgumentSetterChained() throws Exception {
        Class<? extends SampleArgumentSetter> loaded = new ByteBuddy()
                .subclass(SampleArgumentSetter.class)
                .method(named(BAR))
                .intercept(FieldAccessor.ofField(FOO).setsArgumentAt(0).andThen(FixedValue.value(BAR)))
                .make()
                .load(SampleArgumentSetter.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        SampleArgumentSetter sampleArgumentSetter = loaded.getDeclaredConstructor().newInstance();
        assertThat(sampleArgumentSetter.bar(FOO), is((Object) BAR));
        assertThat(sampleArgumentSetter.foo, is((Object) FOO));
    }

    @Test(expected = IllegalStateException.class)
    public void testArgumentSetterNonVoid() throws Exception {
        new ByteBuddy()
                .subclass(SampleArgumentSetter.class)
                .method(named(BAR))
                .intercept(FieldAccessor.ofField(FOO).setsArgumentAt(0))
                .make();
    }

    @Test
    public void testArgumentSetterConstructor() throws Exception {
        Class<?> loaded = new ByteBuddy()
                .subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                .defineField(FOO, String.class, Visibility.PUBLIC, FieldManifestation.FINAL)
                .defineConstructor(Visibility.PUBLIC)
                .withParameters(String.class)
                .intercept(MethodCall.invoke(Object.class.getDeclaredConstructor()).andThen(FieldAccessor.ofField(FOO).setsArgumentAt(0)))
                .make()
                .load(null, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        assertThat(loaded.getDeclaredField(FOO).get(loaded.getDeclaredConstructor(String.class).newInstance(FOO)), is((Object) FOO));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentCannotBeNegative() throws Exception {
        FieldAccessor.ofField(FOO).setsArgumentAt(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void testArgumentSetterNoParameterAtIndex() throws Exception {
        new ByteBuddy()
                .subclass(SampleArgumentSetter.class)
                .method(named(FOO))
                .intercept(FieldAccessor.ofField(FOO).setsArgumentAt(1).andThen(FixedValue.value(BAR)))
                .make();
    }

    @Test
    public void testExplicitNameSetter() throws Exception {
        DynamicType.Loaded<SampleSetter> loaded = new ByteBuddy()
                .subclass(SampleSetter.class)
                .method(isDeclaredBy(SampleSetter.class))
                .intercept(FieldAccessor.ofField(FOO))
                .make()
                .load(SampleSetter.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        SampleSetter sampleSetter = loaded.getLoaded().getDeclaredConstructor().newInstance();
        Field field = SampleSetter.class.getDeclaredField(FOO);
        field.setAccessible(true);
        assertThat(field.get(sampleSetter), is((Object) QUX));
        sampleSetter.bar(BAZ);
        assertThat(field.get(sampleSetter), is((Object) BAZ));
        sampleSetter.assertZeroCalls();
    }

    @Test
    public void testExplicitNameGetter() throws Exception {
        DynamicType.Loaded<SampleGetter> loaded = new ByteBuddy()
                .subclass(SampleGetter.class)
                .method(isDeclaredBy(SampleGetter.class))
                .intercept(FieldAccessor.ofField(FOO))
                .make()
                .load(SampleSetter.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        SampleGetter sampleGetter = loaded.getLoaded().getDeclaredConstructor().newInstance();
        Field field = SampleGetter.class.getDeclaredField(FOO);
        field.setAccessible(true);
        assertThat(field.get(sampleGetter), is((Object) BAZ));
        assertThat(sampleGetter.bar(), is((Object) BAZ));
        assertThat(field.get(sampleGetter), is((Object) BAZ));
        sampleGetter.assertZeroCalls();
    }

    @Test(expected = IllegalStateException.class)
    public void testNotAssignable() throws Exception {
        new ByteBuddy()
                .subclass(Baz.class)
                .method(isDeclaredBy(Baz.class))
                .intercept(FieldAccessor.ofField(FOO))
                .make();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFinalFieldSetter() throws Exception {
        new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class))
                .intercept(FieldAccessor.ofBeanProperty())
                .make();
    }

    @Test(expected = IllegalStateException.class)
    public void testFieldNoVisibleField() throws Exception {
        new ByteBuddy()
                .subclass(Bar.class)
                .method(isDeclaredBy(Bar.class))
                .intercept(FieldAccessor.ofBeanProperty())
                .make();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFieldNoBeanMethodName() throws Exception {
        new ByteBuddy()
                .subclass(Qux.class)
                .method(isDeclaredBy(Qux.class))
                .intercept(FieldAccessor.ofBeanProperty())
                .make();
    }

    @Test(expected = IllegalStateException.class)
    public void testIncompatibleExplicitField() throws Exception {
        new ByteBuddy()
                .subclass(Qux.class)
                .method(isDeclaredBy(Qux.class))
                .intercept(FieldAccessor.of(Bar.class.getDeclaredField(BAR)))
                .make();
    }

    @Test(expected = IllegalStateException.class)
    public void testInaccessibleExplicitField() throws Exception {
        new ByteBuddy()
                .subclass(Bar.class)
                .method(isDeclaredBy(Bar.class))
                .intercept(FieldAccessor.of(Bar.class.getDeclaredField(BAR)))
                .make();
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(FieldAccessor.ForImplicitProperty.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.ForImplicitProperty.Appender.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.ForParameterSetter.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.ForParameterSetter.TerminationHandler.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.ForParameterSetter.Appender.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.FieldLocation.Absolute.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.FieldLocation.Relative.class).apply();
        ObjectPropertyAssertion.of(FieldAccessor.FieldLocation.Relative.Prepared.class).apply();
    }

    @SuppressWarnings("unused")
    public static class Foo {

        protected final Object foo = null;

        public void setFoo(Object o) {
            /* empty */
        }
    }

    @SuppressWarnings("unused")
    public static class Bar {

        private Object bar;

        public void setBar(Object o) {
            /* empty */
        }
    }

    public static class BarSub extends Bar {
        /* empty */
    }

    @SuppressWarnings("unused")
    public static class Qux {

        private Object qux;

        public void qux(Object o) {
            /* empty */
        }
    }

    @SuppressWarnings("unused")
    public static class Baz {

        public String foo;

        public void qux(Object o) {
            /* empty */
        }
    }

    public static class SampleArgumentSetter {

        public Object foo;

        public void foo(String value) {
            throw new AssertionError();
        }

        public Object bar(String value) {
            throw new AssertionError();
        }
    }

    public static class SampleGetter extends CallTraceable {

        protected Object foo = BAZ;

        public Object bar() {
            register(FOO);
            return QUX;
        }
    }

    public static class SampleSetter extends CallTraceable {

        protected Object foo = QUX;

        public void bar(Object foo) {
            register(FOO, foo);
        }
    }
}
