/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.core.sequence.provider;

import com.milaboratory.core.Range;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.util.RangeMap;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CachedSequenceProvider<S extends Sequence<S>> implements SequenceProvider<S> {
    final Alphabet<S> alphabet;
    final RangeMap<S> sequences = new RangeMap<>();
    final SequenceProvider<S> provider;

    /**
     * Cached sequence provider wrapping specified sequence provider.
     *
     * @param alphabet alphabet
     * @param provider provider to cache sequences from
     */
    public CachedSequenceProvider(Alphabet<S> alphabet, SequenceProvider<S> provider) {
        this.alphabet = alphabet;
        this.provider = provider;
    }

    /**
     * Cached sequence provider without underlying sequence provider, with automatically inferred provider size.
     *
     * @param alphabet            alphabet
     * @param missingErrorMessage error message for missing sequence
     */
    public CachedSequenceProvider(Alphabet<S> alphabet, String missingErrorMessage) {
        this(alphabet, new NoProvider<S>(-1, missingErrorMessage));
    }

    /**
     * Cached sequence provider without underlying sequence provider, with fixed provider size.
     *
     * @param alphabet            alphabet
     * @param size                size
     * @param missingErrorMessage error message for missing sequence
     */
    public CachedSequenceProvider(Alphabet<S> alphabet, int size, String missingErrorMessage) {
        this(alphabet, new NoProvider<S>(size, missingErrorMessage));
    }

    /**
     * Cached sequence provider without underlying sequence provider, with fixed provider size.
     *
     * @param alphabet alphabet
     * @param size     size
     */
    public CachedSequenceProvider(Alphabet<S> alphabet, int size) {
        this(alphabet, new NoProvider<S>(size));
    }

    /**
     * Cached sequence provider without underlying sequence provider, with automatically inferred provider size.
     *
     * @param alphabet alphabet
     */
    public CachedSequenceProvider(Alphabet<S> alphabet) {
        this(alphabet, new NoProvider<S>(-1));
    }

    public Map.Entry<Range, S> ensureEntry(Range range) {
        if (range.isReverse())
            throw new IllegalArgumentException("Don't support inverse ranges");

        Range direct = range.isReverse() ? range.inverse() : range;

        Map.Entry<Range, S> entry = sequences.findContaining(direct);
        if (entry != null)
            return entry;

        List<Map.Entry<Range, S>> allIntersecting = sequences.findAllIntersectingOrTouching(range);
        int resFrom = range.getFrom(), resTo = range.getTo();
        //,
        //reqFrom = range.getFrom(), reqTo = range.getTo();

        if (!allIntersecting.isEmpty()) {
            Range tmp = allIntersecting.get(0).getKey();

            if (tmp.containsBoundary(resFrom)) {
                resFrom = Math.min(resFrom, tmp.getFrom());
                //reqFrom = Math.max(reqFrom, tmp.getTo());
            }

            tmp = allIntersecting.get(allIntersecting.size() - 1).getKey();

            if (tmp.containsBoundary(resTo)) {
                resTo = Math.max(resTo, tmp.getTo());
                //reqTo = Math.min(reqTo, tmp.getFrom());
            }
        }

        // Depends on requesting strategy

        Range rr = new Range(resFrom, resTo);
        S seq = provider.getRegion(rr);
        for (Map.Entry<Range, S> e : allIntersecting) {
            // Checking
            int length = e.getKey().length();
            S s = e.getValue();
            for (int i = 0, j = e.getKey().getFrom() - rr.getFrom(); i < length; ++i, ++j)
                if (seq.codeAt(j) != s.codeAt(i))
                    throw new IllegalStateException("Inconsistent sequence returned by provider.");
            sequences.remove(e.getKey());
        }

        sequences.put(rr, seq);

        return new AbstractMap.SimpleEntry<>(rr, seq);
    }

    public Set<Map.Entry<Range, S>> entrySet() {
        return sequences.entrySet();
    }

    @Override
    public int size() {
        if (provider instanceof NoProvider) {
            int s = provider.size();
            if (s >= 0)
                return s;
            else // s == -1 => automatically infer size from rangeMap
                if (sequences.isEmpty())
                    throw new IllegalArgumentException(((NoProvider) provider).errorMessage);
                else
                    return sequences.enclosingRange().getUpper(); // last cached position
        } else if (provider instanceof SequenceProviderUtils.LazySequenceProvider)
            if (sequences.isEmpty())
                return provider.size();
            else
                return sequences.enclosingRange().getUpper(); // last cached position
        else
            return provider.size();
    }

    public S getRegion(Range range) {
        if (range.isEmpty())
            return alphabet.getEmptySequence();
        Map.Entry<Range, S> entry = range.isReverse() ? ensureEntry(range.inverse()) : ensureEntry(range);
        return entry.getValue().getRange(entry.getKey().getRelativeRangeOf(range));
    }

    public void setRegion(Range range, S seq) {
        if ((provider instanceof NoProvider && provider.size() > 0) ||
                (provider instanceof SequenceProviderUtils.LazySequenceProvider && ((SequenceProviderUtils.LazySequenceProvider) provider).isInitialized()) ||
                (!(provider instanceof NoProvider) && !(provider instanceof SequenceProviderUtils.LazySequenceProvider))) {
            int providerSize = provider.size();
            if (providerSize >= 0 && range.getUpper() > providerSize)
                throw new IllegalArgumentException("Trying to set sequence outside available range.");
        }

        Map.Entry<Range, S> containing = sequences.findContaining(range);
        if (containing != null) {
            for (int i = 0, j = range.getFrom() - containing.getKey().getFrom(); i < seq.size(); ++i, ++j)
                if (seq.codeAt(i) != containing.getValue().codeAt(j))
                    throw new IllegalStateException("Inconsistent sequence returned by provider.");
            return;
        }

        if (range.isReverse())
            throw new IllegalArgumentException("Don't support ");

        List<Map.Entry<Range, S>> allIntersecting = sequences.findAllIntersectingOrTouching(range);
        int resFrom = range.getFrom(), resTo = range.getTo();

        if (!allIntersecting.isEmpty()) {
            Range tmp = allIntersecting.get(0).getKey();
            if (tmp.containsBoundary(resFrom))
                resFrom = Math.min(resFrom, tmp.getFrom());
            tmp = allIntersecting.get(allIntersecting.size() - 1).getKey();
            if (tmp.containsBoundary(resTo))
                resTo = Math.max(resTo, tmp.getTo());
        }

        if (seq.size() < resTo - resFrom) {
            // Creating new sequence by merging several records
            SequenceBuilder<S> builder = alphabet.createBuilder()
                    .ensureCapacity(resTo - resFrom);

            if (range.getFrom() > resFrom) {
                Map.Entry<Range, S> entry = allIntersecting.get(0);
                assert resFrom == entry.getKey().getFrom();
                builder.append(entry.getValue().getRange(0, range.getFrom() - entry.getKey().getFrom()));
            }

            builder.append(seq);

            if (range.getTo() < resTo) {
                Map.Entry<Range, S> entry = allIntersecting.get(allIntersecting.size() - 1);
                assert resTo == entry.getKey().getTo();
                builder.append(entry.getValue().getRange(range.getTo() - entry.getKey().getFrom(), entry.getValue().size()));
            }

            seq = builder.createAndDestroy();
            range = new Range(resFrom, resTo);
        }

        for (Map.Entry<Range, S> e : allIntersecting) {
            // Checking
            int length = e.getKey().length();
            S s = e.getValue();
            for (int i = 0, j = e.getKey().getFrom() - range.getFrom(); i < length; ++i, ++j)
                if (seq.codeAt(j) != s.codeAt(i))
                    throw new IllegalStateException("Inconsistent sequence returned by provider.");
            sequences.remove(e.getKey());
        }

        sequences.put(range, seq);
    }

    /**
     * Internal
     */
    private static final class NoProvider<S extends Sequence<S>> implements SequenceProvider<S> {
        final int size;
        final String errorMessage;

        public NoProvider(int size) {
            this(size, "No sequence provider");
        }

        public NoProvider(int size, String errorMessage) {
            this.size = size;
            this.errorMessage = errorMessage;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public S getRegion(Range range) {
            throw new IndexOutOfBoundsException(errorMessage + " (query range = " + range + ")");
        }
    }
}
