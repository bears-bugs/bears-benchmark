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

import com.milaboratory.core.sequence.NSequenceWithQuality;
import com.milaboratory.util.SingleIterator;

import java.util.Iterator;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class SingleReadImpl implements SingleRead {
    final long id;
    final NSequenceWithQuality sequenceWithQuality;
    final String description;

    public SingleReadImpl(long id, NSequenceWithQuality sequenceWithQuality, String description) {
        this.id = id;
        this.sequenceWithQuality = sequenceWithQuality;
        this.description = description;
    }

    @Override
    public int numberOfReads() {
        return 1;
    }

    @Override
    public SingleRead getRead(int i) {
        if (i != 0)
            throw new IndexOutOfBoundsException();
        return this;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public NSequenceWithQuality getData() {
        return sequenceWithQuality;
    }

    @Override
    public Iterator<SingleRead> iterator() {
        return new SingleIterator<>((SingleRead) this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (!(o instanceof SingleRead))
            return false;
        SingleRead oth = (SingleRead) o;
        return id == oth.getId()
                && sequenceWithQuality.equals(oth.getData())
                && description.equals(oth.getDescription());
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + sequenceWithQuality.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "@" + description + "\n" + sequenceWithQuality.getSequence() + "\n+\n" + sequenceWithQuality.getQuality();
    }
}
