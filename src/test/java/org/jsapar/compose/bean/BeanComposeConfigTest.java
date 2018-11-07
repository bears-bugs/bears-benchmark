package org.jsapar.compose.bean;

import org.jsapar.error.ValidationAction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for BeanComposeConfig
 * Created by stejon0 on 2016-10-16.
 */
public class BeanComposeConfigTest {

    @Test
    public void testGetSetOnUndefinedLineType() throws Exception {
        BeanComposeConfig c = new BeanComposeConfig();
        c.setOnUndefinedLineType(ValidationAction.NONE);
        assertEquals(ValidationAction.NONE, c.getOnUndefinedLineType());
        c.setOnUndefinedLineType(ValidationAction.ERROR);
        assertEquals(ValidationAction.ERROR, c.getOnUndefinedLineType());
    }

}