package net.bytebuddy.matcher;

import net.bytebuddy.test.utility.MockitoRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CollectionElementMatcherTest extends AbstractElementMatcherTest<CollectionElementMatcher<?>> {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    private Iterable<Object> iterable;

    private Object elemet;

    @Mock
    private ElementMatcher<? super Object> elementMatcher;

    @SuppressWarnings("unchecked")
    public CollectionElementMatcherTest() {
        super((Class<CollectionElementMatcher<?>>) (Object) CollectionElementMatcher.class, "with");
    }

    @Before
    public void setUp() throws Exception {
        elemet = new Object();
        iterable = Arrays.asList(new Object(), elemet);
    }

    @Test
    public void testMatch() throws Exception {
        when(elementMatcher.matches(elemet)).thenReturn(true);
        assertThat(new CollectionElementMatcher<Object>(1, elementMatcher).matches(iterable), is(true));
        verify(elementMatcher).matches(elemet);
        verifyNoMoreInteractions(elementMatcher);
    }

    @Test
    public void testNoMatch() throws Exception {
        when(elementMatcher.matches(elemet)).thenReturn(false);
        assertThat(new CollectionElementMatcher<Object>(1, elementMatcher).matches(iterable), is(false));
        verify(elementMatcher).matches(elemet);
        verifyNoMoreInteractions(elementMatcher);
    }

    @Test
    public void testNoMatchIndex() throws Exception {
        assertThat(new CollectionElementMatcher<Object>(2, elementMatcher).matches(iterable), is(false));
        verifyZeroInteractions(elementMatcher);
    }
}
