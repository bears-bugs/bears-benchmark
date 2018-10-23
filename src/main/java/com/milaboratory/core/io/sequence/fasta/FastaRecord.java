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
package com.milaboratory.core.io.sequence.fasta;

import com.milaboratory.core.sequence.Sequence;

/**
 * Represents single fasta record
 *
 * @param <S> sequence type
 */
public final class FastaRecord<S extends Sequence<S>> {
    private final long id;
    private final String description;
    private final S sequence;

    /**
     * General constructor.
     *
     * @param id          serial id of record
     * @param description description string
     * @param sequence    sequence
     */
    public FastaRecord(long id, String description, S sequence) {
        this.id = id;
        this.description = description;
        this.sequence = sequence;
    }

    /**
     * Returns serial id of fasta record (zero-based)
     *
     * @return serial id of fasta record
     */
    public long getId() {
        return id;
    }

    /**
     * Returns description string (text that goes after ">")
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return sequence (main content of fasta record)
     *
     * @return sequence
     */
    public S getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FastaRecord)) return false;

        FastaRecord<?> that = (FastaRecord<?>) o;

        if (id != that.id) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return sequence.equals(that.sequence);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + sequence.hashCode();
        return result;
    }
}
