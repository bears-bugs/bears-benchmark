/**
 * 
 */
package org.jsapar.schema;

import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * @author stejon0
 *
 */
public class FixedWidthCellPositionsTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

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
     * Test method for {@link org.jsapar.schema.FixedWidthCellPositions#increment(org.jsapar.schema.FixedWidthSchemaCell)}.
     */
    @Test
    public void testIncrement() {
        FixedWidthSchemaCell cell = new FixedWidthSchemaCell("test", 17);
        FixedWidthCellPositions pos = new FixedWidthCellPositions();
        pos.increment(cell);
        assertEquals(1, pos.getFirst());
        assertEquals(17, pos.getLast());

        pos.increment(cell);
        assertEquals(18, pos.getFirst());
        assertEquals(34, pos.getLast());
    }

}
