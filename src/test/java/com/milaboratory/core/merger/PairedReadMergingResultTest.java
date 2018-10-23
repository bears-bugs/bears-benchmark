/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.core.merger;

import com.milaboratory.core.io.sequence.PairedRead;
import com.milaboratory.core.io.sequence.SingleReadImpl;
import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.core.sequence.NSequenceWithQuality;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.SequenceQuality;
import org.junit.Test;

import static com.milaboratory.core.merger.MergerParameters.IdentityType.Unweighted;

/**
 * Created by poslavsky on 15/04/15.
 */
public class PairedReadMergingResultTest {
    @Test
    public void test1() throws Exception {
        PairedReadMergingResult se = new PairedReadMergingResult(new PairedRead(
                new SingleReadImpl(12, new NSequenceWithQuality(new NucleotideSequence("atgc"), new SequenceQuality("++++")), "x"),
                new SingleReadImpl(12, new NSequenceWithQuality(new NucleotideSequence("atgc"), new SequenceQuality("++++")), "x")),
                new NSequenceWithQuality(new NucleotideSequence("atgc"), new SequenceQuality("++++")), 12, 3,false, 9,
                Unweighted, 0.9);
        IOTestUtil.assertJavaSerialization(se);
    }
}