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

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.util.Factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Sequence tree map, with fast neighbours search. <p/> <p>Types of mutations: <br/> 0 = mismatch, <br/> 1 = deletion
 * (excess nucleotide in the reference sequence relative to the target key),<br/> 2 = insertion (missing nucleotide in
 * the reference sequence relative to the target key) </p>
 *
 * @param <S> - key type (must be a sequence)
 * @param <O> - value type
 */
public class SequenceTreeMap<S extends Sequence<S>, O>
        implements java.io.Serializable {
    public final Alphabet<S> alphabet;
    public final Node<O> root;

    /**
     * Creates a tree map for specified {@link com.milaboratory.core.sequence.Alphabet}.
     *
     * @param alphabet alphabet
     */
    public SequenceTreeMap(Alphabet<S> alphabet) {
        this.alphabet = alphabet;
        this.root = new Node<>(alphabet.size());
    }

    public O createIfAbsent(S sequence, Factory<O> factory) {
        final int size = sequence.size();
        Node<O> node = root;
        for (int i = 0; i < size; ++i)
            node = node.getOrCreate(sequence.codeAt(i));
        if (node.object == null)
            node.object = factory.create();
        return node.object;
    }

    public O put(S sequence, O object) {
        final int size = sequence.size();
        Node<O> node = root;
        for (int i = 0; i < size; ++i)
            node = node.getOrCreate(sequence.codeAt(i));
        O prev = node.object;
        node.object = object;
        return prev;
    }

    public Node<O> getNode(S sequence) {
        final int size = sequence.size();
        Node<O> node = root;
        for (int i = 0; i < size; ++i)
            if ((node = node.links[sequence.codeAt(i)]) == null)
                break;
        return node;
    }

    public O get(S sequence) {
        Node<O> node = getNode(sequence);
        if (node == null)
            return null;
        return node.object;
    }

    public O remove(S sequence) {
        final int size = sequence.size();
        Node<O> node = root;
        Node<O>[] nodes = new Node[size + 1];
        nodes[0] = root;
        int i, j;
        for (i = 0; i < size; ++i) {
            if ((node = node.links[sequence.codeAt(i)]) == null)
                break;
            nodes[i + 1] = node;
        }

        if (node == null)
            return null;

        final O ret = node.object;
        node.object = null;

        OUTER:
        for (i = size; i > 0; --i) {
            // Checking that current node not holding any object
            if (node.object != null)
                break;

            // Checking that this node is not involved in any branch of the trees
            for (j = alphabet.size() - 1; j >= 0; --j)
                if (node.links[j] != null)
                    break OUTER;

            // Next node (backtracking)
            node = nodes[i - 1];

            // Removing i-th node from the tree
            node.links[sequence.codeAt(i - 1)] = null;
        }

        return ret;
    }

    public Map<S, O> toMap() {
        HashMap<S, O> map = new HashMap<>();

        ValuesOp op = valuesOp();
        O n;
        while ((n = op.take()) != null)
            map.put(op.getSequence(), n);

        return map;
    }

    public NodeOp nodesOp() {
        return new NodeOp(root);
    }

    public ValuesOp valuesOp() {
        return new ValuesOp(root);
    }

    public NodeIterator nodeIterator() {
        return new NodeIterator(root);
    }

    public Iterable<O> values() {
        return CUtils.it(valuesOp());
    }

    public Iterable<Node<O>> nodes() {
        return new Iterable<Node<O>>() {
            @Override
            public java.util.Iterator<Node<O>> iterator() {
                return new NodeIterator(root);
            }
        };
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, int mismatches, int deletions, int insertions,
                                                              int totalErrors) {
        return getNeighborhoodIterator(reference,
                new TreeSearchParameters(mismatches, deletions, insertions, totalErrors));
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, int mismatches, int deletions, int insertions,
                                                              int totalErrors, MutationGuide<S> guide) {
        return getNeighborhoodIterator(reference,
                new TreeSearchParameters(mismatches, deletions, insertions, totalErrors),
                guide);
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, int mismatches, int deletions, int insertions) {
        return getNeighborhoodIterator(reference,
                new TreeSearchParameters(mismatches, deletions, insertions));
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, double maxPenalty,
                                                              double[] penalties, int[] maxErrors,
                                                              MutationGuide<S> guide) {
        return getNeighborhoodIterator(reference,
                new TreeSearchParameters(maxErrors, penalties, maxPenalty),
                guide);
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, TreeSearchParameters parameters) {
        return getNeighborhoodIterator(reference, parameters, null);
    }

    public NeighborhoodIterator<S, O> getNeighborhoodIterator(S reference, TreeSearchParameters parameters,
                                                              MutationGuide<S> guide) {
        return new NeighborhoodIterator<>(reference, parameters, guide, root);
    }

    public static final class Node<O> implements java.io.Serializable {
        final Node<O>[] links;
        O object;

        public Node(int letters) {
            this.links = new Node[letters];
        }

        public Node<O> getOrCreate(byte code) {
            Node node;
            if ((node = links[code]) == null)
                node = links[code] = new Node(links.length);
            return node;
        }

        public O getObject() {
            return object;
        }

        public void setObject(O object) {
            this.object = object;
        }

        // This implementation of equals() and hashCode()
        // is important for clusterization.

        @Override
        public boolean equals(Object o) {
            return (this == o);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    public final class NodeOp implements OutputPort<Node<O>>, java.io.Serializable {
        int pointer = 0;
        NodeWrapper<O>[] wrappers = new NodeWrapper[10];

        public NodeOp(Node<O> root) {
            wrappers[0] = new NodeWrapper<O>(root);
        }

        private void ensureNext() {
            if (pointer + 1 == wrappers.length)
                wrappers = Arrays.copyOf(wrappers,
                        (wrappers.length * 3) / 2 + 1);

            if (wrappers[pointer + 1] == null)
                wrappers[pointer + 1] = new NodeWrapper<O>();
        }

        @Override
        public Node<O> take() {
            if (pointer == -1)
                return null;

            NodeWrapper<O> nodeWrapper;
            Node<O> node;
            do {
                nodeWrapper = wrappers[pointer];
                node = nodeWrapper.getNext();
                if (node != null) {
                    ensureNext();
                    wrappers[++pointer].reset(node);
                    if (node.object != null)
                        return node;
                } else
                    --pointer;
            } while (pointer >= 0);

            return null;
        }

        public S getSequence() {
            SequenceBuilder<S> builder = alphabet.createBuilder().ensureCapacity(pointer);
            for (int i = 0; i < pointer; ++i)
                builder.append(wrappers[i].position);
            return builder.createAndDestroy();
        }
    }

    public final class NodeIterator extends CUtils.OPIterator<Node<O>>
            implements java.io.Serializable {
        public NodeIterator(Node<O> root) {
            super(new NodeOp(root));
        }

        @Override
        public Node<O> next() {
            return super.next();
        }

        public S getSequence() {
            return ((NodeOp) op).getSequence();
        }
    }

    public final class ValuesOp implements OutputPort<O>, java.io.Serializable {
        final NodeOp nodeOp;

        public ValuesOp(Node<O> root) {
            this.nodeOp = new NodeOp(root);
        }

        @Override
        public O take() {
            final Node<O> n = nodeOp.take();
            return n == null ? null : n.object;
        }

        public S getSequence() {
            return nodeOp.getSequence();
        }
    }

    private static final class NodeWrapper<O> implements java.io.Serializable {
        private byte position = -1;
        private Node<O> node;

        NodeWrapper() {
        }

        NodeWrapper(Node<O> node) {
            this.node = node;
        }

        void reset(Node<O> node) {
            this.node = node;
            this.position = -1;
        }

        Node<O> getNext() {
            while (++position < node.links.length)
                if (node.links[position] != null)
                    return node.links[position];
            return null;
        }
    }
}
