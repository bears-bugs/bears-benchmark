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
package com.milaboratory.core.sequence;

import org.junit.Assert;
import org.junit.Test;

public class SequenceQualityBuilderTest {
    @Test
    public void testConcatenate1() throws Exception {
        SequenceQuality q1 = new SequenceQuality(new byte[]{1, 3, 2, 2}),
                q2 = new SequenceQuality(new byte[]{3, 2, 1, 2});

        Assert.assertEquals(new SequenceQuality(new byte[]{1, 3, 2, 2, 3, 2, 1, 2}),
                q1.concatenate(q2));
    }

    @Test
    public void testConcatenate2() throws Exception {
        SequenceQuality q1 = new SequenceQuality(new byte[]{1, 3, 2, 2}),
                q2 = new SequenceQuality(new byte[]{});

        Assert.assertEquals(new SequenceQuality(new byte[]{1, 3, 2, 2}),
                q1.concatenate(q2));
        Assert.assertEquals(new SequenceQuality(new byte[]{1, 3, 2, 2}),
                q2.concatenate(q1));
    }

    @Test
    public void testConcatenate3() throws Exception {
        SequenceQuality q1 = new SequenceQuality(new byte[]{}),
                q2 = new SequenceQuality(new byte[]{});

        Assert.assertEquals(new SequenceQuality(new byte[]{}),
                q1.concatenate(q2));
    }
}