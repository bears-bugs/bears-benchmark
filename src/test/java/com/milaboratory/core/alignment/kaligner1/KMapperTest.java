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
package com.milaboratory.core.alignment.kaligner1;

import com.milaboratory.core.alignment.AlignmentTest;
import com.milaboratory.core.alignment.LinearGapAlignmentScoring;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.IntArrayList;
import junit.framework.Assert;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.milaboratory.core.alignment.kaligner1.KMapper.SEED_NOT_FOUND_OFFSET;
import static com.milaboratory.core.alignment.kaligner1.KMapper.getBestOffset;
import static com.milaboratory.core.mutations.generator.MutationsGenerator.generateMutations;
import static com.milaboratory.test.TestUtil.its;
import static com.milaboratory.test.TestUtil.randomSequence;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class KMapperTest extends AlignmentTest {
    public static final KAlignerParameters gParams = new KAlignerParameters(5, false, false,
            1.5f, 0.75f, 1.0f, -0.1f, -0.3f, 4, 10, 15, 2, -10,
            40.0f, 0.87f, 7,
            LinearGapAlignmentScoring.getNucleotideBLASTScoring());

    @Test
    public void testBestOffset1() throws Exception {
        assertEquals(4, getBestOffset(new IntArrayList(-1, 2, 3, 3, 4, 4, 4), 10, 0, 0));
        assertEquals(3, getBestOffset(new IntArrayList(-1, 2, 3, 3, 3, 4, 5), 10, 0, 0));
        assertEquals(-1, getBestOffset(new IntArrayList(-1, -1, -1, 3, 3, 4, 5), 10, 0, 0));
        assertEquals(3, getBestOffset(new IntArrayList(-1, -1, 3, 3, 3, 4, 5), 10, 0, 0));
        assertEquals(-1, getBestOffset(new IntArrayList(-1, -1, 3, 3, 3, 4, 5), 3, 0, 0));
        assertEquals(3, getBestOffset(new IntArrayList(-1, -1, -1, 3, 3, 4, 5), -1, 0, 0));
        //assertEquals(23, getBestOffset(new IntArrayList(22, 23, 27, 54, 68, 93), Integer.MIN_VALUE, 0, 1));
    }

    @Test
    public void testBestOffset2() {
        //int v = -3358711;
        int offset = -205;
        int v = offset << 14 | 0;
        IntArrayList list = new IntArrayList(1);
        list.add(v);
        //list.add((offset+1) << 14 | 0);
        assertEquals(-205, getBestOffset(list, Integer.MIN_VALUE, 14, 3));
        System.out.println(v >> 14);

    }

    @Test
    public void test1() throws Exception {
        KMapper aligner = KMapper.createFromParameters(gParams);
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        for (int i = 0; i < its(1000, 50000); ++i) {
            List<KMappingHit> hits =
                    aligner.align(new NucleotideSequence("GAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGAAGTAGATGATGATGCAGCGTATG")).hits;

            assertEquals("On i = " + i, 1, hits.size());
            assertEquals(-21, hits.get(0).offset);
            assertEquals(2, hits.get(0).id);
        }
    }

    @Test
    public void test2() throws Exception {
        KMapper aligner = KMapper.createFromParameters(gParams);
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        KMappingResult result =
                aligner.align(new NucleotideSequence("GACATTATATACAGACATATAATAAATACGGATACGCTGTCGGCCTAGGCGCGTCGAACGCGC"));

        Assert.assertEquals(1, result.hits.size());
        Assert.assertEquals(2, result.hits.get(0).id);
    }

    @Test
    public void testRandom1() throws Exception {
        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
        int baseSize = its(500, 2000);
        List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
        for (int i = 0; i < baseSize; ++i)
            ncs.add(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 60));

        KMapper ka = KMapper.createFromParameters(gParams.clone().setMapperMaxSeedsDistance(2).setMapperMinSeedsDistance(1).setMapperKValue(6));
        for (NucleotideSequence seq : ncs)
            ka.addReference(seq);

        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(3.0);
        int total = its(1000, 100000);
        int found = 0;
        OUTER:
        for (int i = 0; i < total; ++i) {
            int id = rdi.nextInt(0, baseSize - 1);
            NucleotideSequence seq = ncs.get(id);
            int subSize = rdi.nextInt(15, seq.size());
            boolean left = (rdi.nextInt(0, 1) == 0);
            NucleotideSequence target;
            if (left)
                target = seq.getRange(seq.size() - subSize, seq.size()).concatenate(randomSequence(NucleotideSequence.ALPHABET, rdi, 20, 30));
            else
                target = randomSequence(NucleotideSequence.ALPHABET, rdi, 20, 30).concatenate(seq.getRange(0, subSize));

            int[] muts = generateMutations(target, model).getRAWMutations();
            target = mutate(target, muts);

            KMappingResult result = ka.align(target);
            List<KMappingHit> hits = result.hits;
            for (KMappingHit hit : hits) {

                int previousSeedHit = -1, seedHit, seedHitOffset;
                for (int k = 0; k < hit.seedOffsets.length; ++k) {
                    seedHitOffset = hit.seedOffsets[k];
                    if (seedHitOffset != KMapper.SEED_NOT_FOUND_OFFSET)
                        if (previousSeedHit == -1)
                            previousSeedHit = result.seeds.get(hit.from + k) - seedHitOffset;
                        else {
                            seedHit = result.seeds.get(hit.from + k) - seedHitOffset;
                            assertTrue(previousSeedHit < seedHit);
                            previousSeedHit = seedHit;
                        }
                }

                if (hit.id == id) {
                    //Test for kmers
                    for (int k = 0; k < hit.seedOffsets.length; ++k) {
                        if (hit.seedOffsets[k] == SEED_NOT_FOUND_OFFSET)
                            continue;
                        int kmer1 = 0, kmer2 = 0;
                        //get kmer in target sequence
                        int targetFrom = result.seeds.get(hit.from + k);
                        for (int j = targetFrom; j < targetFrom + ka.getKValue(); ++j)
                            kmer1 = kmer1 << 2 | target.codeAt(j);

                        //kmer in ref sequence
                        int refFrom = targetFrom - hit.seedOffsets[k];
                        for (int j = refFrom; j < refFrom + ka.getKValue(); ++j)
                            kmer2 = kmer2 << 2 | ncs.get(hit.id).codeAt(j);

                        Assert.assertEquals(kmer1, kmer2);
                    }
                    ++found;
                    continue OUTER;
                }
            }

        }

        Assert.assertTrue((float) found / total > 0.85f);
    }
}