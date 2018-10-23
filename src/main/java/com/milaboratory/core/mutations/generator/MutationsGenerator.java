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
package com.milaboratory.core.mutations.generator;

import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.MutationsBuilder;
import com.milaboratory.core.sequence.NucleotideSequence;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class MutationsGenerator {
    private MutationsGenerator() {
    }

    public static Mutations<NucleotideSequence> generateMutations(NucleotideSequence sequence,
                                                                  NucleotideMutationModel model,
                                                                  Range range) {
        return generateMutations(sequence, model, range.getFrom(), range.getTo());
    }

    public static Mutations<NucleotideSequence> generateMutations(NucleotideSequence sequence,
                                                                  NucleotideMutationModel model,
                                                                  int from, int to) {
        MutationsBuilder<NucleotideSequence> builder = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        int mut, previous = NON_MUTATION;
        for (int i = from; i < to; ++i) {
            mut = model.generateMutation(i, sequence.codeAt(i));
            if (mut != NON_MUTATION) {
                switch (getRawTypeCode(mut)) {
                    case RAW_MUTATION_TYPE_SUBSTITUTION:
                        builder.append(mut);
                        break;
                    case RAW_MUTATION_TYPE_DELETION:
                        if (getRawTypeCode(previous) == RAW_MUTATION_TYPE_INSERTION)
                            mut = NON_MUTATION;
                        else
                            builder.append(mut);
                        break;
                    case RAW_MUTATION_TYPE_INSERTION:
                        if (getRawTypeCode(previous) == RAW_MUTATION_TYPE_DELETION)
                            mut = NON_MUTATION;
                        else {
                            builder.append(mut);
                            --i;
                        }
                        break;
                }
            }
            previous = mut;
        }

        mut = model.generateMutation(to, -1);
        if (getRawTypeCode(mut) == RAW_MUTATION_TYPE_INSERTION &&
                getRawTypeCode(previous) != RAW_MUTATION_TYPE_DELETION)
            builder.append(mut);

        return builder.createAndDestroy();
    }

    public static Mutations<NucleotideSequence> generateMutations(NucleotideSequence sequence,
                                                                  NucleotideMutationModel model) {
        return generateMutations(sequence, model, 0, sequence.size());
    }

}
