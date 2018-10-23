/*
 * Copyright 2017 MiLaboratory.com
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
package com.milaboratory.core.io.sequence;

import com.milaboratory.core.sequence.NSequenceWithQuality;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.Serializer;

class IO {
    public static class SequenceReadSerializer implements Serializer<SequenceRead> {
        @Override
        public void write(PrimitivO output, SequenceRead object) {
            output.writeVarLong(object.getId());
            if (object instanceof SingleRead) {
                output.writeByte(1);
                SingleRead read = (SingleRead) object;
                output.writeObject(read.getData());
                output.writeObject(read.getDescription());
            } else {
                output.writeByte(object.numberOfReads());
                for (int i = 0; i < object.numberOfReads(); i++) {
                    SingleRead read = object.getRead(i);
                    output.writeObject(read.getData());
                    output.writeObject(read.getDescription());
                }
            }
        }

        @Override
        public SequenceRead read(PrimitivI input) {
            long id = input.readVarLong();
            byte readsCount = input.readByte();
            if (readsCount == 1) {
                NSequenceWithQuality seq = input.readObject(NSequenceWithQuality.class);
                String description = input.readObject(String.class);
                return new SingleReadImpl(id, seq, description);
            } else {
                SingleRead[] reads = new SingleRead[readsCount];
                for (int i = 0; i < readsCount; i++) {
                    NSequenceWithQuality seq = input.readObject(NSequenceWithQuality.class);
                    String description = input.readObject(String.class);
                    reads[i] = new SingleReadImpl(id, seq, description);
                }
                if (reads.length == 2)
                    return new PairedRead(reads);
                return new MultiRead(reads);
            }
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
}
