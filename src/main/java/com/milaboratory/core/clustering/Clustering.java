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

import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.tree.NeighborhoodIterator;
import com.milaboratory.core.tree.SequenceTreeMap;
import com.milaboratory.core.tree.TreeSearchParameters;
import com.milaboratory.util.CanReportProgress;
import com.milaboratory.util.Factory;

import java.util.*;

import static com.milaboratory.core.tree.SequenceTreeMap.Node;

/**
 * Performs clustering of objects with similar sequences.
 *
 * Sequences corresponding to each object {@link T} are defined through {@link SequenceExtractor} provided by user.
 *
 * Object are considered to be comparable by {@link ClusteringStrategy} which extends {@link Comparator} interface.
 *
 * {@link ClusteringStrategy} also defines different clustering parameters, and clustering criteria
 * (by @{link ClusteringStrategy#canAddToCluster(com.milaboratory.core.clustering.Cluster, java.lang.Object, com.milaboratory.core.tree.NeighborhoodIterator)}).
 *
 * The following algorithm is performed:
 * 1. All objects are added to trie
 * 2. Iteration from bigger to smaller objects (reverse sorted by {@link ClusteringStrategy#compare(Object, Object)})
 *    a. For each object (iteration) all similar objects that fulfills {@link ClusteringStrategy} criteria are added to its cluster
 *    b. The same search is repeated for all objects added in (a)
 *    .... the same procedure repeated {@code depth} times
 *
 * @param <T> objects to be clustered
 * @param <S> sequence type
 */
public final class Clustering<T, S extends Sequence<S>> implements CanReportProgress {
    final Collection<T> inputObjects;
    final SequenceExtractor<T, S> sequenceExtractor;
    final ClusteringStrategy<T, S> strategy;
    final List<Cluster<T>> clusters = new ArrayList<>();
    volatile int progress;

    public Clustering(Collection<T> inputObjects, SequenceExtractor<T, S> sequenceExtractor,
                      ClusteringStrategy<T, S> strategy) {
        this.inputObjects = inputObjects;
        this.sequenceExtractor = sequenceExtractor;
        this.strategy = strategy;
    }

    @Override
    public double getProgress() {
        return (1.0 * progress) / inputObjects.size();
    }

    @Override
    public boolean isFinished() {
        return progress == inputObjects.size();
    }

    public List<Cluster<T>> performClustering() {
        try {
            if (inputObjects.isEmpty())
                return clusters;

            final Comparator<Cluster<T>> clusterComparator = getComparatorOfClusters(strategy, sequenceExtractor);
            // For performance
            final TreeSearchParameters params = strategy.getSearchParameters();
            final int maxDepth = strategy.getMaxClusterDepth();

            final List<T> objects = new ArrayList<>(inputObjects);
            // Reverse object sorting
            // Bigger objects will be in the beginning of the list
            Collections.sort(objects, getComparatorOfObjectsRegardingSequences(strategy, sequenceExtractor));

            @SuppressWarnings("unchecked")
            Alphabet<S> alphabet = sequenceExtractor.getSequence(objects.get(0)).getAlphabet();

            final Factory<T[]> arrayFactory = new Factory<T[]>() {
                @Override
                public T[] create() {
                    return (T[]) new Object[1];
                }
            };

            // Putting all objects into tree map
            // Objects with the same sequence placed into arrays (buckets)
            final SequenceTreeMap<S, T[]> tree = new SequenceTreeMap<>(alphabet);
            for (T object : objects) {
                T[] array = tree.createIfAbsent(sequenceExtractor.getSequence(object), arrayFactory);
                if (array[0] == null)
                    array[0] = object;
                else {
                    array = Arrays.copyOf(array, array.length + 1);
                    array[array.length - 1] = object;
                    tree.put(sequenceExtractor.getSequence(object), array);
                }
            }

            Node<T[]> current;

            // Used on each iteration to prevent double processing of the same trie node (NeighborhoodIterator may
            // return the same node several times)
            // Used as IdentityHashMap ...
            final HashSet<Node<T[]>> processedNodes = new HashSet<>();

            ArrayList<Cluster<T>> previousLayer = new ArrayList<>(), nextLayer = new ArrayList<>(), tmp;

            T[] temp;
            boolean inTree;
            // Iterating through objects from biggest to smallest
            for (int i = 0; i < objects.size(); ++i) {
                this.progress = i;
                T object = objects.get(i);

                // checking whether object is already clustered
                if ((temp = tree.get(sequenceExtractor.getSequence(object))) == null)
                    continue;
                inTree = false;
                for (T t : temp)
                    if (t == object) {
                        inTree = true;
                        break;
                    }
                if (!inTree)
                    continue;
                // <- object is not yet clustered

                // Creating single-object root cluster (no parent)
                Cluster<T> tempCluster = new Cluster<>(object);
                // Adding root cluster to the output cluster list
                clusters.add(tempCluster);
                previousLayer.clear();
                previousLayer.add(tempCluster);

                // Trying to add more objects to the cluster
                // Clustering more objects to objects clustered on the previous layer
                // First layer is cluster head
                for (int depth = 0; depth < maxDepth; ++depth) {

                    nextLayer.clear();
                    for (Cluster<T> previousCluster : previousLayer) {

                        NeighborhoodIterator<S, T[]> iterator = tree
                                .getNeighborhoodIterator(sequenceExtractor
                                        .getSequence(previousCluster.head), params, null);
                        processedNodes.clear();

                        while ((current = iterator.nextNode()) != null) {
                            if (!processedNodes.add(current))
                                continue;

                            T[] currentObjects = current.getObject();
                            T matchedObject = null;
                            // Will remain true by the end of iteration if this bucket contains no object
                            boolean allNulls = true;
                            for (int j = 0; j < currentObjects.length; j++) {
                                if (currentObjects[j] == null)
                                    continue;
                                matchedObject = currentObjects[j];

                                // Checking clusterization criteria:
                                //   1. previousCluster.head > matchedObject
                                //   2. strategy.canAddToCluster(previousCluster, matchedObject, iterator)
                                if (strategy.compare(previousCluster.head, matchedObject) <= 0
                                        || !strategy.canAddToCluster(previousCluster, matchedObject, iterator)) {
                                    // Can't cluster this object, it remains in the bucket in the trie
                                    // So, after iteration this bucket will contain at least one non-null object
                                    allNulls = false;
                                    continue;
                                }

                                // Creating child cluster
                                nextLayer.add(tempCluster = new Cluster<>(matchedObject, previousCluster));
                                // Adding new cluster as child to previousCluster
                                previousCluster.add(tempCluster);
                                // Removing the object from bucket
                                currentObjects[j] = null;
                            }
                            assert matchedObject != null;
                            if (allNulls)
                                // Removing bucket from trie if it contains no objects
                                tree.remove(sequenceExtractor.getSequence(matchedObject));
                        }

                        if (previousCluster.children != null)
                            // Reverse sorting cluster children
                            Collections.sort(previousCluster.children, clusterComparator);
                    }

                    // Reverse sorting newly collected layer
                    Collections.sort(nextLayer, clusterComparator);
                    // Swapping nextLayer <-and-> previousLayer
                    tmp = nextLayer;
                    nextLayer = previousLayer;
                    previousLayer = tmp;
                }
            }

            // Returning collected clusters
            return clusters;
        } finally {
            // Set finished state on method leave
            progress = inputObjects.size();
        }
    }

    public List<Cluster<T>> getClusters() {
        if (progress != inputObjects.size())
            throw new IllegalStateException("Not yet clustered.");
        return clusters;
    }

    /**
     * First REVERSE compare cluster heads (bigger objects comes first), if equal, REVERSE compare by head sequence.
     */
    static <T, S extends Sequence> Comparator<Cluster<T>>
    getComparatorOfClusters(final Comparator<T> objectComparator, final SequenceExtractor<T, S> extractor) {
        return new Comparator<Cluster<T>>() {
            @Override
            public int compare(Cluster<T> o1, Cluster<T> o2) {
                int i = objectComparator.compare(o2.head, o1.head); // Reverse comparison
                return i == 0 ?
                        extractor.getSequence(o2.head).compareTo(extractor.getSequence(o1.head))
                        : i;
            }
        };
    }

    /**
     * First REVERSE compare objects (bigger objects comes first), if equal, REVERSE compare object sequence.
     */
    static <T, S extends Sequence> Comparator<T>
    getComparatorOfObjectsRegardingSequences(final Comparator<T> objectComparator, final SequenceExtractor<T, S> extractor) {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                int i = objectComparator.compare(o2, o1);  // Reverse comparison
                return i == 0 ?
                        extractor.getSequence(o2).compareTo(extractor.getSequence(o1))
                        : i;
            }
        };
    }

    /**
     * Helper method. See class description.
     */
    public static <T, S extends Sequence<S>> List<Cluster<T>> performClustering(Collection<T> inputObjects,
                                                                                SequenceExtractor<T, S> sequenceExtractor,
                                                                                ClusteringStrategy<T, S> strategy) {
        return new Clustering<>(inputObjects, sequenceExtractor, strategy).performClustering();
    }
}
