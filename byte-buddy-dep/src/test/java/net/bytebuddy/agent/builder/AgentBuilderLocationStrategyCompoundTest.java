package net.bytebuddy.agent.builder;

import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.test.utility.MockitoRule;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import net.bytebuddy.utility.JavaModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class AgentBuilderLocationStrategyCompoundTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private AgentBuilder.LocationStrategy first, second;

    @Mock
    private ClassFileLocator firstLocator, secondLocator;

    @Mock
    private ClassLoader classLoader;

    @Mock
    private JavaModule module;

    @Test
    public void testApplication() throws Exception {
        AgentBuilder.LocationStrategy locationStrategy = new AgentBuilder.LocationStrategy.Compound(first, second);
        when(first.classFileLocator(classLoader, module)).thenReturn(firstLocator);
        when(second.classFileLocator(classLoader, module)).thenReturn(secondLocator);
        assertThat(locationStrategy.classFileLocator(classLoader, module), is((ClassFileLocator) new ClassFileLocator.Compound(firstLocator, secondLocator)));
        verify(first).classFileLocator(classLoader, module);
        verifyNoMoreInteractions(first);
        verify(second).classFileLocator(classLoader, module);
        verifyNoMoreInteractions(second);
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(AgentBuilder.LocationStrategy.Compound.class).create(new ObjectPropertyAssertion.Creator<List<?>>() {
            @Override
            public List<?> create() {
                return Collections.singletonList(mock(AgentBuilder.LocationStrategy.class));
            }
        }).apply();
    }
}
