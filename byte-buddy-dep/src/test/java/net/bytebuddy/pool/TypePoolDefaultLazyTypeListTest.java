package net.bytebuddy.pool;

import net.bytebuddy.description.type.AbstractTypeListTest;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.anyOf;

public class TypePoolDefaultLazyTypeListTest extends AbstractTypeListTest<Class<?>> {

    private TypePool typePool;

    @Before
    public void setUp() throws Exception {
        typePool = TypePool.Default.ofClassPath();
    }

    @After
    public void tearDown() throws Exception {
        typePool.clear();
    }

    @Override
    protected Class<?> getFirst() throws Exception {
        return Foo.class;
    }

    @Override
    protected Class<?> getSecond() throws Exception {
        return Bar.class;
    }

    @Override
    protected TypeList asList(List<Class<?>> elements) {
        return typePool.describe(Holder.class.getName()).resolve().getInterfaces().asErasures().filter(anyOf(elements.toArray(new Class<?>[elements.size()])));
    }

    @Override
    protected TypeDescription asElement(Class<?> element) {
        return new TypeDescription.ForLoadedType(element);
    }
}
