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
package com.milaboratory.core.io.sequence.fasta;

import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.Sequence;
import org.apache.commons.math3.random.Well1024a;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class FastaWriterTest {
    @Test
    public void test1() throws Exception {
        for (Alphabet alphabet : new Alphabet[]{NucleotideSequence.ALPHABET, AminoAcidSequence.ALPHABET}) {
            int count = 1000;
            FastaRecord[] reads = new FastaRecord[count];
            File temp = File.createTempFile("temp", ".fasta");
            temp.deleteOnExit();
            try (FastaWriter writer = new FastaWriter(temp, 50)) {
                for (int i = 0; i < count; ++i) {
                    reads[i] = randomRecord(alphabet, i);
                    writer.write(reads[i]);
                }
            }
            try (FastaReader reader = new FastaReader(temp, alphabet)) {
                for (int i = 0; i < count; ++i) {
                    FastaRecord actual = reader.take();
                    Assert.assertEquals(reads[i], actual);
                }
                Assert.assertTrue(reader.take() == null);
            }
            temp.delete();
        }
    }

    private static <S extends Sequence<S>> FastaRecord<S> randomRecord(Alphabet<S> alphabet, long id) {
        Well1024a random = new Well1024a(id);
        byte[] seq = new byte[50 + random.nextInt(150)];
        for (int i = 0; i < seq.length; ++i)
            seq[i] = (byte) random.nextInt(alphabet.size());
        return new FastaRecord<>(id, "id" + id, alphabet.createBuilder().append(seq).createAndDestroy());
    }
}