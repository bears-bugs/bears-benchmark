package net.bytebuddy.pool;

import net.bytebuddy.description.type.AbstractTypeDescriptionTest;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.is;

public class TypePoolDefaultTypeDescriptionSuperClassLoadingTest extends AbstractTypeDescriptionTest {

    @Override
    protected TypeDescription describe(Class<?> type) {
        TypePool typePool = new TypePool.Default(TypePool.CacheProvider.NoOp.INSTANCE,
                ClassFileLocator.ForClassLoader.of(type.getClassLoader()),
                TypePool.Default.ReaderMode.EXTENDED);
        try {
            return new TypeDescription.SuperTypeLoading(typePool.describe(type.getName()).resolve(), type.getClassLoader());
        } finally {
            typePool.clear();
        }
    }

    @Override
    protected TypeDescription.Generic describeType(Field field) {
        return describe(field.getDeclaringClass()).getDeclaredFields().filter(is(field)).getOnly().getType();
    }

    @Override
    protected TypeDescription.Generic describeReturnType(Method method) {
        return describe(method.getDeclaringClass()).getDeclaredMethods().filter(is(method)).getOnly().getReturnType();
    }

    @Override
    protected TypeDescription.Generic describeParameterType(Method method, int index) {
        return describe(method.getDeclaringClass()).getDeclaredMethods().filter(is(method)).getOnly().getParameters().get(index).getType();
    }

    @Override
    protected TypeDescription.Generic describeExceptionType(Method method, int index) {
        return describe(method.getDeclaringClass()).getDeclaredMethods().filter(is(method)).getOnly().getExceptionTypes().get(index);
    }

    @Override
    protected TypeDescription.Generic describeSuperClass(Class<?> type) {
        return describe(type).getSuperClass();
    }

    @Override
    protected TypeDescription.Generic describeInterfaceType(Class<?> type, int index) {
        return describe(type).getInterfaces().get(index);
    }

    @Test
    public void testDelegateProperties() throws Exception {
        ObjectPropertyAssertion.of(TypeDescription.SuperTypeLoading.ClassLoadingDelegate.Simple.class).apply();
    }
}
