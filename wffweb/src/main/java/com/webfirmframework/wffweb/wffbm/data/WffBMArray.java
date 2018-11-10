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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.webfirmframework.wffweb.WffRuntimeException;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;
import com.webfirmframework.wffweb.util.data.NameValue;

/**
 * The java array object representation for JavaScript array. <br>
 * Sample code :- <br>
 *
 * <pre>
 * WffBMObject bmObject = new WffBMObject();
 *
 * WffBMArray stringArray = new WffBMArray(BMValueType.STRING);
 * stringArray.add("array value 1");
 * stringArray.add("array value 2");
 *
 * bmObject.put("stringArray", BMValueType.BM_ARRAY, stringArray);
 *
 * WffBMArray numberArray = new WffBMArray(BMValueType.NUMBER);
 * numberArray.add(555);
 * numberArray.add(5);
 * numberArray.add(55);
 *
 * bmObject.put("numberArray", BMValueType.BM_ARRAY, numberArray);
 *
 * <code>// to store bytes in an array use WffBMByteArray</code>
 * WffBMByteArray byteArray = new WffBMByteArray();
 * byteArray.write("こんにちは WFFWEB".getBytes("UTF-8"));
 *
 * bmObject.put("byteArray", BMValueType.BM_BYTE_ARRAY, byteArray);
 *
 * WffBMArray booleanArray = new WffBMArray(BMValueType.BOOLEAN);
 * booleanArray.add(true);
 * booleanArray.add(false);
 * booleanArray.add(true);
 *
 * bmObject.put("booleanArray", BMValueType.BM_ARRAY, booleanArray);
 *
 * WffBMArray regexArray = new WffBMArray(BMValueType.REG_EXP);
 * regexArray.add("[w]");
 * regexArray.add("[f]");
 * regexArray.add("[f]");
 *
 * bmObject.put("regexArray", BMValueType.BM_ARRAY, regexArray);
 *
 * WffBMArray funcArray = new WffBMArray(BMValueType.FUNCTION);
 * funcArray.add("function(arg) {console.log(arg);}");
 * funcArray.add("function(arg1) {console.log(arg1);}");
 * funcArray.add("function(arg2) {console.log(arg2);}");
 *
 * bmObject.put("funcArray", BMValueType.BM_ARRAY, funcArray);
 *
 * WffBMArray nullArray = new WffBMArray(BMValueType.NULL);
 * nullArray.add(null);
 * nullArray.add(null);
 * nullArray.add(null);
 *
 * bmObject.put("nullArray", BMValueType.BM_ARRAY, nullArray);
 *
 * WffBMArray undefinedArray = new WffBMArray(BMValueType.UNDEFINED);
 * undefinedArray.add(null);
 * undefinedArray.add(null);
 * undefinedArray.add(null);
 *
 * bmObject.put("undefinedArray", BMValueType.BM_ARRAY, undefinedArray);
 *
 * WffBMArray arrayArray = new WffBMArray(BMValueType.BM_ARRAY);
 * arrayArray.add(funcArray);
 * arrayArray.add(funcArray);
 * arrayArray.add(funcArray);
 *
 * bmObject.put("arrayArray", BMValueType.BM_ARRAY, arrayArray);
 *
 * WffBMArray objectArray = new WffBMArray(BMValueType.BM_OBJECT);
 * objectArray.add(bmObject.clone());
 * objectArray.add(bmObject.clone());
 * objectArray.add(bmObject.clone());
 *
 * bmObject.put("objectArray", BMValueType.BM_ARRAY, objectArray);
 * </pre>
 *
 * @author WFF
 * @see WffBMByteArray
 * @see WffBMObject
 */
public class WffBMArray extends LinkedList<Object> implements WffBMData {

    private static final long serialVersionUID = 1L;

    private boolean outer;

    private BMValueType valueType;

    public WffBMArray(final BMValueType valueType) {
        this.valueType = valueType;
    }

    public WffBMArray(final BMValueType valueType, final boolean outer) {
        this.valueType = valueType;
        this.outer = outer;
    }

    public WffBMArray(final byte[] bmArrayBytes) {
        try {
            valueType = initWffBMObject(bmArrayBytes, true);
        } catch (final UnsupportedEncodingException e) {
            throw new WffRuntimeException("Invalid Wff BM Array bytes", e);
        }
    }

    public WffBMArray(final byte[] bmArrayBytes, final boolean outer) {
        try {
            valueType = initWffBMObject(bmArrayBytes, outer);
        } catch (final UnsupportedEncodingException e) {
            throw new WffRuntimeException("Invalid Wff BM Array bytes", e);
        }
    }

    private BMValueType initWffBMObject(final byte[] bmArrayBytes,
            final boolean outer) throws UnsupportedEncodingException {

        if (bmArrayBytes.length == 0 && !outer) {
            // if the inner WffBMArray is an empty array then the bmArrayBytes
            // will be an empty array. Then we can say its type is null in the
            // perceptive of Java it is BMValueType is null
            // because there is no type for an empty array value in JavaSript
            // for JavaScript null type there is BMValueType.NULL
            return null;
        }

        final List<NameValue> bmObject = WffBinaryMessageUtil.VERSION_1
                .parse(bmArrayBytes);

        final Iterator<NameValue> iterator = bmObject.iterator();
        if (iterator.hasNext()) {

            if (outer) {
                final NameValue typeNameValue = iterator.next();
                if (typeNameValue.getName()[0] == BMType.ARRAY.getType()) {
                    this.outer = true;
                } else {
                    throw new WffRuntimeException(
                            "Not a valid Wff BM Array bytes");
                }
            }

            if (iterator.hasNext()) {
                final NameValue nameValue = iterator.next();
                final byte valueType = nameValue.getName()[0];
                final byte[][] values = nameValue.getValues();

                if (valueType == BMValueType.STRING.getType()) {

                    for (final byte[] value : values) {
                        this.add(new String(value, "UTF-8"));
                    }
                } else if (valueType == BMValueType.NUMBER.getType()) {

                    for (final byte[] value : values) {
                        final double doubleValue = ByteBuffer.wrap(value)
                                .getDouble(0);

                        this.add(doubleValue);
                    }

                } else if (valueType == BMValueType.UNDEFINED.getType()) {

                    for (@SuppressWarnings("unused")
                    final byte[] value : values) {
                        this.add(null);
                    }

                } else if (valueType == BMValueType.NULL.getType()) {

                    for (@SuppressWarnings("unused")
                    final byte[] value : values) {
                        this.add(null);
                    }

                } else if (valueType == BMValueType.BOOLEAN.getType()) {
                    for (final byte[] value : values) {
                        this.add(value[0] == 1);
                    }
                } else if (valueType == BMValueType.BM_OBJECT.getType()) {

                    for (final byte[] value : values) {
                        this.add(new WffBMObject(value, false));
                    }

                } else if (valueType == BMValueType.BM_ARRAY.getType()) {

                    for (final byte[] value : values) {

                        this.add(new WffBMArray(value, false));
                    }

                } else if (valueType == BMValueType.REG_EXP.getType()) {
                    for (final byte[] value : values) {
                        this.add(new String(value, "UTF-8"));
                    }
                } else if (valueType == BMValueType.FUNCTION.getType()) {

                    for (final byte[] value : values) {
                        this.add(new String(value, "UTF-8"));
                    }
                } else if (valueType == BMValueType.BM_BYTE_ARRAY.getType()) {
                    for (final byte[] value : values) {
                        this.add(new WffBMByteArray(value, false));
                    }
                } else if (valueType == BMValueType.INTERNAL_BYTE.getType()) {
                    throw new WffRuntimeException(
                            "BMValueType.BYTE is only for internal use, use WffBMByteArray for row bytes.");
                }

                return BMValueType.getInstanceByType(valueType);
            }
        }

        return null;
    }

    public boolean isOuter() {
        return outer;
    }

    public void setOuter(final boolean outer) {
        this.outer = outer;
    }

    public byte[] build() {
        try {
            return build(outer);
        } catch (final UnsupportedEncodingException e) {
            throw new WffRuntimeException("Could not build wff bm array bytes",
                    e);
        }
    }

    @Override
    public byte[] build(final boolean outer)
            throws UnsupportedEncodingException {

        final Deque<NameValue> nameValues = new ArrayDeque<>(outer ? 2 : 1);

        if (outer) {
            final NameValue typeNameValue = new NameValue();
            typeNameValue.setName(new byte[] { BMType.ARRAY.getType() });
            nameValues.add(typeNameValue);
        }

        final NameValue nameValue = new NameValue();
        final byte valueType = this.valueType.getType();
        nameValue.setName(valueType);

        nameValues.add(nameValue);

        final byte[][] values = new byte[size()][0];
        nameValue.setValues(values);

        if (valueType == BMValueType.STRING.getType()) {

            int count = 0;
            for (final Object eachValue : this) {
                final String value = (String) eachValue;
                values[count] = value.getBytes("UTF-8");
                count++;
            }
        } else if (valueType == BMValueType.NUMBER.getType()) {

            int count = 0;
            for (final Object eachValue : this) {
                final Number value = (Number) eachValue;
                values[count] = WffBinaryMessageUtil
                        .getOptimizedBytesFromDouble(value.doubleValue());
                count++;
            }

        } else if (valueType == BMValueType.UNDEFINED.getType()) {

            for (int i = 0; i < size(); i++) {
                values[i] = new byte[] {};
            }

        } else if (valueType == BMValueType.NULL.getType()) {

            for (int i = 0; i < size(); i++) {
                values[i] = new byte[] {};
            }
        } else if (valueType == BMValueType.BOOLEAN.getType()) {
            int count = 0;
            for (final Object eachValue : this) {
                final Boolean value = (Boolean) eachValue;
                final byte[] valueBytes = {
                        (byte) (value.booleanValue() ? 1 : 0) };
                values[count] = valueBytes;
                count++;
            }
        } else if (valueType == BMValueType.BM_OBJECT.getType()) {
            int count = 0;
            for (final Object eachValue : this) {
                final WffBMObject value = (WffBMObject) eachValue;
                values[count] = value.build(false);
                count++;
            }

        } else if (valueType == BMValueType.BM_ARRAY.getType()) {

            int count = 0;
            for (final Object eachValue : this) {
                final WffBMArray value = (WffBMArray) eachValue;
                values[count] = value.build(false);
                count++;
            }

        } else if (valueType == BMValueType.REG_EXP.getType()) {
            int count = 0;
            for (final Object eachValue : this) {
                final String value = (String) eachValue;
                values[count] = value.getBytes("UTF-8");
                count++;
            }
        } else if (valueType == BMValueType.FUNCTION.getType()) {

            int count = 0;
            for (final Object eachValue : this) {
                final String value = (String) eachValue;
                values[count] = value.getBytes("UTF-8");
                count++;
            }
        } else if (valueType == BMValueType.BM_BYTE_ARRAY.getType()) {

            int count = 0;
            for (final Object eachValue : this) {
                @SuppressWarnings("resource")
                final WffBMByteArray value = (WffBMByteArray) eachValue;
                values[count] = value.build(false);
                count++;
            }
        } else if (valueType == BMValueType.INTERNAL_BYTE.getType()) {
            throw new WffRuntimeException(
                    "BMValueType.BYTE is only for internal use, use WffBMByteArray for row bytes.");
        }

        return WffBinaryMessageUtil.VERSION_1
                .getWffBinaryMessageBytes(nameValues);
    }

    /**
     * gets value type of this array only if it contains any value otherwise
     * returns <code>null</code>.
     *
     * @return the value type of this array
     * @since 2.1.0
     * @author WFF
     */
    public BMValueType getValueType() {
        return valueType;
    }

    @Override
    public BMType getBMType() {
        return BMType.ARRAY;
    }
}
