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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class ObjectUtilTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.util.ObjectUtil#isEqual(java.lang.Object[])}.
     */
    @Test
    public void testIsEqualObjectArray() {
        assertFalse(ObjectUtil.isEqual(null));
        assertFalse(ObjectUtil.isEqual(new Object[]{null}));
        assertFalse(ObjectUtil.isEqual(new Object[]{"d"}));
        assertFalse(ObjectUtil.isEqual("dfd"));
        assertTrue(ObjectUtil.isEqual(null, null, null));
        assertFalse(ObjectUtil.isEqual("d", null, null));
        assertFalse(ObjectUtil.isEqual(null, "dfd", null));
        assertFalse(ObjectUtil.isEqual(null, null, "dfd"));
        String obj1 = "hi";
        String obj2 = "hi";
        String obj3 = "hi";
        assertTrue(ObjectUtil.isEqual(obj1, obj2, obj3));
        obj2 = "hidfd";
        assertFalse(ObjectUtil.isEqual(obj1, obj2, obj3));
        obj2 = obj1;
        obj3 = "dfdf";
        assertFalse(ObjectUtil.isEqual(obj1, obj2, obj3));
        obj3 = obj1;
        obj1 = "dfdasdfsdf";
        assertFalse(ObjectUtil.isEqual(obj1, obj2, obj3));
        obj3 = "dfdfdfdfddddddddd";
        assertFalse(ObjectUtil.isEqual(obj1, obj2, obj3));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.util.ObjectUtil#isEqual(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testIsEqualObjectObject() {
        assertTrue(ObjectUtil.isEqual(null, null));
        assertFalse(ObjectUtil.isEqual("df", null));
        assertFalse(ObjectUtil.isEqual(null, "df"));
        String obj1 = "hi";
        String obj2 = "hi";
        assertTrue(ObjectUtil.isEqual(obj1, obj2));
        assertTrue(ObjectUtil.isEqual("hi", "hi"));
    }

}
