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
package com.milaboratory.core.io.sequence;

import com.milaboratory.core.sequence.MultiNSequenceWithQuality;
import com.milaboratory.util.ArrayIterator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class MultiRead implements SequenceRead, java.io.Serializable {
    final SingleRead[] data;

    public MultiRead(SingleRead[] data) {
        if (data.length == 0)
            throw new IllegalArgumentException("Empty data.");
        long id = data[0].getId();
        for (int i = 1; i < data.length; ++i)
            if (data[i].getId() != id)
                throw new IllegalArgumentException("Incompatible read ids.");
        this.data = data;
    }

    @Override
    public int numberOfReads() {
        return data.length;
    }

    @Override
    public SingleRead getRead(int i) {
        return data[i];
    }

    @Override
    public long getId() {
        return data[0].getId();
    }

    @Override
    public Iterator<SingleRead> iterator() {
        return new ArrayIterator<>(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiRead that = (MultiRead) o;

        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
