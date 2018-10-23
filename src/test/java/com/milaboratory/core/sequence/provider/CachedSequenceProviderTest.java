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
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.Well44497b;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CachedSequenceProviderTest {
    @Test
    public void test1() throws Exception {
        final NucleotideSequence sequence = new NucleotideSequence("ATTAGACAGCTGCATAGTGCTCGCTCGGCGATGACTGCGCGGCGCGC" +
                "ATGGATCGACTAGCTCTATCGAGCTTCTCTGAAGCGTATCGAT");
        final List<Range> requests = new ArrayList<>();

        CachedSequenceProvider<NucleotideSequence> cache = new CachedSequenceProvider<>(NucleotideSequence.ALPHABET,
                new SequenceProvider<NucleotideSequence>() {
                    @Override
                    public int size() {
                        return sequence.size();
                    }

                    @Override
                    public NucleotideSequence getRegion(Range range) {
                        requests.add(range);
                        return sequence.getRange(range);
                    }
                });

        Range r = new Range(10, 20);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(15, 20);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(15, 25);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(30, 40);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(26, 28);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(26, 31);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(40, 45);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
        r = new Range(25, 26);
        Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));

        assertList(requests,
                10, 20,
                10, 25,
                30, 40,
                26, 28,
                26, 40,
                26, 45,
                10, 45);

        Assert.assertEquals(sequence.size(), cache.size());
    }

    @Test
    public void test2() throws Exception {
        test2i(NucleotideSequence.ALPHABET);
        test2i(AminoAcidSequence.ALPHABET);
    }

    public <S extends Sequence<S>> void test2i(Alphabet<S> alphabet) throws Exception {
        Well44497b w = new Well44497b();
        for (int i = 0; i < 100; i++) {
            final S sequence = TestUtil.randomSequence(alphabet, 1000, 2000);

            CachedSequenceProvider<S> cache = new CachedSequenceProvider<>(alphabet, SequenceProviderUtils.fromSequence(sequence));

            for (int j = 0; j < 1000; j++) {
                int from = w.nextInt(sequence.size() - 1);
                int to = from + w.nextInt(sequence.size() - from);
                Range r = new Range(from, to);
                Assert.assertEquals(sequence.getRange(r), cache.getRegion(r));
                Assert.assertFalse(cache.sequences.isOverFragmented());
            }

            Assert.assertTrue(cache.size() <= sequence.size());
        }
    }

    @Test
    public void test3() throws Exception {
        test3i(NucleotideSequence.ALPHABET);
        test3i(AminoAcidSequence.ALPHABET);
    }

    public <S extends Sequence<S>> void test3i(Alphabet<S> alphabet) throws Exception {
        Well44497b w = new Well44497b();
        for (int i = 0; i < 100; i++) {
            final S sequence = TestUtil.randomSequence(alphabet, 1000, 2000);

            CachedSequenceProvider<S> cache = new CachedSequenceProvider<>(alphabet);

            List<Range> ranges = new ArrayList<>();

            for (int j = 0; j < 100; j++) {
                int from = w.nextInt(sequence.size() - 1);
                int to = from + w.nextInt(sequence.size() - from);
                Range r = new Range(from, to);
                if (r.isEmpty())
                    continue;
                ranges.add(r);
                cache.setRegion(r, sequence.getRange(r));
                Assert.assertFalse(cache.sequences.isOverFragmented());
                for (Range rr : ranges)
                    Assert.assertEquals(sequence.getRange(rr), cache.getRegion(rr));
            }

            Assert.assertTrue(cache.size() <= sequence.size());
        }
    }


    public static void assertList(List<Range> ranges, int... boundaries) {
        Assert.assertEquals(boundaries.length / 2, ranges.size());
        for (int i = 0; i < ranges.size(); i++) {
            Assert.assertEquals("At " + i, boundaries[i * 2], ranges.get(i).getFrom());
            Assert.assertEquals("At " + i, boundaries[i * 2 + 1], ranges.get(i).getTo());
        }
    }
}