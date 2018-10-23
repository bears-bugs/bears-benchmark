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

import com.milaboratory.core.sequence.Sequence;

/**
 * Created by dbolotin on 25/06/14.
 */
final class BranchingEnumerator<S extends Sequence<S>, O> {
    //reference sequence
    final S reference;
    final MutationGuide guide;

    //Setup parameters
    byte mode;
    boolean autoMove1;

    //Runtime fields
    byte code;
    int position;
    SequenceTreeMap.Node<O> node;

    BranchingEnumerator(S reference, MutationGuide guide) {
        this.reference = reference;
        this.guide = guide;
    }

    /**
     * @param mode
     * @param autoMove1 used to prevent mutually compensating mutations
     */
    public void setup(byte mode, boolean autoMove1) {
        this.mode = mode;
        this.autoMove1 = autoMove1;
        this.node = null;
    }

    public void reset(int position, SequenceTreeMap.Node<O> node) {
        this.position = position;
        this.node = node;
        this.code = -1;

        if (autoMove1)
            move1();

        checkIterationEnd();
    }

    /**
     * Move the pointer one step forward. Move is made exactly matching the corresponding nucleotide in the reference
     * sequence, so this method prevents branching in the current position.
     */
    private void move1() {
        if (node == null)
            return;

        if (position >= reference.size()) {
            node = null;
            return;
        }

        node = node.links[reference.codeAt(position++)];
    }

    public void checkIterationEnd() {
        switch (mode) {
            case 0:
                if (position >= reference.size())
                    node = null;
                return;
            case 1:
                if (position >= reference.size() - 1 && code != -1)
                    node = null;
                return;
            case 2:
                if (position >= reference.size() + 1)
                    node = null;
                return;
        }

        throw new IllegalStateException();
    }

    public SequenceTreeMap.Node<O> next() {
        if (node == null)
            return null;

        switch (mode) {
            case 0:
                while (true) {
                    ++code;

                    if (code == reference.getAlphabet().size()) {
                        if (position >= reference.size() - 1)
                            return node = null;

                        code = 0;
                        node = node.links[reference.codeAt(position++)];

                        if (node == null)
                            return null;
                    }

                    if (code == reference.codeAt(position))
                        continue;

                    if (node.links[code] != null &&
                            (guide == null || guide.allowMutation(reference, position, (byte) 0, code)))
                        return node.links[code];
                }
            case 1:
                do {
                    if (position >= reference.size() - 1 && code != -1 || // ?
                            position >= reference.size()) //Out of sequence range
                        return node = null;

                    if (code != -1) {
                        node = node.links[reference.codeAt(position++)];
                    } else
                        code = 0;

                    if (guide == null || guide.allowMutation(reference, position, (byte) 1, (byte) -1))
                        return node;
                } while (node != null);
            case 2:
                while (true) {
                    ++code;

                    if (code == reference.getAlphabet().size()) {
                        if (position >= reference.size())
                            return node = null;

                        code = 0;
                        node = node.links[reference.codeAt(position++)];
                    }

                    if (node == null)
                        return null;

                    if (node.links[code] != null &&
                            (guide == null || guide.allowMutation(reference, position, (byte) 2, code)))
                        return node.links[code];
                }
        }

        return null;
    }

    /**
     * Returns the position of next nucleotide after branching.
     *
     * @return
     */
    public int getNextPositionAfterBranching() {
        switch (mode) {
            case 0:
                return position + 1;
            case 1:
                return position + 1;
            case 2:
                return position;
        }
        return -1;
    }

    public int getPosition() {
        return position;
    }
}
