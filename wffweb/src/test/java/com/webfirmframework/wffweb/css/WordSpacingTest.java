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
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class WordSpacingTest {

    @Test
    public void testWordSpacing() {
        WordSpacing wordSpacing = new WordSpacing();
        assertEquals(WordSpacing.NORMAL, wordSpacing.getCssValue());
    }

    @Test
    public void testWordSpacingString() {
        {
            WordSpacing wordSpacing = new WordSpacing(WordSpacing.INITIAL);
            assertEquals(WordSpacing.INITIAL, wordSpacing.getCssValue());
        }
        {
            WordSpacing wordSpacing = new WordSpacing("50px");
            assertEquals("50px", wordSpacing.getCssValue());
        }
    }

    @Test
    public void testWordSpacingWordSpacing() {
        WordSpacing wordSpacing = new WordSpacing("50px");
        WordSpacing wordSpacing1 = new WordSpacing(wordSpacing);
        assertEquals("50px", wordSpacing1.getCssValue());
    }

    @Test
    public void testWordSpacingFloat() {
        WordSpacing wordSpacing = new WordSpacing(75);
        assertEquals("75.0%", wordSpacing.getCssValue());
    }

    @Test
    public void testWordSpacingFloatCssLengthUnit() {
        {
            WordSpacing wordSpacing = new WordSpacing(75, CssLengthUnit.PER);
            assertEquals("75.0%", wordSpacing.getCssValue());
            assertEquals(CssLengthUnit.PER, wordSpacing.getUnit());
            assertTrue(wordSpacing.getValue() == 75);
        }
        {
            WordSpacing wordSpacing = new WordSpacing(75, CssLengthUnit.CH);
            assertEquals("75.0ch", wordSpacing.getCssValue());
        }
        {
            WordSpacing wordSpacing = new WordSpacing(75, CssLengthUnit.EM);
            assertEquals("75.0em", wordSpacing.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WordSpacing wordSpacing = new WordSpacing();
            wordSpacing.setPercent(75);
            assertEquals("75.0%", wordSpacing.getCssValue());
            assertEquals(CssLengthUnit.PER, wordSpacing.getUnit());
            assertTrue(wordSpacing.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WordSpacing wordSpacing = new WordSpacing();
        assertEquals(CssNameConstants.WORD_SPACING, wordSpacing.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setAsInherit();
        assertEquals(WordSpacing.INHERIT, wordSpacing.getCssValue());
        wordSpacing.setAsNormal();
        assertEquals(WordSpacing.NORMAL, wordSpacing.getCssValue());
    }

    @Test
    public void testToString() {
        WordSpacing wordSpacing = new WordSpacing(75, CssLengthUnit.EM);
        assertEquals(wordSpacing.getCssName()+": 75.0em", wordSpacing.toString());
    }

    @Test
    public void testGetValue() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setPercent(75);
        assertTrue(wordSpacing.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setPercent(75);
        assertEquals(CssLengthUnit.PER, wordSpacing.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setCssValue("75%");
        assertEquals("75%", wordSpacing.getCssValue());
        assertEquals(CssLengthUnit.PER, wordSpacing.getUnit());
        assertTrue(wordSpacing.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setAsInitial();
        assertEquals(WordSpacing.INITIAL, wordSpacing.getCssValue());
       assertNull(wordSpacing.getValue());
       assertNull(wordSpacing.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setAsInherit();
        assertEquals(WordSpacing.INHERIT, wordSpacing.getCssValue());
       assertNull(wordSpacing.getValue());
       assertNull(wordSpacing.getUnit());
    }

    @Test
    public void testSetAsNormal() {
        WordSpacing wordSpacing = new WordSpacing();
        wordSpacing.setAsInherit();
        wordSpacing.setAsNormal();
        assertEquals(WordSpacing.NORMAL, wordSpacing.getCssValue());
       assertNull(wordSpacing.getValue());
       assertNull(wordSpacing.getUnit());
    }

}
