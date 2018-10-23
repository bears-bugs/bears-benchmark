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

import java.util.Arrays;

/**
 * Array list of primitive integers.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class IntArrayList implements java.io.Serializable {

    int[] data;
    int size = 0;

    public IntArrayList() {
        data = new int[10];
    }

    public IntArrayList(int initialCapacity) {
        data = new int[initialCapacity];
    }

    public IntArrayList(int... data) {
        this.data = data;
        size = data.length;
    }

    private IntArrayList(int[] data, int size) {
        this.data = data;
        this.size = size;
    }

    public void clear() {
        size = 0;
    }

    public void ensureCapacity(int minCapacity) {
        int oldCapacity = data.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = (oldCapacity * 3) / 2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            data = Arrays.copyOf(data, newCapacity);
        }
    }

    public void add(int num) {
        ensureCapacity(size + 1);
        data[size++] = num;
    }

    public void add(int position, int num) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException();
        ensureCapacity(size + 1);
        System.arraycopy(data, position, data, position + 1,
                size - position);
        data[position] = num;
        size++;
    }

    public void addAll(int[] arr) {
        int arrLen = arr.length;
        ensureCapacity(arrLen + size);
        System.arraycopy(arr, 0, data, size, arrLen);
        size += arrLen;
    }

    public void addAll(IntArrayList intArrayList) {
        int arrLen = intArrayList.size();
        ensureCapacity(arrLen + size);
        System.arraycopy(intArrayList.data, 0, data, size, arrLen);
        size += arrLen;
    }

    public void set(int position, int num) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException();
        data[position] = num;
    }

    public void copyFrom(IntArrayList other) {
        clear();
        addAll(other);
    }

    public void sort() {
        Arrays.sort(data, 0, size);
    }

    public void stableSort(IntComparator comparator) {
        int i, j;
        int key;
        for (i = 1; i < size; i++) {
            key = data[i];
            for (j = i; j > 0 && comparator.compare(data[j - 1], key) > 0; j--) {
                data[j] = data[j - 1];
            }
            data[j] = key;
        }
    }

    /**
     * Alias for addAll(int) method.
     *
     * @param value
     */
    public void push(int value) {
        add(value);
    }

    /**
     * Return last element leaving it in stack.
     *
     * @return last element leaving it in stack
     */
    public int peek() {
        return data[size - 1];
    }

    /**
     * Removes the object at the top of this stack and returns that object as the value of this function.
     *
     * @return pop
     */
    public int pop() {
        return data[--size];
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position. The number of components
     * copied is equal to the length argument.
     *
     * @param src       - the source array.
     * @param fromIndex - starting position in the source array.
     * @param length    - the number of array elements to be copied.
     */
    public void add(int[] src, int fromIndex, int length) {
        ensureCapacity(size + length);
        System.arraycopy(src, fromIndex, data, size, length);
        size = size + length;
    }

    public void reverse() {
        reverse(0, size);
    }

    public void reverse(int from, int to) {
        int tmp, index1, index2, length = to - from;
        for (int i = (length + 1) / 2 - 1; i >= 0; --i) {
            tmp = data[index1 = from + i];
            data[index1] = data[index2 = from + length - i - 1];
            data[index2] = tmp;
        }
    }

    public int get(int i) {
        if (i < 0 || i >= size)
            throw new IndexOutOfBoundsException("Size: " + size + " index: " + i);
        return data[i];
    }

    public boolean replaceFirst(int from, int to) {
        for (int i = 0; i < size; ++i)
            if (data[i] == from) {
                data[i] = to;
                return true;
            }
        return false;
    }

    public boolean replaceAll(int from, int to) {
        boolean replaced = false;
        for (int i = 0; i < size; ++i)
            if (data[i] == from) {
                data[i] = to;
                replaced = true;
            }
        return replaced;
    }

    public int last() {
        return data[size - 1];
    }

    public int[] toArray() {
        return Arrays.copyOfRange(data, 0, size);
    }

    public int size() {
        return size;
    }

    public boolean contains(int value) {
        return indexOf(value) >= 0;
    }

    public int indexOf(int value) {
        for (int i = 0; i < size; ++i)
            if (data[i] == value)
                return i;
        return -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public IntArrayList clone() {
        return new IntArrayList(Arrays.copyOf(data, data.length), size);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IntArrayList other = (IntArrayList) obj;
        if (this.size != other.size)
            return false;
        for (int i = 0; i < size; ++i)
            if (this.data[i] != other.data[i])
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        for (int i = 0; i < size; ++i)
            hash = 31 * hash + data[i];
        return hash;
    }

    @Override
    public String toString() {
        int iMax = size() - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(data[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static int[] getArrayReference(IntArrayList list) {
        return list.data;
    }

    public interface IntComparator {
        int compare(int i1, int i2);
    }
}
