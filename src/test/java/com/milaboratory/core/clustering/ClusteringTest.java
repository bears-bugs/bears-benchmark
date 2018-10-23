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
package com.milaboratory.core.clustering;

import com.milaboratory.core.alignment.Aligner;
import com.milaboratory.core.alignment.LinearGapAlignmentScoring;
import com.milaboratory.core.mutations.Mutation;
import com.milaboratory.core.mutations.MutationType;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.tree.NeighborhoodIterator;
import com.milaboratory.core.tree.TreeSearchParameters;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class ClusteringTest {
    private static final SequenceExtractor<TestObject, NucleotideSequence> EXTRACTOR
            = new SequenceExtractor<TestObject, NucleotideSequence>() {
        @Override
        public NucleotideSequence getSequence(TestObject object) {
            return object.sequence;
        }
    };

    private static Cluster<TestObject> getRandomTestCluster(NucleotideSequence sequence,
                                                            int depth, int maxChildren,
                                                            int maxCount, int delta,
                                                            long seed) {
        return getRandomTestCluster(sequence, 0, depth, maxChildren, maxCount, delta, seed);
    }

    private static Cluster<TestObject> getRandomTestCluster(NucleotideSequence sequence,
                                                            int color,
                                                            int depth, int maxChildren,
                                                            int maxCount, int delta,
                                                            long seed) {
        assert delta * depth < maxCount;
        Cluster<TestObject> head = new Cluster<>(new TestObject(maxCount, sequence, color));
        HashSet<NucleotideSequence> nucleotideSequences = new HashSet<>();
        nucleotideSequences.add(sequence);
        addRandomTestCluster(head, color, nucleotideSequences, maxChildren, depth, delta, new Well19937a(seed));
        return head;
    }

    private static void addRandomTestCluster(
            Cluster<TestObject> headCluster,
            int color,
            HashSet<NucleotideSequence> generated,
            int maxCount, int depth, int delta, RandomGenerator random) {
        if (depth == 0)
            return;
        int count = random.nextInt(maxCount);
        NucleotideSequence sequence = headCluster.head.sequence;
        int position, mutationType, from, to = 0;

        for (int i = 0; i < count; ++i) {
            mutationType = random.nextInt(3);
            position = random.nextInt(sequence.size() + (mutationType == 2 ? 1 : 0));

            from = mutationType == 2 ? 0 : sequence.codeAt(position);
            switch (mutationType) {
                case 0:
                    do {
                        to = random.nextInt(4);
                    } while (to == from);
                    break;
                case 1:
                    to = 0;
                    break;
                case 2:
                    to = random.nextInt(4);
                    break;
            }

            int mutation = Mutation.createMutation(
                    MutationType.getType(mutationType),
                    position, from, to);
            NucleotideSequence mutated = new Mutations<NucleotideSequence>(
                    NucleotideSequence.ALPHABET, new int[]{mutation}).mutate(sequence);

            if (!generated.add(mutated)) {
                --i;
                continue;
            }

            if (!testSequenceToAdd(headCluster, mutated)) {
                --i;
                continue;
            }

            Cluster<TestObject> tCluster = new Cluster<>(
                    new TestObject(headCluster.head.count - delta, mutated, color), headCluster);
            addRandomTestCluster(tCluster, color, generated, maxCount, depth - 1, delta, random);
        }
    }

    private static boolean testSequenceToAdd(Cluster<TestObject> head, NucleotideSequence sequence) {
        int counter = 1;
        Cluster<TestObject> current = head;
        do {
            Mutations mutations = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                    current.head.sequence, sequence).getAbsoluteMutations();
            if (counter == 1)
                Assert.assertEquals(1, mutations.size());

            Assert.assertTrue(mutations.size() <= counter);

            if (mutations.size() != counter)
                return false;

            ++counter;
            current = current.parent;
        } while (current != null);

        return true;
    }

    private static void assertDiffSizeClusters(ClusteringStrategy<TestObject, NucleotideSequence> strategy,
                                               Cluster<TestObject>... clusters) {
        Comparator<Cluster<TestObject>> comparatorOfClusters = Clustering.getComparatorOfClusters(strategy, EXTRACTOR);
        for (Cluster<TestObject> cluster : clusters)
            cluster.sort(comparatorOfClusters);
        ArrayList<TestObject> input = Cluster.getAll(clusters);
        List<Cluster<TestObject>> result =
                Clustering.performClustering(input, EXTRACTOR, strategy);
        Assert.assertTrue(input.size() != result.size());
    }

    private static void assertClusters(boolean diffLayerCounts,
                                       ClusteringStrategy<TestObject, NucleotideSequence> strategy,
                                       Cluster<TestObject>... clusters) {
        Comparator<Cluster<TestObject>> comparatorOfClusters = Clustering.getComparatorOfClusters(strategy, EXTRACTOR);
        for (Cluster<TestObject> cluster : clusters)
            cluster.sort(comparatorOfClusters);
        ArrayList<TestObject> input = Cluster.getAll(clusters);
        List<Cluster<TestObject>> result =
                Clustering.performClustering(input, EXTRACTOR, strategy);
        Arrays.sort(clusters, comparatorOfClusters);

        Assert.assertEquals(clusters.length, result.size());

        for (int i = 0; i < clusters.length; ++i) {
            //assert equal counts
            Assert.assertEquals(clusters[i].head, result.get(i).head);
            Assert.assertEquals(clusters[i].totalCount(), result.get(i).totalCount());
            //assert correct counts
            assertClusterChildren(diffLayerCounts, result.get(i));
        }
    }

    private static void assertClusterChildren(boolean diffLayerCounts, Cluster<TestObject> cluster) {
        if (cluster.children == null)
            return;
        int count = cluster.children.get(0).head.count;
        int color = cluster.head.color;
        Assert.assertTrue(count < cluster.head.count);
        Assert.assertEquals(color, cluster.children.get(0).head.color);
        assertClusterChildren(diffLayerCounts, cluster.children.get(0));
        for (int i = 1; i < cluster.children.size(); ++i) {
            if (!diffLayerCounts)
                Assert.assertEquals(count, cluster.children.get(i).head.count);
            else
                Assert.assertTrue(cluster.children.get(i).head.count < cluster.head.count);
            Assert.assertEquals(color, cluster.children.get(i).head.color);
            assertClusterChildren(diffLayerCounts, cluster.children.get(i));
        }
    }

    private static <T> Cluster<T> createCluster(T head, Cluster<T>... children) {
        Cluster<T> r = new Cluster<T>(head, null);
        if (children != null)
            for (Cluster<T> c : children) {
                r.add(c);
            }
        return r;
    }

    private static TestObject createtestObject(int i, String str) {
        return new TestObject(i, str);
    }

    //@Test
    public void test1() throws Exception {
        TestStrategy strategy = new TestStrategy(2, new TreeSearchParameters(1, 1, 1, 1));
        Cluster<TestObject> expected =
                createCluster(
                        createtestObject(100, "AAAAAAAA"),
                        createCluster(
                                createtestObject(50, "AAAAAAAT"),
                                createCluster(createtestObject(10, "ATAAAAAT"))),
                        createCluster(
                                createtestObject(45, "AAACAAAA"),
                                createCluster(createtestObject(15, "AAACAAA")))
                );
        expected.sort(Clustering.getComparatorOfClusters(strategy, EXTRACTOR));

        TestObject[] cluster1 =
                {new TestObject(100, "AAAAAAAA"),
                        new TestObject(50, "AAAAAAAT"),
                        new TestObject(45, "AAACAAAA"),
                        new TestObject(10, "ATAAAAAT"),
                        new TestObject(15, "AAACAAA"),
                };

        List<TestObject> input = Arrays.asList(cluster1);
        List<Cluster<TestObject>> clusters =
                Clustering.performClustering(input,
                        EXTRACTOR,
                        strategy);
        for (Cluster<TestObject> cluster : clusters) {
            System.out.println(cluster);
            System.out.println();
        }
    }

    @Test
    public void test2() throws Exception {
        TestStrategy strategy = new TestStrategy(2, new TreeSearchParameters(1, 1, 1, 1));
        Cluster<TestObject> head = createCluster(createtestObject(100, "AAAAAAAA"));
        Cluster<TestObject> child1 = new Cluster<>(createtestObject(50, "AAAAAAAT"), head);
        Cluster<TestObject> child1_1 = new Cluster<>(createtestObject(10, "ATAAAAAT"), child1);
        child1.add(child1_1);
        Cluster<TestObject> child2 = new Cluster<>(createtestObject(45, "AAACAAAA"), head);
        Cluster<TestObject> child2_1 = new Cluster<>(createtestObject(15, "AAACAAA"), child2);
        child2.add(child2_1);

        head.add(child1);
        head.add(child2);

        assertClusters(true, strategy, head);
    }

    @Test
    public void testRandom1() throws Exception {
        int out = TestUtil.its(3, 30);
        Well19937a rand = new Well19937a();
        for (int x = 0; x < out; ++x) {
            int N = TestUtil.its(40, 300);
            Cluster<TestObject>[] clusters = new Cluster[N];
            Well19937a rand1 = new Well19937a(rand.nextLong());

            TestStrategy strategy = new TestStrategy(5, new TreeSearchParameters(1, 1, 1, 1));
            for (int i = 0; i < N; ++i)
                clusters[i] = getRandomTestCluster(
                        TestUtil.randomSequence(NucleotideSequence.ALPHABET,
                                rand1, 45, 55), strategy.depth, 5,
                        10000 + rand1.nextInt(10000), 5 + rand1.nextInt(900), rand1.nextLong());

            assertClusters(false, strategy, clusters);
        }
    }

    @Test
    public void testRandom2WithColor() {
        int out = TestUtil.its(3, 30);
        Well19937a rand = new Well19937a();
        int colors = 3;
        for (int x = 0; x < out; ++x) {
            int N = TestUtil.its(16, 100);
            Cluster<TestObject>[] clusters = new Cluster[N * colors];
            Well19937a rand1 = new Well19937a(rand.nextLong());

            TestStrategy strategy = new TestStrategy(5, new TreeSearchParameters(1, 1, 1, 1));
            int c = 0;
            for (int i = 0; i < N; ++i) {
                long seed = rand1.nextLong();
                NucleotideSequence sequence = TestUtil.randomSequence(NucleotideSequence.ALPHABET,
                        rand1, 45, 55);
                for (int color = 0; color < colors; ++color) {
                    clusters[c++] = getRandomTestCluster(
                            sequence, color, strategy.depth, 5,
                            10000 + rand1.nextInt(10000), 5 + rand1.nextInt(900), seed);
                }
            }

            assertDiffSizeClusters(strategy.getDummy(), clusters);
            assertClusters(false, strategy, clusters);
        }
    }

    private static class DummyStrategy
            implements ClusteringStrategy<TestObject, NucleotideSequence> {
        final int depth;
        final TreeSearchParameters parameters;

        DummyStrategy(int depth, TreeSearchParameters parameters) {
            this.depth = depth;
            this.parameters = parameters;
        }

        @Override
        public boolean canAddToCluster(Cluster<TestObject> cluster,
                                       TestObject minorObject,
                                       NeighborhoodIterator<NucleotideSequence, TestObject[]> iterator) {
            return true;
        }

        @Override
        public TreeSearchParameters getSearchParameters() {
            return parameters;
        }

        @Override
        public int getMaxClusterDepth() {
            return depth;
        }

        @Override
        public int compare(TestObject o1, TestObject o2) {
            return Integer.compare(o1.count, o2.count);
        }
    }

    private static class TestStrategy extends DummyStrategy {
        private TestStrategy(int depth, TreeSearchParameters parameters) {
            super(depth, parameters);
        }

        @Override
        public boolean canAddToCluster(Cluster<TestObject> cluster, TestObject minorObject, NeighborhoodIterator<NucleotideSequence, TestObject[]> iterator) {
            return cluster.head.color == minorObject.color;
        }

        public DummyStrategy getDummy() {
            return new DummyStrategy(depth, parameters);
        }
    }

    private static class TestObject {
        final int count;
        final NucleotideSequence sequence;
        final int color;

        TestObject(int count, NucleotideSequence sequence) {
            this.count = count;
            this.sequence = sequence;
            this.color = 0;
        }

        TestObject(int count, String sequence) {
            this(count, new NucleotideSequence(sequence));
        }

        TestObject(int count, NucleotideSequence sequence, int color) {
            this.count = count;
            this.sequence = sequence;
            this.color = color;
        }

        @Override
        public String toString() {
            return sequence.toString() + " : " + count;
        }
    }
}