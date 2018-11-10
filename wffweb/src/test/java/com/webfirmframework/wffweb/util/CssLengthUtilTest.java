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
package com.webfirmframework.wffweb.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssLengthUnit;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class CssLengthUtilTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.util.CssLengthUtil#getLengthValueAsPremitiveAndUnit(java.lang.String)}.
     */
    @Test
    public void testGetLengthValueAsPremitiveAndUnit() {
        
        assertEquals(2, CssLengthUtil.getLengthValueAsPremitiveAndUnit("2px").length);
        assertEquals(5.0F, CssLengthUtil.getLengthValueAsPremitiveAndUnit("5px")[0]);
        //assertEquals(Float.TYPE, CssLengthUtil.getLengthValueAsPremitiveAndUnit("5px")[0].getClass()); // is useless due to auto-boxing
        assertEquals(CssLengthUnit.PX, CssLengthUtil.getLengthValueAsPremitiveAndUnit("5px")[1]);
        assertEquals(1, CssLengthUtil.getLengthValueAsPremitiveAndUnit("2").length);
        assertEquals(15F, CssLengthUtil.getLengthValueAsPremitiveAndUnit("15")[0]);
        
        assertEquals(25F, CssLengthUtil.getLengthValueAsPremitiveAndUnit(" 25 % ")[0]);
        assertEquals(CssLengthUnit.PER, CssLengthUtil.getLengthValueAsPremitiveAndUnit(" 25 % ")[1]);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.util.CssLengthUtil#getLengthValueAndUnit(java.lang.String)}.
     */
    @Test
    public void testGetLengthValueAndUnit() {
        
        assertEquals(2, CssLengthUtil.getLengthValueAndUnit("2px").length);
        assertEquals(5.0F, CssLengthUtil.getLengthValueAndUnit("5px")[0]);
        //assertEquals(Float.class, CssLengthUtil.getLengthValueAndUnit("5px")[0].getClass());
        assertEquals(CssLengthUnit.PX, CssLengthUtil.getLengthValueAndUnit("5px")[1]);
        assertEquals(1, CssLengthUtil.getLengthValueAndUnit("2").length);
        assertEquals(15F, CssLengthUtil.getLengthValueAndUnit("15")[0]);
        
        assertEquals(25F, CssLengthUtil.getLengthValueAndUnit(" 25 % ")[0]);
        assertEquals(CssLengthUnit.PER, CssLengthUtil.getLengthValueAndUnit(" 25 % ")[1]);
    }

}
