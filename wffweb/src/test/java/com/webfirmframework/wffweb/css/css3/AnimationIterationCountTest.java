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
 */
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class AnimationIterationCountTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#AnimationIterationCount()}.
     */
    @Test
    public void testAnimationIterationCount() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        assertEquals(1, animationIterationCount.getValue().intValue());
        assertEquals("1", animationIterationCount.getCssValue());
        
        animationIterationCount.setAsInherit();
        assertEquals(AnimationIterationCount.INHERIT, animationIterationCount.getCssValue());
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#AnimationIterationCount(java.lang.String)}.
     */
    @Test
    public void testAnimationIterationCountString() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount("2");
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        assertEquals("2", animationIterationCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#AnimationIterationCount(com.webfirmframework.wffweb.css.css3.AnimationIterationCount)}.
     */
    @Test
    public void testAnimationIterationCountAnimationIterationCount() {
        final AnimationIterationCount animationIterationCount1 = new AnimationIterationCount("2");
        AnimationIterationCount animationIterationCount = new AnimationIterationCount(animationIterationCount1);
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        assertEquals("2", animationIterationCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#AnimationIterationCount(integer)}.
     */
    @Test
    public void testAnimationIterationCountInteger() {
        {
            AnimationIterationCount animationIterationCount = new AnimationIterationCount(2);
            assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
            assertEquals("2", animationIterationCount.getCssValue());
            
        }
        {
            final AnimationIterationCount animationIterationCount1 = new AnimationIterationCount(2);
            AnimationIterationCount animationIterationCount = new AnimationIterationCount(animationIterationCount1);
            assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
            assertEquals("2", animationIterationCount.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount(2);
        assertEquals(CssNameConstants.ANIMATION_ITERATION_COUNT, animationIterationCount.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        assertEquals(1, animationIterationCount.getValue().intValue());
        assertEquals("1", animationIterationCount.getCssValue());

        animationIterationCount.setAsInherit();
        assertEquals(AnimationIterationCount.INHERIT, animationIterationCount.getCssValue());
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#toString()}.
     */
    @Test
    public void testToString() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        assertEquals(1, animationIterationCount.getValue().intValue());
        assertEquals(CssNameConstants.ANIMATION_ITERATION_COUNT + ": 1", animationIterationCount.toString());

        animationIterationCount.setAsInherit();
        assertEquals(CssNameConstants.ANIMATION_ITERATION_COUNT + ": " + AnimationIterationCount.INHERIT,
                animationIterationCount.toString());
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#getValue()}.
     */
    @Test
    public void testGetValue() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        assertEquals(1, animationIterationCount.getValue().intValue());
        animationIterationCount.setAsInherit();
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#setValue(integer)}.
     */
    @Test
    public void testSetValue() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        animationIterationCount.setValue(2);
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        animationIterationCount.setAsInherit();
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        animationIterationCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        animationIterationCount.setAsInherit();
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        animationIterationCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        animationIterationCount.setAsInitial();
        assertEquals(AnimationIterationCount.INITIAL, animationIterationCount.getCssValue());
        assertNull(animationIterationCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        AnimationIterationCount animationIterationCount = new AnimationIterationCount();
        animationIterationCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), animationIterationCount.getValue());
        animationIterationCount.setAsInherit();
        assertEquals(AnimationIterationCount.INHERIT, animationIterationCount.getCssValue());
        assertNull(animationIterationCount.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new AnimationIterationCount().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        try {
            new AnimationIterationCount().setCssValue("2px");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.AnimationIterationCount#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        
        assertTrue(AnimationIterationCount.isValid("1"));
        assertTrue(AnimationIterationCount.isValid("0"));
        assertTrue(AnimationIterationCount.isValid(AnimationIterationCount.INITIAL));
        assertTrue(AnimationIterationCount.isValid(AnimationIterationCount.INHERIT));
        assertTrue(AnimationIterationCount.isValid(AnimationIterationCount.INFINITE));
        
        assertFalse(AnimationIterationCount.isValid(".5"));
        assertFalse(AnimationIterationCount.isValid("0.5"));
        assertFalse(AnimationIterationCount.isValid("0.1"));
        assertFalse(AnimationIterationCount.isValid("dfd"));
        assertFalse(AnimationIterationCount.isValid("1px"));
        assertFalse(AnimationIterationCount.isValid(""));
        assertFalse(AnimationIterationCount.isValid("1 1"));
        assertFalse(AnimationIterationCount.isValid("1.1"));
        assertFalse(AnimationIterationCount.isValid("-0.5"));
        assertFalse(AnimationIterationCount.isValid("-0"));
        assertFalse(AnimationIterationCount.isValid("-0.0"));
        assertFalse(AnimationIterationCount.isValid("+0.0"));
    }

}
