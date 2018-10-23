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
package com.milaboratory.core;

import com.milaboratory.core.io.sequence.PairedRead;
import com.milaboratory.core.io.sequence.SingleReadImpl;
import com.milaboratory.core.sequence.NSequenceWithQuality;
import org.junit.Assert;
import org.junit.Test;

public class PairedEndReadsLayoutTest {
    @Test
    public void test1() throws Exception {
        NSequenceWithQuality r1 = new NSequenceWithQuality("ATTAGACA");
        NSequenceWithQuality r2 = new NSequenceWithQuality("GATCAGT");

        PairedRead pr = new PairedRead(
                new SingleReadImpl(0, r1, "R1"),
                new SingleReadImpl(0, r2, "R2")
        );

        Target[] targets = PairedEndReadsLayout.ReverseOnly.createTargets(pr);
        Assert.assertEquals(1, targets.length);
        Assert.assertEquals(r2, targets[0].targets[0]);
        Assert.assertEquals(r1.getReverseComplement(), targets[0].targets[1]);

        targets = PairedEndReadsLayout.Collinear.createTargets(pr);
        Assert.assertEquals(2, targets.length);
        Assert.assertEquals(r1, targets[0].targets[0]);
        Assert.assertEquals(r2, targets[0].targets[1]);
        Assert.assertEquals(r2.getReverseComplement(), targets[1].targets[0]);
        Assert.assertEquals(r1.getReverseComplement(), targets[1].targets[1]);

        targets = PairedEndReadsLayout.Opposite.createTargets(pr);
        Assert.assertEquals(2, targets.length);
        Assert.assertEquals(r1, targets[0].targets[0]);
        Assert.assertEquals(r2.getReverseComplement(), targets[0].targets[1]);
        Assert.assertEquals(r2, targets[1].targets[0]);
        Assert.assertEquals(r1.getReverseComplement(), targets[1].targets[1]);
    }
}