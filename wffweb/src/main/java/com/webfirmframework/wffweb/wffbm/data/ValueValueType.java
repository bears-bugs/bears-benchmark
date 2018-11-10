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

import java.io.Serializable;

public class ValueValueType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Object value;

    private byte valueTypeByte;

    public ValueValueType(final String name, final byte valueTypeByte,
            final Object value) {
        super();
        this.name = name;
        this.valueTypeByte = valueTypeByte;
        this.value = value;
    }

    public ValueValueType(final String name, final BMValueType valueType,
            final Object value) {
        super();
        this.name = name;
        valueTypeByte = valueType.getType();
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public BMValueType getValueType() {
        return BMValueType.values()[valueTypeByte];
    }

    public byte getValueTypeByte() {
        return valueTypeByte;
    }

    public void setValueTypeByte(final byte valueTypeByte) {
        this.valueTypeByte = valueTypeByte;
    }

    public void setValueType(final BMValueType valueType) {
        valueTypeByte = valueType.getType();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValueValueType other = (ValueValueType) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
