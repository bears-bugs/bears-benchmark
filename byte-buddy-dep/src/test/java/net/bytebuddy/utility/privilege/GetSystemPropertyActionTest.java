package net.bytebuddy.utility.privilege;

import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GetSystemPropertyActionTest {

    private static final String FOO = "foo", BAR = "bar";

    @Test
    public void testRun() throws Exception {
        System.setProperty(FOO, BAR);
        try {
            assertThat(new GetSystemPropertyAction(FOO).run(), is(BAR));
        } finally {
            System.clearProperty(FOO);
        }
    }

    @Test
    public void testObjectProperty() throws Exception {
        ObjectPropertyAssertion.of(GetSystemPropertyAction.class).apply();
    }
}