package net.bytebuddy.dynamic.loading;

import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PackageTypeStrategyNoOpTest {

    private static final String FOO = "foo", BAR = "bar";

    @Test
    public void testPackageNotDefined() throws Exception {
        assertThat(PackageDefinitionStrategy.NoOp.INSTANCE.define(getClass().getClassLoader(), FOO, BAR).isDefined(), is(false));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(PackageDefinitionStrategy.NoOp.class).apply();
    }
}
