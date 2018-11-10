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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class PerspectiveTest {

    @Test
    public void testPerspective() {
        Perspective perspective = new Perspective();
        assertEquals(Perspective.NONE, perspective.getCssValue());
    }

    @Test
    public void testPerspectiveString() {
        {
            Perspective perspective = new Perspective(Perspective.NONE);
            assertEquals(Perspective.NONE, perspective.getCssValue());
        }
        {
            Perspective perspective = new Perspective("50px");
            assertEquals("50px", perspective.getCssValue());
        }
    }

    @Test
    public void testPerspectivePerspective() {
        Perspective perspective = new Perspective("50px");
        Perspective perspective1 = new Perspective(perspective);
        assertEquals("50px", perspective1.getCssValue());
    }

    @Test
    public void testPerspectiveFloat() {
        Perspective perspective = new Perspective(75);
        assertEquals("75.0%", perspective.getCssValue());
    }

    @Test
    public void testPerspectiveFloatCssLengthUnit() {
        {
            Perspective perspective = new Perspective(75, CssLengthUnit.PER);
            assertEquals("75.0%", perspective.getCssValue());
            assertEquals(CssLengthUnit.PER, perspective.getUnit());
            assertTrue(perspective.getValue() == 75);
        }
        {
            Perspective perspective = new Perspective(75, CssLengthUnit.CH);
            assertEquals("75.0ch", perspective.getCssValue());
        }
        {
            Perspective perspective = new Perspective(75, CssLengthUnit.EM);
            assertEquals("75.0em", perspective.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            Perspective perspective = new Perspective();
            perspective.setPercent(75);
            assertEquals("75.0%", perspective.getCssValue());
            assertEquals(CssLengthUnit.PER, perspective.getUnit());
            assertTrue(perspective.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        Perspective perspective = new Perspective();
        assertEquals(CssNameConstants.PERSPECTIVE, perspective.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Perspective perspective = new Perspective();
        assertEquals(Perspective.NONE, perspective.getCssValue());
        perspective.setCssValue("25px");
        assertEquals("25px", perspective.getCssValue());
    }

    @Test
    public void testToString() {
        Perspective perspective = new Perspective(75, CssLengthUnit.EM);
        assertEquals(perspective.getCssName() + ": 75.0em", perspective.toString());
    }

    @Test
    public void testGetValue() {
        Perspective perspective = new Perspective();
        perspective.setPercent(75);
        assertTrue(perspective.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        Perspective perspective = new Perspective();
        perspective.setPercent(75);
        assertEquals(CssLengthUnit.PER, perspective.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        Perspective perspective = new Perspective();
        perspective.setCssValue("75%");
        assertEquals("75%", perspective.getCssValue());
        assertEquals(CssLengthUnit.PER, perspective.getUnit());
        assertTrue(perspective.getValue() == 75);
    }

    @Test
    public void testSetAsAuto() {
        Perspective perspective = new Perspective();
        perspective.setAsAuto();
        assertEquals(Perspective.NONE, perspective.getCssValue());
       assertNull(perspective.getValue());
       assertNull(perspective.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = Perspective.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Perspective.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Perspective.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Perspective.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Perspective.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Perspective.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Perspective.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Perspective.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Perspective.isValid("45rem");
            assertTrue(valid);
        }
    }

}
