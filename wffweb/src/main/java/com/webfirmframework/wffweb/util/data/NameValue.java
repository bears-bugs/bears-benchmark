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
package com.webfirmframework.wffweb.util.data;

import java.io.Serializable;

/**
 * A data class which can hold name-values pair. The {@code name} property is an
 * array of bytes where as the {@code values} property is an array of byte
 * array.
 *
 * @author WFF
 *
 */
public class NameValue implements Serializable {

    private static final long serialVersionUID = 1_1_0L;

    private byte[] name = {};

    private byte[][] values = {};

    public NameValue() {
    }

    /**
     * @param name
     *            an array of bytes
     * @param values
     *            an array of bytes array, i.e. a two dimensional array of
     *            bytes.
     * @author WFF
     */
    public NameValue(final byte[] name, final byte[][] values) {
        super();
        this.name = name;
        this.values = values;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(final byte... name) {
        this.name = name;
    }

    public byte[][] getValues() {
        return values;
    }

    public void setValues(final byte[]... values) {
        this.values = values;
    }

}
