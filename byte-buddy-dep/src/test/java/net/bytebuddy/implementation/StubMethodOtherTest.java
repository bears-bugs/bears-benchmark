package net.bytebuddy.implementation;

import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.test.utility.MockitoRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StubMethodOtherTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private InstrumentedType instrumentedType;

    @Mock
    private Implementation implementation;

    @Test
    public void testPreparation() throws Exception {
        assertThat(StubMethod.INSTANCE.prepare(instrumentedType), is(instrumentedType));
        verifyZeroInteractions(instrumentedType);
    }

    @Test
    public void testComposition() throws Exception {
        assertThat(StubMethod.INSTANCE.andThen(implementation), is(implementation));
    }
}
