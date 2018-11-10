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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.webfirmframework.wffweb.WffRuntimeException;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;
import com.webfirmframework.wffweb.util.data.NameValue;

/**
 * This a byte array to hold utf-8 bytes. use WffBMByteArray.write to add bytes
 * into this array. <br>
 * Sample :- <br>
 *
 * <pre>
 * WffBMObject bmObject = new WffBMObject();
 *
 * WffBMByteArray byteArray = new WffBMByteArray(); byteArray.write("こんにちは
 * WFFWEB".getBytes("UTF-8"));
 *
 * bmObject.put("byteArray", BMValueType.BM_BYTE_ARRAY, byteArray);
 *
 * </pre>
 *
 * @author WFF
 * @see WffBMArray
 * @see WffBMObject
 */
public class WffBMByteArray extends ByteArrayOutputStream
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean outer;

    private final BMValueType valueType;

    public WffBMByteArray() {
        valueType = BMValueType.INTERNAL_BYTE;
    }

    public WffBMByteArray(final boolean outer) {
        this.outer = outer;
        valueType = BMValueType.INTERNAL_BYTE;
    }

    public WffBMByteArray(final byte[] bmArrayBytes) {
        super(bmArrayBytes.length);
        valueType = BMValueType.INTERNAL_BYTE;
        try {
            initWffBMObject(bmArrayBytes, outer);
        } catch (final IOException e) {
            throw new WffRuntimeException(e.getMessage(), e);
        }
    }

    public WffBMByteArray(final byte[] bmArrayBytes, final boolean outer) {
        super(bmArrayBytes.length);
        valueType = BMValueType.INTERNAL_BYTE;
        try {
            initWffBMObject(bmArrayBytes, outer);
        } catch (final IOException e) {
            throw new WffRuntimeException(e.getMessage(), e);
        }
    }

    public BMValueType getValueType() {
        return valueType;
    }

    public boolean isOuter() {
        return outer;
    }

    public void setOuter(final boolean outer) {
        this.outer = outer;
    }

    private void initWffBMObject(final byte[] bmArrayBytes, final boolean outer)
            throws IOException {

        if (bmArrayBytes.length == 0 && !outer) {
            // if the inner WffBMByteArray is an empty array then the
            // bmArrayBytes
            // will be an empty array.
            return;
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

                if (valueType == BMValueType.INTERNAL_BYTE.getType()) {
                    for (final byte[] value : values) {
                        // the first value will contain all the bytes, but still
                        // kept a loop here.
                        this.write(value);

                    }
                } else {
                    throw new WffRuntimeException(
                            "The array value is not byte type");
                }

            }
        }

    }

    /**
     * @return the wff bm array bytes
     * @since 2.0.0
     * @author WFF
     */
    public byte[] build() {
        return build(outer);
    }

    /**
     * @param outer
     * @return the wff bm array bytes as outer array
     * @since 1.1.5
     * @author WFF
     */
    public byte[] build(final boolean outer) {

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

        final byte[][] values = { toByteArray() };
        nameValue.setValues(values);

        return WffBinaryMessageUtil.VERSION_1
                .getWffBinaryMessageBytes(nameValues);
    }

}
