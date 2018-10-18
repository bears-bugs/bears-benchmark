package net.bytebuddy.dynamic;

import net.bytebuddy.test.utility.MockitoRule;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ClassFileLocatorCompoundTest {

    private static final String FOO = "foo";

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private ClassFileLocator classFileLocator;

    @Mock
    private ClassFileLocator otherClassFileLocator;

    @Mock
    private ClassFileLocator.Resolution legal, illegal;

    @Before
    public void setUp() throws Exception {
        when(legal.isResolved()).thenReturn(true);
    }

    @Test
    public void testApplicationOrderCallsSecond() throws Exception {
        when(classFileLocator.locate(FOO)).thenReturn(illegal);
        when(otherClassFileLocator.locate(FOO)).thenReturn(legal);
        assertThat(new ClassFileLocator.Compound(classFileLocator, otherClassFileLocator).locate(FOO), is(legal));
        verify(classFileLocator).locate(FOO);
        verifyNoMoreInteractions(classFileLocator);
        verify(otherClassFileLocator).locate(FOO);
        verifyNoMoreInteractions(otherClassFileLocator);
    }

    @Test
    public void testApplicationOrderDoesNotCallSecond() throws Exception {
        when(classFileLocator.locate(FOO)).thenReturn(legal);
        assertThat(new ClassFileLocator.Compound(classFileLocator, otherClassFileLocator).locate(FOO), is(legal));
        verify(classFileLocator).locate(FOO);
        verifyNoMoreInteractions(classFileLocator);
        verifyZeroInteractions(otherClassFileLocator);
    }

    @Test
    public void testClosable() throws Exception {
        when(classFileLocator.locate(FOO)).thenReturn(legal);
        new ClassFileLocator.Compound(classFileLocator, otherClassFileLocator).close();
        verify(classFileLocator).close();
        verifyNoMoreInteractions(classFileLocator);
        verify(otherClassFileLocator).close();
        verifyNoMoreInteractions(otherClassFileLocator);
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(ClassFileLocator.Compound.class).create(new ObjectPropertyAssertion.Creator<List<?>>() {
            @Override
            public List<?> create() {
                return Collections.singletonList(mock(ClassFileLocator.class));
            }
        }).apply();
    }
}
