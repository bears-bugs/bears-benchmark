/*
* Copyright 2014-2018 Web Firm Framework
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* @author WFF
*/
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class ColumnsTest {

    @Test
    public void testColumns() {
        Columns columns = new Columns();
        assertEquals("auto auto", columns.getCssValue());
        assertEquals(ColumnWidth.AUTO, columns.getColumnWidth().getCssValue());
        assertEquals(ColumnWidth.AUTO, columns.getColumnWidth().getCssValue());
    }

    @Test
    public void testColumnsString() {
        Columns columns = new Columns("55px 5");
        assertEquals("55px 5", columns.getCssValue());
        assertEquals("55px", columns.getColumnWidth().getCssValue());
        assertEquals("5", columns.getColumnCount().getCssValue());
    }

    @Test
    public void testColumnsColumns() {
        Columns columns = new Columns("55px 5");
        Columns columns1 = new Columns(columns);
        assertEquals("55px 5", columns1.getCssValue());
        assertEquals("55px", columns1.getColumnWidth().getCssValue());
        assertEquals("5", columns1.getColumnCount().getCssValue());
    }

    @Test
    public void testGetCssName() {
        Columns columns = new Columns();
        assertEquals(CssNameConstants.COLUMNS, columns.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Columns columns = new Columns();
        assertEquals("auto auto", columns.getCssValue());
        Columns columns1 = new Columns("55px 5");
        assertEquals("55px 5", columns1.getCssValue());
    }

    @Test
    public void testToString() {
        Columns columns = new Columns("55px 5");
        assertEquals(CssNameConstants.COLUMNS
                + ": 55px 5", columns.toString());
    }

    @Test
    public void testSetCssValueString() {
        try {
            Columns columns = new Columns();
            columns.setCssValue("55px 5");
            assertEquals("55px 5", columns.getCssValue());
            assertEquals("55px", columns.getColumnWidth().getCssValue());
            assertEquals("5", columns.getColumnCount().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testIsValid() {
        assertTrue(Columns.isValid("55px 5"));
        
        assertTrue(Columns.isValid("auto"));
        assertTrue(Columns.isValid(Columns.INHERIT));
        assertTrue(Columns.isValid(Columns.INITIAL));
        assertTrue(Columns.isValid("5"));
        assertTrue(Columns.isValid("55px"));
        
        assertFalse(Columns.isValid("cir cle inside url(Test.png)"));
        assertFalse(Columns.isValid("dircle inside url(Test.png)"));
        assertFalse(Columns.isValid("circle ins ide url(Test.png)"));
        assertFalse(Columns.isValid("circle insside url(Test.png)"));
        assertFalse(Columns.isValid("circle inside ur l(Test.png)"));
        assertFalse(Columns.isValid("circle inside urll(Test.png)"));
//        fail("Not yet implemented");
    }

    @Test
    public void testSetAsInitial() {
        Columns columns = new Columns();
        final ColumnWidth columnWidth = columns.getColumnWidth();
        assertNotNull(columnWidth);
        final ColumnCount columnCount = columns.getColumnCount();
        assertNotNull(columnWidth);
        
        assertTrue(columnWidth.isAlreadyInUse());
        assertTrue(columnCount.isAlreadyInUse());
        
        columns.setAsInitial();
        
        assertEquals(Columns.INITIAL, columns.getCssValue());
        assertNull(columns.getColumnWidth());
        assertNull(columns.getColumnCount());
        assertFalse(columnWidth.isAlreadyInUse());
        assertFalse(columnCount.isAlreadyInUse());
    }

    @Test
    public void testSetAsInherit() {
        Columns columns = new Columns();
        columns.setAsInherit();
        assertEquals(Columns.INHERIT, columns.getCssValue());
    }

    @Test
    public void testGetColumnWidth() {
        try {
            Columns columns = new Columns();
            columns.setCssValue("55px 5");
            assertEquals("55px 5", columns.getCssValue());
            assertNotNull(columns.getColumnWidth());
            assertEquals("55px", columns.getColumnWidth().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetColumnCount() {
        try {
            Columns columns = new Columns();
            columns.setCssValue("55px 5");
            assertEquals("55px 5", columns.getCssValue());
            assertNotNull(columns.getColumnCount());
            assertEquals("5", columns.getColumnCount().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSetColumnWidth() {
        try {
            Columns columns = new Columns();
            columns.setCssValue("55px 5");
            columns.setColumnWidth(new ColumnWidth("155px"));
            assertEquals("155px 5", columns.getCssValue());
            assertNotNull(columns.getColumnWidth());
            assertEquals("155px", columns.getColumnWidth().getCssValue());
            assertNotNull(columns.getColumnCount());
            assertEquals("5", columns.getColumnCount().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }
    
    @Test
    public void testSetColumnCount() {
        try {
            Columns columns = new Columns();
            columns.setCssValue("55px 5");
            columns.setColumnCount(new ColumnCount("2"));
            assertEquals("55px 2", columns.getCssValue());
            assertNotNull(columns.getColumnCount());
            assertEquals("2", columns.getColumnCount().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }
    
    @Test
    public void testName() throws Exception {
        // type position image
        String sample = "155px 10";
        sample = sample.trim();
        final String[] sampleParts = sample.split(" ");

        ColumnWidth columnWidth = null;
        ColumnCount columnCount = null;

        for (final String eachPart : sampleParts) {
           if (columnWidth == null
                    && ColumnWidth.isValid(eachPart)) {
                columnWidth = new ColumnWidth(eachPart);
//                System.out.println(columnWidth);
            } else if (columnCount == null
                    && ColumnWidth.isValid(eachPart)) {
                columnCount = new ColumnCount(eachPart);
//                System.out.println(columnWidth);
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
//        boolean invalid = true;
        if (columnWidth != null) {
            cssValueBuilder.append(columnWidth.getCssValue());
            cssValueBuilder.append(" ");
//            invalid = false;
        }
//        System.out.println(invalid);
//        System.out.println(StringBuilderUtil.getTrimmedString(cssValueBuilder));
//        System.out.println(Columns.isValid(sample));

        // System.out.println(part1);
        // System.out.println(part2);
        // System.out.println(part3);

    }
    
    @Test
    public void testColumnsNullValue() {
        try {
            Columns columns = new Columns();
            columns.setColumnWidth(null);
            columns.setColumnCount(null);
            assertEquals("inherit", columns.getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testColumnsInvalidValueValue() {

        Columns columns = new Columns("inherit");
        final ColumnWidth columnWidth = columns.getColumnWidth();
        final ColumnCount columnCount = columns.getColumnCount();

        assertNull(columnWidth);
        
        assertNull(columnCount);
        
        {
            assertEquals("inherit", columns.getCssValue());
            

            ColumnWidth columnWidthTemp = new ColumnWidth("initial");
            try {
                columns.setColumnWidth(columnWidthTemp);// must throw an
                                                                // InvalidValueException
            } catch (InvalidValueException e) {

                assertNull(columns.getColumnWidth());

                columnWidthTemp.setAsAuto();
                columns.setColumnWidth(columnWidthTemp);
                final String columnWidthTempCssValue = columnWidthTemp
                        .getCssValue();
                try {
                    columnWidthTemp.setAsInherit();// must throw an
                                                      // InvalidValueException
                } catch (InvalidValueException e1) {
                    assertEquals(columnWidthTempCssValue,
                            columnWidthTemp.getCssValue());
                    throw e1;
                }

            }
        }
        

    }
    
    @Test(expected = InvalidValueException.class)
    public void testColumnsInvalidValueValue2() {

        Columns columns = new Columns("inherit");
        final ColumnWidth columnWidth = columns.getColumnWidth();
        final ColumnCount columnCount = columns.getColumnCount();

        assertNull(columnWidth);
        
        assertNull(columnCount);
        
        {
            assertEquals("inherit", columns.getCssValue());

            ColumnCount columnCountTemp = new ColumnCount("initial");
            try {
                columns.setColumnCount(columnCountTemp);// must throw an
                                                                // InvalidValueException
            } catch (InvalidValueException e) {

                assertNull(columns.getColumnCount());

                columnCountTemp.setAsAuto();
                columns.setColumnCount(columnCountTemp);
                final String columnCountTempCssValue = columnCountTemp
                        .getCssValue();
                try {
                    columnCountTemp.setAsInherit();// must throw an
                                                      // InvalidValueException
                } catch (InvalidValueException e1) {
                    assertEquals(columnCountTempCssValue,
                            columnCountTemp.getCssValue());
                    throw e1;
                }

            }
        }

    }
    
    @Test(expected = InvalidValueException.class)
    public void testColumnsSetCssValueInvalidValueValue() {
        try {
            new Columns("disc outside initial");
        } catch (Exception e1) {
            try {
                new Columns("disc outside inherit");
            } catch (Exception e2) {
                Columns columns = new Columns();
                try {
                    columns.setCssValue("disc outside inherit");
                } catch (Exception e) {
                    assertEquals("auto auto", columns.getCssValue());
                    throw e;
                }
            }
        }
    }

}
