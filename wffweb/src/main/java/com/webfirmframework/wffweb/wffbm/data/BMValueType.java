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
package com.webfirmframework.wffweb.wffbm.data;

/**
 * BM Object/Array value type
 *
 * @author WFF
 *
 */
public enum BMValueType {

    // this order should not be changed
    /**
     * to initialize String value
     */
    STRING,

    /**
     * It can be int, float, long, double or any 64 bit floating point number
     */
    NUMBER,

    /**
     * to assign unidentified in JavaScript value, the value can be null for
     * this data type.
     */
    UNDEFINED,

    /**
     * to initialize null value
     */
    NULL,

    /**
     * to initialize boolean value
     */
    BOOLEAN,

    /**
     * to initialize another {@code WffBMObject}
     */
    BM_OBJECT,

    /**
     * to initialize another {@code WffBMArray}
     */
    BM_ARRAY,

    /**
     * to initialize reg expression
     */
    REG_EXP,

    /**
     * to initialize function, eg: function(arg) {alert(arg);}
     *
     * <pre>
     * <code>
     * WffBMObject bmObject = new WffBMObject();
     * bmObject.put("testMeFun", BMValueType.FUNCTION, "function(arg) {alert(arg);}");
     * so the usage would be <i>jsObject.testMeFun("Hello world");</i>
     * </code>
     * </pre>
     */
    FUNCTION,

    /**
     * to initialize binary data. In java it's byte data type and in JavaScript
     * it's Int8Array.
     */
    BM_BYTE_ARRAY,

    /**
     * Only for internal use not for development purpose. If value is a byte.
     */
    INTERNAL_BYTE;

    private byte type;

    private BMValueType() {
        type = (byte) ordinal();
    }

    public byte getType() {
        return type;
    }

    /**
     * @param type
     * @return the {@code BMValueType} instance by type
     * @since 2.0.0
     * @author WFF
     */
    public static BMValueType getInstanceByType(final byte type) {
        return BMValueType.values()[type];
    }
}
