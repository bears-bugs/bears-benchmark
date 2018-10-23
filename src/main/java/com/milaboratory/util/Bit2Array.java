/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public final class Bit2Array implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    byte[] data;
    int size;

    public Bit2Array(int length) {
        this.size = length;
        data = new byte[(length + 3) >> 2];
    }

    Bit2Array(int size, byte[] data) {
        //if (data.length != ((size + 3) >> 2))
        //    throw new IllegalArgumentException();
        this.size = size;
        this.data = data;
    }

    public int size() {
        return size;
    }

    public int get(int index) {
        return (data[index >> 2] >>> ((index & 3) << 1)) & 0x3;
    }

    public void set(int index, int value) {
        data[index >> 2] &= ~(0x3 << ((index & 3) << 1));
        data[index >> 2] |= (value & 0x3) << ((index & 3) << 1);
    }

    public Bit2Array clone() {
        return new Bit2Array(size, Arrays.copyOf(data, data.length));
    }

    public Bit2Array extend(int size) {
        if (size < this.size)
            throw new IllegalArgumentException();
        return new Bit2Array(size, Arrays.copyOf(data, (size + 3) >> 2));
    }

    public byte[] toByteArray() {
        byte[] data = new byte[size];
        for (int i = 0; i < size; ++i)
            data[i] = (byte) get(i);
        return data;
    }

    public void copyFrom(Bit2Array other, int otherOffset, int thisOffset, int length) {
        if (thisOffset < 0 || thisOffset + length > size ||
                otherOffset < 0 || otherOffset + length > other.size)
            throw new IndexOutOfBoundsException();

        //TODO optimize
        for (int i = 0; i < length; ++i)
            set(thisOffset + i, other.get(otherOffset + i));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.data);
        hash = 47 * hash + this.size;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Bit2Array other = (Bit2Array) obj;
        if (this.size != other.size)
            return false;
        if (!Arrays.equals(this.data, other.data))
            return false;
        return true;
    }

    //TODO optimize
    public Bit2Array getRange(int from, int to) {
        if (from < 0 || (from >= size && size != 0)
                || to < from || to > size)
            throw new IndexOutOfBoundsException("from=" + from + ", to=" + to);

        Bit2Array ret = new Bit2Array(to - from);
        int i = 0;
        for (int j = from; j < to; ++j, ++i)
            ret.set(i, get(j));
        return ret;
    }

    public void writeTo(DataOutput output) throws IOException {
        output.writeInt(size);
        output.write(data);
    }

    public static Bit2Array readFrom(DataInput input) throws IOException {
        int size = input.readInt();
        byte[] buf = new byte[(size + 3) >> 2];
        input.readFully(buf);
        return new Bit2Array(size, buf);
    }

    /*public static Bit2Array wrap(byte[] data, int size) {
        return new Bit2Array(size, data);
    }*/

    public static byte[] extractRawDataArray(Bit2Array array) {
        return array.data;
    }

    public static Bit2Array construct(int size, byte[] data) {
        return new Bit2Array(size, data);
    }
}
