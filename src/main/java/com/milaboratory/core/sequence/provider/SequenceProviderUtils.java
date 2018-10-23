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
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.Sequence;

public final class SequenceProviderUtils {
    private SequenceProviderUtils() {
    }

    public static SequenceProvider<NucleotideSequence> reversedProvider(final SequenceProvider<NucleotideSequence> provider) {
        return new SubSequenceProvider<>(new Range(provider.size(), 0), provider);
    }

    public static <S extends Sequence<S>> SequenceProvider<S> subProvider(final SequenceProvider<S> provider, final Range targetRange) {
        return new SubSequenceProvider<>(targetRange, provider);
    }

    public static <S extends Sequence<S>> SequenceProvider<S> fromSequence(final S sequence) {
        return new SequenceProvider<S>() {
            @Override
            public int size() {
                return sequence.size();
            }

            @Override
            public S getRegion(Range range) {
                if (range.getUpper() > sequence.size())
                    throw new SequenceProviderIndexOutOfBoundsException(range.intersection(new Range(0, sequence.size())));
                return sequence.getRange(range);
            }
        };
    }

    public static <S extends Sequence<S>> SequenceProvider<S> lazyProvider(final SequenceProviderFactory<S> factory) {
        return new LazySequenceProvider<>(factory);
    }

    private static final class SubSequenceProvider<S extends Sequence<S>> implements SequenceProvider<S> {
        final Range targetRange;
        final SequenceProvider<S> provider;

        public SubSequenceProvider(Range targetRange, SequenceProvider<S> provider) {
            if (provider instanceof SubSequenceProvider) {
                this.targetRange = ((SubSequenceProvider<S>) provider).targetRange.getAbsoluteRangeFor(targetRange);
                this.provider = ((SubSequenceProvider<S>) provider).provider;
            } else {
                this.targetRange = targetRange;
                this.provider = provider;
            }
        }

        @Override
        public int size() {
            return targetRange.length();
        }

        @Override
        public S getRegion(Range range) {
            return provider.getRegion(targetRange.getAbsoluteRangeFor(range));
        }
    }

    public static class LazySequenceProvider<S extends Sequence<S>> implements SequenceProvider<S> {
        private final SequenceProviderFactory<S> factory;
        volatile SequenceProvider<S> innerProvider;

        public LazySequenceProvider(SequenceProviderFactory<S> factory) {
            this.factory = factory;
            innerProvider = null;
        }

        public boolean isInitialized() {
            return innerProvider != null;
        }

        void ensureProvider() {
            if (innerProvider == null)
                synchronized (this) {
                    if (innerProvider == null)
                        innerProvider = factory.create();
                }
        }

        @Override
        public int size() {
            ensureProvider();
            return innerProvider.size();
        }

        @Override
        public S getRegion(Range range) {
            ensureProvider();
            return innerProvider.getRegion(range);
        }
    }
}
