/**
 * 
 */
package org.jsapar.model;

import org.jsapar.schema.SchemaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author stejon0
 *
 */
public class NumberCellTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test method for {@link NumberCell#getValue()}.
     */
    @Test
    public void testGetValue() {
        IntegerCell cell = new IntegerCell("test", 42);
        Assert.assertEquals(42, cell.getValue());
    }


    /**
     * Test method for {@link NumberCell#getValue()}.
     */
    @Test
    public void testGetNumberValue() {
        IntegerCell cell = new IntegerCell("test", 42);
        Assert.assertEquals(42, cell.getValue());
    }


    @Test
    public void testCompareValueTo_eq() throws SchemaException{
        NumberCell left = new FloatCell("test", 10.1);
        NumberCell right = new FloatCell("test", 10.1);
        Assert.assertEquals(true, left.compareValueTo(right) == 0 );
    }

    @Test
    public void testCompareValueTo_lt() throws SchemaException{
        NumberCell left = new FloatCell("test", 0.1);
        NumberCell right = new FloatCell("test", 10.1);
        Assert.assertEquals(true, left.compareValueTo(right) < 0 );
    }

    @Test
    public void testCompareValueTo_lt_big() throws SchemaException{
        NumberCell left = new FloatCell("test", 10.1);
        NumberCell right = new BigDecimalCell("test", new BigDecimal(1000011010100.1321));
        Assert.assertEquals(true, left.compareValueTo(right) < 0 );
    }
    
}
