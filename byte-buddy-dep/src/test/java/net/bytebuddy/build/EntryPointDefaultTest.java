package net.bytebuddy.build;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.test.utility.MockitoRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static net.bytebuddy.test.utility.FieldByFieldComparison.hasPrototype;
import static net.bytebuddy.test.utility.FieldByFieldComparison.matchesPrototype;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class EntryPointDefaultTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private TypeDescription typeDescription;

    @Mock
    private ByteBuddy byteBuddy;

    @Mock
    private ClassFileLocator classFileLocator;

    @Mock
    private MethodNameTransformer methodNameTransformer;

    @Mock
    private DynamicType.Builder<?> builder, otherBuilder;

    @Test
    @SuppressWarnings("unchecked")
    public void testRebase() throws Exception {
        assertThat(EntryPoint.Default.REBASE.byteBuddy(ClassFileVersion.ofThisVm()), hasPrototype(new ByteBuddy()));
        when(byteBuddy.rebase(typeDescription, classFileLocator, methodNameTransformer)).thenReturn((DynamicType.Builder) builder);
        assertThat(EntryPoint.Default.REBASE.transform(typeDescription, byteBuddy, classFileLocator, methodNameTransformer), hasPrototype((DynamicType.Builder) builder));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRedefine() throws Exception {
        assertThat(EntryPoint.Default.REDEFINE.byteBuddy(ClassFileVersion.ofThisVm()), hasPrototype(new ByteBuddy()));
        when(byteBuddy.redefine(typeDescription, classFileLocator)).thenReturn((DynamicType.Builder) builder);
        assertThat(EntryPoint.Default.REDEFINE.transform(typeDescription, byteBuddy, classFileLocator, methodNameTransformer), hasPrototype((DynamicType.Builder) builder));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRedefineLocal() throws Exception {
        assertThat(EntryPoint.Default.REDEFINE_LOCAL.byteBuddy(ClassFileVersion.ofThisVm()),
                hasPrototype(new ByteBuddy().with(Implementation.Context.Disabled.Factory.INSTANCE)));
        when(byteBuddy.redefine(typeDescription, classFileLocator)).thenReturn((DynamicType.Builder) builder);
        when(builder.ignoreAlso(matchesPrototype(not(isDeclaredBy(typeDescription))))).thenReturn((DynamicType.Builder) otherBuilder);
        assertThat(EntryPoint.Default.REDEFINE_LOCAL.transform(typeDescription, byteBuddy, classFileLocator, methodNameTransformer), hasPrototype((DynamicType.Builder) otherBuilder));
    }
}
