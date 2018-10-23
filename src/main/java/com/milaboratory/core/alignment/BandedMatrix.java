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
package com.milaboratory.core.alignment;

import java.util.Arrays;

/**
 * BandedMatrix - class which used to store alignment matrix for {@link BandedLinearAligner}.
 * <p>It stores only main diagonal values as well as values of diagonal which are closed to main one (it's defined by
 * #rowFactor and #columnDelta)</p>
 */
public final class BandedMatrix implements java.io.Serializable {
    /**
     * Value of empty cell
     */
    public static final int DEFAULT_VALUE = Integer.MIN_VALUE / 2;
    /**
     * Main alignment matrix
     */
    private final int[] matrix;
    /**
     * Row length
     */
    private final int rowFactor;
    /**
     * Negative offset value of first row
     */
    private final int columnDelta;

    public BandedMatrix(CachedIntArray cachedArray, int size1, int size2, int width) {
        if (width >= size1)
            width = size1 - 1;
        if (width >= size2)
            width = size2 - 1;
        this.rowFactor = 2 * width + Math.abs(size2 - size1);
        this.columnDelta = -Math.min(0, size2 - size1) + width;
        this.matrix = cachedArray.get((size1 - 1) * rowFactor + columnDelta + size2);
    }

    public int getRowFactor() {
        return rowFactor;
    }

    public int getColumnDelta() {
        return columnDelta;
    }

    public int get(int i, int j) {
        if (j - i < -columnDelta || j - i > rowFactor - columnDelta)
            return DEFAULT_VALUE;

        return matrix[i * rowFactor + j + columnDelta];
    }

    public void set(int i, int j, int value) {
        assert !(j - i < -columnDelta || j - i > rowFactor - columnDelta)
                : String.format("i: %s, j: %s, columnDelta: %s, rowFactor: %s", i, j, columnDelta, rowFactor);

        matrix[i * rowFactor + j + columnDelta] = value;
    }

    @Override
    public String toString() {
        return Arrays.toString(matrix);
    }
}
