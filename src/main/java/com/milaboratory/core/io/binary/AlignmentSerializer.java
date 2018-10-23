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
package com.milaboratory.core.io.binary;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.Serializer;

public class AlignmentSerializer implements Serializer<Alignment> {
    @Override
    public void write(PrimitivO output, Alignment object) {
        output.writeObject(object.getSequence1());
        output.writeObject(object.getAbsoluteMutations());
        output.writeObject(object.getSequence1Range());
        output.writeObject(object.getSequence2Range());
        output.writeFloat(object.getScore());
    }

    @Override
    public Alignment read(PrimitivI input) {
        Sequence sequence = input.readObject(Sequence.class);
        Mutations mutations = input.readObject(Mutations.class);
        Range range1 = input.readObject(Range.class);
        Range range2 = input.readObject(Range.class);
        float score = input.readFloat();
        return new Alignment(sequence, mutations, range1, range2, score);
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public boolean handlesReference() {
        return false;
    }
}
