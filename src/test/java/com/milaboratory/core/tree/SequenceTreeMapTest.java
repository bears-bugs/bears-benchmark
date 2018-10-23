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
package com.milaboratory.core.tree;

import com.milaboratory.core.alignment.*;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.*;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.milaboratory.core.sequence.SequencesUtils.concatenate;
import static com.milaboratory.test.TestUtil.its;
import static com.milaboratory.test.TestUtil.randomSequence;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class SequenceTreeMapTest {
    private int repeats = 10;

    public SequenceTreeMapTest() {
        this.repeats = 1;

        String val = System.getProperty("repeats");

        if (val != null) {
            try {
                this.repeats = Integer.valueOf(val, 10);
            } catch (NumberFormatException nfe) {
            }
        }
    }

    /*
     * Exact tests
     */

    @Test
    public void testExact1() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        assertNull(map.put(new NucleotideSequence("attagaca"), 1));
        assertEquals((Integer) 1, map.put(new NucleotideSequence("attagaca"), 2));

        assertNull(map.put(new NucleotideSequence("attacaca"), 3));

        assertEquals((Integer) 3, map.get(new NucleotideSequence("attacaca")));

        Set<NucleotideSequence> sequences = new HashSet<>();
        sequences.add(new NucleotideSequence("attacaca"));
        sequences.add(new NucleotideSequence("attagaca"));

        Set<Integer> ints = new HashSet<>();
        ints.add(2);
        ints.add(3);

        SequenceTreeMap.Node node;

        for (SequenceTreeMap.NodeIterator iterator = map.nodeIterator(); iterator.hasNext(); ) {
            node = iterator.next();
            assertTrue(ints.remove(node.object));
            assertTrue(sequences.remove(iterator.getSequence()));
        }

        assertTrue(sequences.isEmpty());
        assertTrue(ints.isEmpty());
    }

    @Test
    public void testExact2() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);
        Set<NucleotideSequence> sequences = new HashSet<>();
        Set<Integer> ints = new HashSet<>();

        assertNull(map.put(new NucleotideSequence("attacacaattaattacacacacaattacaca"), 3));
        sequences.add(new NucleotideSequence("attacacaattaattacacacacaattacaca"));
        ints.add(3);
        assertNull(map.put(new NucleotideSequence("attacacaattacacaattacgacacttacaca"), 4));
        sequences.add(new NucleotideSequence("attacacaattacacaattacgacacttacaca"));
        ints.add(4);
        assertNull(map.put(new NucleotideSequence("atattattacacaacacatacattacacaaca"), 5));
        sequences.add(new NucleotideSequence("atattattacacaacacatacattacacaaca"));
        ints.add(5);
        assertNull(map.put(new NucleotideSequence("attacacaattacacaattacacaattacacaattacacaattacaca"), 19));
        sequences.add(new NucleotideSequence("attacacaattacacaattacacaattacacaattacacaattacaca"));
        ints.add(19);

        SequenceTreeMap.Node node;
        for (SequenceTreeMap.NodeIterator iterator = map.nodeIterator(); iterator.hasNext(); ) {
            node = iterator.next();
            assertTrue(ints.remove(node.object));
            assertTrue(sequences.remove(iterator.getSequence()));
        }

        assertTrue(sequences.isEmpty());
        assertTrue(ints.isEmpty());
    }

    /*
     * Branching enumerator tests
     */

    @Test
    public void testBranchingEnumerator1() {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        assertNull(map.put(new NucleotideSequence("attagaca"), 1)); // 1 mm
        assertNull(map.put(new NucleotideSequence("attacaca"), 2)); // match
        assertNull(map.put(new NucleotideSequence("ataacaca"), 3)); // 1 mm
        assertNull(map.put(new NucleotideSequence("attcgtca"), 4)); // many mm
        assertNull(map.put(new NucleotideSequence("atttacaca"), 5)); // 1 insertion in stretch
        assertNull(map.put(new NucleotideSequence("atacaca"), 6)); // 1 deletion in the "t" stretch
        assertNull(map.put(new NucleotideSequence("attacacta"), 7)); // 1 insertion
        assertNull(map.put(new NucleotideSequence("attcaca"), 8)); // 1 deletion
        assertNull(map.put(new NucleotideSequence("attacac"), 9)); // 1 deletion in the end
        assertNull(map.put(new NucleotideSequence("ttacaca"), 10)); // 1 deletion in the beginning
        assertNull(map.put(new NucleotideSequence("tattacaca"), 11)); // 1 insertion in the beginning
        assertNull(map.put(new NucleotideSequence("attacacat"), 12)); // 1 insertion in the ent
        assertNull(map.put(new NucleotideSequence("attacact"), 13)); // 1 mm end
        assertNull(map.put(new NucleotideSequence("tttacaca"), 14)); // 1 mm begin

        HashSet<Integer>[] asserts = new HashSet[3];
        asserts[0] = new HashSet<>(Arrays.asList(1, 3, 13, 14));
        asserts[1] = new HashSet<>(Arrays.asList(6, 8, 9, 10));
        asserts[2] = new HashSet<>(Arrays.asList(5, 7, 11, 12));

        NucleotideSequence reference = new NucleotideSequence("attacaca");

        for (byte mode = 0; mode < 3; ++mode) {

            BranchingEnumerator<NucleotideSequence, Integer> e =
                    new BranchingEnumerator<>(reference, null);
            e.setup(mode, false);
            e.reset(0, map.root);
            SequenceTreeMap.Node<Integer> n;

            HashSet<Integer> collector = new HashSet<>();

            while ((n = e.next()) != null) {
                int i = e.getNextPositionAfterBranching();
                while (i < reference.size() && n != null)
                    n = n.links[reference.codeAt(i++)];
                if (n != null && n.object != null)
                    collector.add(n.object);
            }

            assertEquals(asserts[mode], collector);
        }
    }

    /*
     * Non-randomised tests for NeighborhoodIterator
     */

    @Test
    public void testGetMutations() throws Exception {
        SequenceTreeMap<NucleotideSequence, NucleotideSequence> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        NucleotideSequence[] seqs = new NucleotideSequence[]{
                new NucleotideSequence("attagaca"),
                new NucleotideSequence("attagaca"),
                new NucleotideSequence("attacaca"),
                new NucleotideSequence("ataacaca"),
                new NucleotideSequence("attcgtca"),
                new NucleotideSequence("atttacaca"),
                new NucleotideSequence("atacaca"),
                new NucleotideSequence("attacacta"),
                new NucleotideSequence("attcaca"),
                new NucleotideSequence("attacac"),
                new NucleotideSequence("ttacaca"),
                new NucleotideSequence("tattacaca"),
                new NucleotideSequence("attacacat"),
                new NucleotideSequence("attacact"),
                new NucleotideSequence("tttacaca")
        };

        for (NucleotideSequence seq : seqs)
            map.put(seq, seq);

        NucleotideSequence reference = new NucleotideSequence("attacaca");

        NeighborhoodIterator<NucleotideSequence, NucleotideSequence> ni = map.getNeighborhoodIterator(reference, 2.0,
                new double[]{0.31, 0.301, 0.3001}, new int[]{2, 2, 2}, null);

        for (NucleotideSequence seq : ni.it()) {
            Mutations<NucleotideSequence> mutations = ni.getCurrentMutations();
            assertEquals(seq, mutations.mutate(reference));
        }
    }

    @Test
    public void testNIterator() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        assertNull(map.put(new NucleotideSequence("attagaca"), 1)); // 1 mm
        assertNull(map.put(new NucleotideSequence("attacaca"), 2)); // match
        assertNull(map.put(new NucleotideSequence("ataacaca"), 3)); // 1 mm
        assertNull(map.put(new NucleotideSequence("attcgtca"), 4)); // many mm
        assertNull(map.put(new NucleotideSequence("atttacaca"), 5)); // 1 insertion in stretch
        assertNull(map.put(new NucleotideSequence("atacaca"), 6)); // 1 deletion in the "t" stretch
        assertNull(map.put(new NucleotideSequence("attacacta"), 7)); // 1 insertion
        assertNull(map.put(new NucleotideSequence("attcaca"), 8)); // 1 deletion
        assertNull(map.put(new NucleotideSequence("attacac"), 9)); // 1 deletion in the end
        assertNull(map.put(new NucleotideSequence("ttacaca"), 10)); // 1 deletion in the beginning
        assertNull(map.put(new NucleotideSequence("tattacaca"), 11)); // 1 insertion in the beginning
        assertNull(map.put(new NucleotideSequence("attacacat"), 12)); // 1 insertion in the ent
        assertNull(map.put(new NucleotideSequence("attacact"), 13)); // 1 mm end
        assertNull(map.put(new NucleotideSequence("tttacaca"), 14)); // 1 mm begin

        NucleotideSequence reference = new NucleotideSequence("attacaca");

        SequenceTreeMap.Node<Integer> node;

        HashSet<Integer>[] allAsserts = new HashSet[3];
        allAsserts[0] = new HashSet<>(Arrays.asList(1, 3, 13, 14));
        allAsserts[1] = new HashSet<>(Arrays.asList(6, 8, 9, 10));
        allAsserts[2] = new HashSet<>(Arrays.asList(5, 7, 11, 12));

        for (int i = 0; i < 8; ++i) {

            double lastPenalty = -1.0;
            HashSet<Integer> asserts = new HashSet<>();
            asserts.add(2);
            int[] maxMut = new int[3];
            for (int j = 0; j < 3; ++j) {
                if (((0x1 << j) & i) != 0) {
                    maxMut[j] = 1;
                    asserts.addAll(allAsserts[j]);
                }
            }

            HashSet<Integer> asserts1 = new HashSet<>(asserts);

            NeighborhoodIterator ni = map.getNeighborhoodIterator(reference, 0.5,
                    new double[]{0.31, 0.301, 0.3001}, maxMut, null);

            while ((node = ni.nextNode()) != null) {
                assertTrue(lastPenalty <= ni.getPenalty());
                lastPenalty = ni.getPenalty();
                asserts.remove(node.object);
                assertTrue(asserts1.contains(node.object));
            }
            assertTrue(asserts.isEmpty());
        }
    }

    @Test
    public void testEdge1() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("CTG"),
                sequence2 = new NucleotideSequence("C");

        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        //map.put(sequence1, 1);
        map.put(sequence2, 2);

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence1, 1.0,
                        new double[]{0.1, 0.1, Double.MAX_VALUE},
                        new int[]{0, 2, 0}, null);

        //System.out.println(neighborhoodIterator.nextNode().object);
    }

    @Test
    public void testEdge2() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("CTG"),
                sequence2 = new NucleotideSequence("CGT");

        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        //map.put(sequence1, 1);
        map.put(sequence2, 2);

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence1, 1.0,
                        new double[]{0.1, 0.1, Double.MAX_VALUE},
                        new int[]{2, 0, 0}, null);

        //System.out.println(neighborhoodIterator.nextNode().object);
    }

    @Test
    public void testEdge3() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("C"),
                sequence2 = new NucleotideSequence("CTG");

        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

        //map.put(sequence1, 1);
        map.put(sequence2, 2);

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence1, 1.0,
                        new double[]{0.1, 0.1, 0.1},
                        new int[]{0, 0, 2}, null);

        //System.out.println(neighborhoodIterator.nextNode().object);
    }

    /*
     * Non-randomised tests for NeighborhoodIterator in guided mode
     */

    @Test
    public void testGuideDel() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);
        map.put(new NucleotideSequence("attacacaattaattacacacacaattacaca"), 3);

        NucleotideSequence sequence = new NucleotideSequence("attacacaattaatttacacacacaattacaca");

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, new TreeSearchParameters(new int[]{1, 1, 0},
                                new double[]{0.1, 0.1, Double.MAX_VALUE}, 0.2),
                        new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 15 && type == 1;
                            }
                        }
                );

        assertNotNull(neighborhoodIterator.nextNode());

        neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, 0.2,
                        new double[]{0.1, 0.1, Double.MAX_VALUE},
                        new int[]{1, 1, 0}, new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 16 && type == 1;
                            }
                        }
                );

        assertNull(neighborhoodIterator.nextNode());
    }

    @Test
    public void testGuideMM() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);
        map.put(new NucleotideSequence("attacacaattaattacacacacaattacaca"), 3);
        //map.put(new NucleotideSequence("attacacaattaatttacacacacaattacaca"), 4);

        NucleotideSequence sequence = new NucleotideSequence("attacacaattaataacacacacaattacaca");

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, 0.2,
                        new double[]{0.1, 0.1, Double.MAX_VALUE},
                        new int[]{1, 1, 0}, new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 14 && type == 0;
                            }
                        }
                );

        assertNotNull(neighborhoodIterator.nextNode());

        neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, 0.2,
                        new double[]{0.1, 0.1, Double.MAX_VALUE},
                        new int[]{1, 1, 0}, new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 15 && type == 0;
                            }
                        }
                );

        assertNull(neighborhoodIterator.nextNode());
    }

    @Test
    public void testGuideIns() throws Exception {
        SequenceTreeMap<NucleotideSequence, Integer> map = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);
        map.put(new NucleotideSequence("attacacaattaattacacacacaattacaca"), 3);
        //map.put(new NucleotideSequence("attacacaattaatttacacacacaattacaca"), 4);

        NucleotideSequence sequence = new NucleotideSequence("attacacaattaatacacacacaattacaca");

        NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, 0.2,
                        new double[]{0.1, 0.1, 0.1},
                        new int[]{1, 1, 1}, new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 14 && type == 2;
                            }
                        }
                );

        assertNotNull(neighborhoodIterator.nextNode());

        neighborhoodIterator =
                map.getNeighborhoodIterator(sequence, 0.2,
                        new double[]{0.1, 0.1, 0.1},
                        new int[]{1, 1, 1}, new MutationGuide<NucleotideSequence>() {
                            @Override
                            public boolean allowMutation(NucleotideSequence ref, int position, byte type, byte code) {
                                return position == 15 && type == 2;
                            }
                        }
                );

        assertNull(neighborhoodIterator.nextNode());
    }

    @Test
    public void testGuideNew() throws Exception {
        SequenceTreeMap<AminoAcidSequence, Integer> map = new SequenceTreeMap<>(AminoAcidSequence.ALPHABET);

        map.put(new AminoAcidSequence("AAXSFD"), 3);
        map.put(new AminoAcidSequence("AAXFD"), 4);

        Set<Integer> set = new HashSet<>();
        Integer i;

        MutationGuide guide = new MutationGuide() {
            @Override
            public boolean allowMutation(Sequence reference, int position, byte type, byte to) {
                //TODO fix!!!!
                return type == 2;//&&  to == IncompleteAminoAcidSequence.UNKNOWN_LETTER_CODE;
            }
        };

        NeighborhoodIterator<AminoAcidSequence, Integer> ni = map.getNeighborhoodIterator(
                new AminoAcidSequence("AASFD"), 1, 1, 1, 1, guide);

        while ((i = ni.next()) != null) {
            set.add(i);
        }

        assertTrue(set.contains(3));
        assertFalse(set.contains(4));

        guide = new MutationGuide() {
            @Override
            public boolean allowMutation(Sequence reference, int position, byte type, byte to) {
                return type == 2 && to == AminoAcidAlphabet.F;
            }
        };

        ni = map.getNeighborhoodIterator(new AminoAcidSequence("AAXSD"), 1, 1, 1, 1, guide);

        while ((i = ni.next()) != null) {
            set.add(i);
        }

        assertTrue(set.contains(3));
        assertFalse(set.contains(4));


        guide = new MutationGuide() {
            @Override
            public boolean allowMutation(Sequence reference, int position, byte type, byte to) {
                return type == 1 && reference.codeAt(position) == AminoAcidAlphabet.G;
            }
        };

        ni = map.getNeighborhoodIterator(new AminoAcidSequence("AA_SGFD"), 1, 1, 1, 1, guide);

        while ((i = ni.next()) != null) {
            set.add(i);
        }

        assertTrue(set.contains(3));
        assertFalse(set.contains(4));
    }

    /*
         * Randomized tests
         */
    final RandomGenerator random = new Well19937a();

    /*
     * Utility functions and their tests
     */
//    private Sequence getRandomSequence(Alphabet alphabet, int length) {
//        SequenceBuilder builder = alphabet.getBuilderFactory().create(length);
//        for (int i = 0; i < length; ++i)
//            builder.setCode(i, (byte) random.nextInt(alphabet.size()));
//        return builder.create();
//    }

    private Sequence introduceMutation(Sequence sequence, int type) {
        SequenceBuilder builder;
        int position, i;
        switch (type) {
            case -1: //Copy
                return sequence;

            case 0: //Mismatch
                if (sequence.getAlphabet() == NucleotideSequence.ALPHABET)
                    return introduceNucleotideMismatch((NucleotideSequence) sequence);
                builder = sequence.getAlphabet().createBuilder().ensureCapacity(sequence.size());
                builder.append(sequence);
                position = random.nextInt(sequence.size());
                builder.set(position,
                        (byte) ((sequence.codeAt(position) + 1 + random.nextInt(sequence.getAlphabet().size() - 1)) %
                                sequence.getAlphabet().size()));
                return (Sequence) builder.createAndDestroy();

            case 1: //Deletion
                builder = sequence.getAlphabet().createBuilder().ensureCapacity(sequence.size() - 1);
                position = random.nextInt(sequence.size());
                for (i = 0; i < position; ++i)
                    builder.append(sequence.codeAt(i));
                ++i;
                for (; i < sequence.size(); ++i)
                    builder.append(sequence.codeAt(i));
                return (Sequence) builder.createAndDestroy();

            case 2: //Insertion
                builder = sequence.getAlphabet().createBuilder().ensureCapacity(sequence.size() + 1);
                position = random.nextInt(sequence.size() + 1);
                for (i = 0; i < position; ++i)
                    builder.append(sequence.codeAt(i));
                builder.append((byte) random.nextInt(sequence.getAlphabet().size()));
                for (; i < sequence.size(); ++i)
                    builder.append(sequence.codeAt(i));
                return (Sequence) builder.createAndDestroy();

            default:
                throw new IllegalArgumentException();
        }
    }

    private NucleotideSequence introduceNucleotideMismatch(NucleotideSequence sequence) {
        final byte[] storage = sequence.asArray();
        int position = random.nextInt(storage.length);
        storage[position] = (byte) (0x3 & (storage[position] + 1 + random.nextInt(3)));
        return new NucleotideSequence(storage);
    }

    final static Alphabet[] alphabets = {NucleotideSequence.ALPHABET, AminoAcidSequence.ALPHABET};

    private Alphabet getRandomAlphabet() {
        return alphabets[random.nextInt(alphabets.length)];
    }

    private Alphabet getAlphabetSequence(int id) {
        return alphabets[id % alphabets.length];
    }

    @Test
    public void testIntroduceMutation1() throws Exception {
        NucleotideSequence sequence;
        for (int i = its(100, 500); i > 0; --i) {
            sequence = (NucleotideSequence) randomSequence(NucleotideSequence.ALPHABET, 100, 100);
            for (int j = its(100, 500); j > 0; --j)
                assertThat(sequence, not(introduceNucleotideMismatch(sequence)));
        }
    }

    @Test
    public void testIntroduceMutation2() throws Exception {
        Sequence sequence;
        Alphabet alphabet;
        for (int i = its(100, 500); i > 0; --i) {
            alphabet = getRandomAlphabet();
            sequence = randomSequence(alphabet, 100, 100);

            //Testing correct equals implementation
            assertEquals(sequence, introduceMutation(sequence, -1));

            for (int j = its(100, 500); j > 0; --j) {
                assertThat(sequence, not(introduceMutation(sequence, 0)));
                assertThat(sequence, not(introduceMutation(sequence, 1)));
                assertThat(sequence, not(introduceMutation(sequence, 2)));
            }
        }
    }

    /*
     * More utility functions for randomized testing
     */
    private <S extends Sequence<S>> SequenceCluster<S> generateCluster(Alphabet<S> alphabet, int maxInCluster, int... maxMutations) {
        final SequenceCluster cluster = new SequenceCluster<>(randomSequence(alphabet, 200, 400));
        Sequence seq;
        int i, j;
        int[] mutations;
        for (i = 1 + random.nextInt(maxInCluster - 1); i > 0; --i) {
            seq = cluster.sequence;
            mutations = new int[3];

            for (j = (mutations[1] = randomInt(maxMutations[1] + 1)); j > 0; --j)
                seq = introduceMutation(seq, 1);

            for (j = (mutations[0] = randomInt(maxMutations[0] + 1)); j > 0; --j)
                seq = introduceMutation(seq, 0);

            for (j = (mutations[2] = randomInt(maxMutations[2] + 1)); j > 0; --j)
                seq = introduceMutation(seq, 2);

            if (seq.equals(cluster.sequence))
                continue;

            cluster.add(new MutatedSequence(seq, mutations));
        }

        return cluster;
    }

    private Sequence introduceErrors(Sequence seq, int[] maxMutations) {
        while (true) {
            int j;

            Sequence sequence = seq;

            for (j = randomInt(maxMutations[1] + 1); j > 0; --j)
                sequence = introduceMutation(sequence, 1);

            for (j = randomInt(maxMutations[0] + 1); j > 0; --j)
                sequence = introduceMutation(sequence, 0);

            for (j = randomInt(maxMutations[2] + 1); j > 0; --j)
                sequence = introduceMutation(sequence, 2);

            if (seq.equals(sequence))
                continue;

            return sequence;
        }
    }

    /**
     * Template for randomized test. See {@link #testRandomizedTest4Clusters()}
     */
    public <S extends Sequence<S>> void clusterTest(Alphabet<S> alphabet, int clusterCount, int inCluster, int[] errors) {
        SequenceCluster<S>[] clusters = new SequenceCluster[clusterCount];

        SequenceTreeMap<S, Integer> sequenceTreeMap = new SequenceTreeMap<>(alphabet);

        for (int i = 0; i < clusters.length; ++i) {
            clusters[i] = generateCluster(alphabet, inCluster, errors);
            sequenceTreeMap.put(clusters[i].sequence, 0);
            for (MutatedSequence<S> s : clusters[i].mutatedSequences)
                sequenceTreeMap.put(s.sequence, s.hashCode());
        }

        for (int i = 0; i < clusters.length; ++i) {
            NeighborhoodIterator<S, Integer> neighborhoodIterator =
                    sequenceTreeMap.getNeighborhoodIterator(clusters[i].sequence, 1.0,
                            new double[]{0.1, 0.1, 0.1},
                            errors, null);

            Set<Integer> set = new HashSet<>(clusters[i].hashes);
            set.add(0);

            SequenceTreeMap.Node<Integer> n;
            while ((n = neighborhoodIterator.nextNode()) != null)
                set.remove(n.object);

            if (!set.isEmpty()) {
                int k = set.iterator().next();
                for (MutatedSequence ms : clusters[i].mutatedSequences)
                    if (ms.hashCode() == k)
                        set.remove(1);
            }
            assertTrue(set.isEmpty());
        }
    }

    int randomInt(int i) {
        if (i == 0)
            return 0;
        return random.nextInt(i);
    }

    /*
     * Randomized tests
     */

    @Test
    public void testRemoveTest() throws Exception {
        RandomGenerator gen = new Well19937a();
        RandomDataGenerator data = new RandomDataGenerator(gen);
        for (int k = 0; k < 100; ++k) {
            Set<NucleotideSequence> seqSet = new HashSet<>();

            for (int i = 0; i < 1000; ++i)
                seqSet.add(randomSequence(NucleotideSequence.ALPHABET, data, 4, 30));

            SequenceTreeMap<NucleotideSequence, NucleotideSequence> seqTree = new SequenceTreeMap<>(NucleotideSequence.ALPHABET);

            for (NucleotideSequence seq : seqSet)
                seqTree.put(seq, seq);

            int n = 0;

            for (NucleotideSequence seq : seqTree.values()) {
                assertTrue(seqSet.contains(seq));
                ++n;
            }

            assertEquals(seqSet.size(), n);

            for (NucleotideSequence seq : seqSet)
                assertEquals(seq, seqTree.remove(seq));

            for (int i = 0; i < 4; ++i)
                assertNull(seqTree.root.links[i]);

            n = 0;

            for (NucleotideSequence seq : seqTree.values())
                ++n;

            assertEquals(0, n);
        }
    }

    @Test
    public void testRandomizedTest1() throws Exception {
        for (int f = 0; f < repeats; ++f) {
            //System.out.println(f);

            Alphabet<NucleotideSequence> alphabet = NucleotideSequence.ALPHABET;
            SequenceCluster<NucleotideSequence>[] clusters = new SequenceCluster[300];

            SequenceTreeMap<NucleotideSequence, Integer> sequenceTreeMap = new SequenceTreeMap<>(alphabet);
            for (int i = 0; i < clusters.length; ++i) {
                clusters[i] = generateCluster(alphabet, 70, 2, 2, 0);
                sequenceTreeMap.put(clusters[i].sequence, 0);
                for (MutatedSequence<NucleotideSequence> s : clusters[i].mutatedSequences)
                    sequenceTreeMap.put(s.sequence, s.hashCode());
            }

            for (int i = 0; i < clusters.length; ++i) {
                NeighborhoodIterator<NucleotideSequence, Integer> neighborhoodIterator =
                        sequenceTreeMap.getNeighborhoodIterator(clusters[i].sequence, 1.0,
                                new double[]{0.1, 0.1, Double.MAX_VALUE},
                                new int[]{2, 2, 0}, null);
                Set<Integer> set = new HashSet<>(clusters[i].hashes);
                set.add(0);
                SequenceTreeMap.Node<Integer> n;

                while ((n = neighborhoodIterator.nextNode()) != null)
                    set.remove(n.object);

                if (!set.isEmpty()) {
                    int k = set.iterator().next();
                    for (MutatedSequence ms : clusters[i].mutatedSequences)
                        if (ms.hashCode() == k)
                            set.remove(1);
                }

                assertTrue(set.isEmpty());
            }
        }
    }

    @Test
    public void testRandomizedTest2() throws Exception {
        for (int f = 0; f < repeats; ++f) {
            //System.out.println(f);

            Alphabet<NucleotideSequence> alphabet = NucleotideSequence.ALPHABET;
            SequenceCluster<NucleotideSequence>[] clusters = new SequenceCluster[300];

            SequenceTreeMap<NucleotideSequence, NucleotideSequence> sequenceTreeMap = new SequenceTreeMap<>(alphabet);
            for (int i = 0; i < clusters.length; ++i) {
                clusters[i] = generateCluster(alphabet, 70, 2, 2, 0);
                sequenceTreeMap.put(clusters[i].sequence, clusters[i].sequence);
                for (MutatedSequence<NucleotideSequence> s : clusters[i].mutatedSequences)
                    sequenceTreeMap.put(s.sequence, s.sequence);
            }

            for (int i = 0; i < clusters.length; ++i) {
                NeighborhoodIterator<NucleotideSequence, NucleotideSequence> neighborhoodIterator =
                        sequenceTreeMap.getNeighborhoodIterator(clusters[i].sequence, 1.0,
                                new double[]{0.1, 0.1, Double.MAX_VALUE},
                                new int[]{2, 2, 0}, null);

                SequenceTreeMap.Node<NucleotideSequence> n;

                while ((n = neighborhoodIterator.nextNode()) != null) {
                    Mutations<NucleotideSequence> mutations = neighborhoodIterator.getCurrentMutations();
                    assertEquals(n.getObject(), mutations.mutate(clusters[i].sequence));
                }
            }
        }
    }

    @Test
    public void testRandomizedTest4Clusters() {
        for (byte t = 0; t < 3; ++t) {
            int[] mut = new int[3];
            mut[t] = 2;
            clusterTest(NucleotideSequence.ALPHABET, 100, 30, mut);
            clusterTest(AminoAcidSequence.ALPHABET, 100, 30, mut);
        }
    }

    /*
     * Randomized tests for guided search
     */

    @Test
    public void testRandomizedTest3() throws Exception {
        for (int f = 0; f < repeats * 6; ++f) {
            //System.out.println(f);
            Alphabet alphabet = getAlphabetSequence(f);

            for (byte t = 0; t < 3; ++t) {
                final Sequence seqRight = randomSequence(alphabet, 50, 100),
                        seqLeft = randomSequence(alphabet, 50, 100),
                        spacer = randomSequence(alphabet, 200, 200),
                        goodSequence = concatenate(seqLeft, spacer, seqRight);


                SequenceTreeMap map = new SequenceTreeMap(alphabet);

                int[] mut = new int[3];
                mut[t] = 3;

                HashSet<Sequence> lErr = new HashSet<>(),
                        rErr = new HashSet<>(), lrErr = new HashSet<>();

                Sequence seq1, seq2, mseq;

                for (int i = 0; i < 100; ++i) {
                    //Left Error
                    seq1 = introduceErrors(seqLeft, mut);
                    mseq = concatenate(seq1, spacer, seqRight);
                    lErr.add(mseq);
                    map.put(mseq, mseq);

                    //Right Error
                    seq1 = introduceErrors(seqRight, mut);
                    mseq = concatenate(seqLeft, spacer, seq1);
                    rErr.add(mseq);
                    map.put(mseq, mseq);

                    //LR Error
                    seq1 = introduceErrors(seqLeft, mut);
                    seq2 = introduceErrors(seqRight, mut);
                    mseq = concatenate(seq1, spacer, seq2);
                    lrErr.add(mseq);
                    map.put(mseq, mseq);
                }

                SequenceTreeMap.Node<Sequence> n;

                //Left run
                NeighborhoodIterator neighborhoodIterator =
                        map.getNeighborhoodIterator(goodSequence, 1.3,
                                new double[]{0.1, 0.1, 0.1},
                                mut, new MutationGuide() {
                                    @Override
                                    public boolean allowMutation(Sequence ref, int position, byte type, byte code) {
                                        return position < seqLeft.size() + 100;
                                    }
                                }
                        );

                HashSet<Sequence> acc = new HashSet<>(lErr);

                while ((n = neighborhoodIterator.nextNode()) != null) {
                    assertTrue(lErr.contains(n.object));
                    assertFalse(rErr.contains(n.object));
                    assertFalse(lrErr.contains(n.object));
                    acc.remove(n.object);
                }
                assertTrue(acc.isEmpty());

                //Right run
                neighborhoodIterator =
                        map.getNeighborhoodIterator(goodSequence, 1.3,
                                new double[]{0.1, 0.1, 0.1},
                                mut, new MutationGuide() {
                                    @Override
                                    public boolean allowMutation(Sequence ref, int position, byte type, byte code) {
                                        return position > seqLeft.size() + 100;
                                    }
                                }
                        );

                acc = new HashSet<>(rErr);

                while ((n = neighborhoodIterator.nextNode()) != null) {
                    assertTrue(rErr.contains(n.object));
                    assertFalse(lErr.contains(n.object));
                    assertFalse(lrErr.contains(n.object));
                    acc.remove(n.object);
                }
                assertTrue(acc.isEmpty());
            }
        }
    }

    /*
     * Classes for randomized tests
     */
    public static final class SequenceCluster<S extends Sequence<S>> {
        final S sequence;
        final List<MutatedSequence<S>> mutatedSequences = new ArrayList<>();
        final Set<Integer> hashes = new HashSet<>();

        public SequenceCluster(S sequence) {
            this.sequence = sequence;
        }

        public void add(MutatedSequence s) {
            mutatedSequences.add(s);
            hashes.add(s.hashCode());
        }
    }

    public static final class MutatedSequence<S extends Sequence<S>> {
        final int[] mutations;
        final S sequence;

        public MutatedSequence(S sequence, int... mutations) {
            this.mutations = mutations;
            this.sequence = sequence;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MutatedSequence that = (MutatedSequence) o;

            if (!Arrays.equals(mutations, that.mutations)) return false;
            if (!sequence.equals(that.sequence)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            //int result = Arrays.hashCode(mutations);
            return sequence.hashCode();
        }
    }

    @Test
    public void optimalityAndScopeTest() {
        //
        // CASSLAPGATN-KLFF seq1
        // CASS-APGATNEKLFF seq2 1ins 1del from s1
        // CASSLAPG-TNEKLFF seq3
        // CASSLAPG-TNEKLyF seq4 + 1mm

        AminoAcidSequence seq1 = new AminoAcidSequence("CASSLAPGATNKLFF"),
                seq2 = new AminoAcidSequence("CASSAPGATNEKLFF"),
                seq3 = new AminoAcidSequence("CASSLAPGTNEKLFF"),
                seq4 = new AminoAcidSequence("CASSLAPGTNEKLYF");

        // Sanity check

        Alignment<AminoAcidSequence> aln1 = Aligner.alignGlobalLinear(
                LinearGapAlignmentScoring.getAminoAcidBLASTScoring(BLASTMatrix.BLOSUM45),
                seq1, seq2
        ), aln2 = Aligner.alignGlobalLinear(
                LinearGapAlignmentScoring.getAminoAcidBLASTScoring(BLASTMatrix.BLOSUM45),
                seq1, seq3
        ), aln3 = Aligner.alignGlobalLinear(
                LinearGapAlignmentScoring.getAminoAcidBLASTScoring(BLASTMatrix.BLOSUM45),
                seq1, seq4
        );
        System.out.println("--NW  alignments--");
        System.out.println(AlignmentUtils.toStringSimple(seq1, aln1.getAbsoluteMutations()));
        System.out.println(AlignmentUtils.toStringSimple(seq1, aln2.getAbsoluteMutations()));
        System.out.println(AlignmentUtils.toStringSimple(seq1, aln3.getAbsoluteMutations()));

        // Init STM

        SequenceTreeMap<AminoAcidSequence, AminoAcidSequence> stm = new SequenceTreeMap<>(AminoAcidSequence.ALPHABET);
        stm.put(seq1, seq1); // we ignore seq1 in search -- anyways not placing it will also reproduce the bug
        stm.put(seq2, seq2);
        stm.put(seq3, seq3);
        stm.put(seq4, seq4);

        // Finding optimal match / finding seq4

        NeighborhoodIterator<AminoAcidSequence, AminoAcidSequence> ni = stm.getNeighborhoodIterator(
                seq1, new TreeSearchParameters(1, 1, 1, 3)
        );

        AminoAcidSequence currentMatch;
        int minMms = seq1.size();
        boolean seq4Found = false;
        System.out.println("--STM alignments--");
        while ((currentMatch = ni.next()) != null) {
            if (!currentMatch.equals(seq1)) {
                Mutations<AminoAcidSequence> mutations = ni.getCurrentMutations();
                minMms = Math.min(mutations.size(), minMms);
                System.out.println(AlignmentUtils.toStringSimple(seq1, mutations));

                if (currentMatch.equals(seq4)) {
                    seq4Found = true;
                }
            }
        }
        System.out.println("------------------");

        Assert.assertEquals(2, minMms); // found optimal alignment for seq2/seq3
        Assert.assertTrue(seq4Found); // seq4 found within scope


        // Finding any match with correct scope for seq2 & seq3

        ni = stm.getNeighborhoodIterator(
                seq1, new TreeSearchParameters(0, 1, 1, 3)
        );

        int results = 0;
        while ((currentMatch = ni.next()) != null) {
            if (!currentMatch.equals(seq1)) {
                results++;
            }
        }

        Assert.assertEquals(2, results);
    }

    @Test
    public void greedyTest() {
        AminoAcidSequence seq1 = new AminoAcidSequence("CAAAAAAW"),
                seq2 = new AminoAcidSequence("CAAAAAAAW"),
                seq3 = new AminoAcidSequence("CCCCEACCCCCCC"),
                seq4 = new AminoAcidSequence("CCCCTCCCCCCC");

        SequenceTreeMap<AminoAcidSequence, AminoAcidSequence> stm = new SequenceTreeMap<>(AminoAcidSequence.ALPHABET);
        stm.put(seq1, seq1);
        stm.put(seq3, seq3);

        // All equal 1 insertion

        NeighborhoodIterator<AminoAcidSequence, AminoAcidSequence> ni = stm.getNeighborhoodIterator(
                seq2, new TreeSearchParameters(0, 1, 0, 1)
        );

        int i = 0;
        while (ni.next() != null) {
            i++;
            //Mutations<AminoAcidSequence> mutations = ni.getCurrentMutations();
            //System.out.println(mutations);
        }

        Assert.assertEquals(7, i); // all 7 variants

        // insertion + mismatch greedy

        ni = stm.getNeighborhoodIterator(
                seq4, new TreeSearchParameters(1, 0, 1, 2)
        );

        i = 0;
        while (ni.next() != null) {
            i++;
            //Mutations<AminoAcidSequence> mutations = ni.getCurrentMutations();
            //System.out.println(mutations);
        }

        Assert.assertEquals(1, i); // only 1 variant as we're greedy

        // insertion + mismatch non-greedy

        ni = stm.getNeighborhoodIterator(
                seq4, new TreeSearchParameters(1, 0, 1, 2, false)
        );

        i = 0;
        while (ni.next() != null) {
            i++;
            //Mutations<AminoAcidSequence> mutations = ni.getCurrentMutations();
            //System.out.println(mutations);
        }

        Assert.assertEquals(2, i); // 2 combinations S4I5 + I4S4

    }
}
