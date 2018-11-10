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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ColumnSpanTest {

    @Test
    public void testCssName() {
        assertEquals("column-span", ColumnSpan.ALL.getCssName());
    }

    @Test
    public void testCssValue() {
        assertEquals("all", ColumnSpan.ALL.getCssValue());
    }

    @Test
    public void testIsValid() {
        assertTrue(ColumnSpan.isValid("1"));
        assertTrue(ColumnSpan.isValid("all"));
        assertTrue(ColumnSpan.isValid("initial"));
        assertTrue(ColumnSpan.isValid("inherit"));

        assertTrue(ColumnSpan.isValid("1".toUpperCase()));
        assertTrue(ColumnSpan.isValid("all".toUpperCase()));
        assertTrue(ColumnSpan.isValid("initial".toUpperCase()));
        assertTrue(ColumnSpan.isValid("inherit".toUpperCase()));

    }

    @Test
    public void testIsValidFalse() {
        assertFalse(ColumnSpan.isValid("1df"));
        assertFalse(ColumnSpan.isValid("adfll"));
        assertFalse(ColumnSpan.isValid("inhdferit"));
    }

}
