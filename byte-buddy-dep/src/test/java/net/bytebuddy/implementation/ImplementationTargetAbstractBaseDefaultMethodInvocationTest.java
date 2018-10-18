package net.bytebuddy.implementation;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImplementationTargetAbstractBaseDefaultMethodInvocationTest {

    @Test
    public void testEnabled() throws Exception {
        assertThat(Implementation.Target.AbstractBase.DefaultMethodInvocation.of(ClassFileVersion.JAVA_V8),
                is(Implementation.Target.AbstractBase.DefaultMethodInvocation.ENABLED));
    }

    @Test
    public void testDisabled() throws Exception {
        assertThat(Implementation.Target.AbstractBase.DefaultMethodInvocation.of(ClassFileVersion.JAVA_V7),
                is(Implementation.Target.AbstractBase.DefaultMethodInvocation.DISABLED));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(Implementation.Target.AbstractBase.DefaultMethodInvocation.class).apply();
    }
}
